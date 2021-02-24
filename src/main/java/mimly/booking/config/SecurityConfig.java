package mimly.booking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import mimly.booking.controller.BookingApplicationError;
import mimly.booking.controller.XSSFilter;
import mimly.booking.model.Assistant;
import mimly.booking.model.repository.AssistantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.header.HeaderWriterFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static mimly.booking.config.AddressBook.API_VERSION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ROLE_ASSISTANT = "ROLE_ASSISTANT";
    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .and()
                .addFilterBefore(XSSFilter(), WebAsyncManagerIntegrationFilter.class)
                .addFilterBefore(commonsRequestLoggingFilter(), HeaderWriterFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .accessDeniedHandler(accessDeniedHandler());

        http.sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .sessionFixation().none();

        http.authorizeRequests()
                .antMatchers("/actuator/**", "/h2-console/**", "/hazelcast/**").hasAuthority(ROLE_ADMIN)
                .antMatchers("/", "/oldschool.html", "/favicon.ico", "/css/**", "/js/**", "/login/**").permitAll()
                //.antMatchers("/api/rest/v1/**").permitAll()
                .antMatchers("/api/v1/**", "/ws-api/v1/**").permitAll()
                .antMatchers("/api/secured/v1/**", "/ws-api/secured/v1/**").hasAnyAuthority(ROLE_ADMIN, ROLE_ASSISTANT)
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .and()
                .logout()
                .deleteCookies("SESSION");
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public ErrorAttributes errorAttributes() {
        return new DefaultErrorAttributes() {
            @Override
            public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
                Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);
                errorAttributes.putIfAbsent("apiVersion", API_VERSION);
                errorAttributes.replace("timestamp", String.valueOf(ZonedDateTime.now().truncatedTo(ChronoUnit.MILLIS)));
                errorAttributes.remove("trace");
                return errorAttributes;
            }
        };
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new AuthenticationEntryPoint() {
            @Override
            public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter().writeValue(
                        response.getWriter(),
                        BookingApplicationError.builder()
                                .httpServletRequest(request)
                                .httpStatus(HttpStatus.UNAUTHORIZED)
                                .throwable(authException)
                                .build()
                );
            }
        };
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
                response.setContentType(APPLICATION_JSON_VALUE);
                response.setStatus(HttpStatus.FORBIDDEN.value());
                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter().writeValue(
                        response.getWriter(),
                        BookingApplicationError.builder()
                                .httpServletRequest(request)
                                .httpStatus(HttpStatus.FORBIDDEN)
                                .throwable(accessDeniedException)
                                .build()
                );
            }
        };
    }

    @Bean
    public XSSFilter XSSFilter() {
        return new XSSFilter();
    }

    @Bean
    public CommonsRequestLoggingFilter commonsRequestLoggingFilter() {
        CommonsRequestLoggingFilter commonsRequestLoggingFilter = new CommonsRequestLoggingFilter();
        commonsRequestLoggingFilter.setIncludeClientInfo(true);
        commonsRequestLoggingFilter.setIncludeQueryString(true);
        commonsRequestLoggingFilter.setIncludePayload(true);
        commonsRequestLoggingFilter.setMaxPayloadLength(256);
        commonsRequestLoggingFilter.setIncludeHeaders(false);
        return commonsRequestLoggingFilter;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(inMemoryUserDetailsManager())
                .passwordEncoder(passwordEncoder());

        auth.inMemoryAuthentication()
                .withUser("mimly")
                .password(passwordEncoder().encode("mimly"))
                .roles("ADMIN");
    }

    @Bean
    public UserDetailsService inMemoryUserDetailsManager() {
        return new UserDetailsService() {

            @Autowired
            private AssistantRepository assistantRepository;

            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                Assistant assistant = assistantRepository.findByUsername(username);
                if (assistant == null) {
                    throw new UsernameNotFoundException(username);
                }
                return assistant;
            }
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}