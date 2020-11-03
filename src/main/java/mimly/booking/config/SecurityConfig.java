package mimly.booking.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();

//        http.csrf().disable();
//
//        http.authorizeRequests()
//                .antMatchers("/h2-console/**").permitAll()
//                .antMatchers("/login").permitAll()
//                .antMatchers("/registration").permitAll()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginPage("/login")
//                .loginProcessingUrl("/login")
//                .failureUrl("/login?error=Bad credentials")
//                .defaultSuccessUrl("/index", true)
//                .and()
//                .logout()
//                .logoutUrl("/logout")
//                .logoutSuccessUrl("/login?success=Signed out successfully")
//                .deleteCookies("JSESSIONID");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        super.configure(auth);
//        auth.userDetailsService(inMemoryUserDetailsManager());
//        auth.userDetailsService(jdbcUserDetailsManager());
//        JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> conf =
//                new JdbcUserDetailsManagerConfigurer<>(jdbcUserDetailsManager());
//        auth.apply(conf);
//        conf.dataSource(dataSource)
//                .withDefaultSchema().passwordEncoder(passwordEncoder())
//                .withUser("mimly")
//                .password(passwordEncoder().encode("mimly"))
//                .roles("USER");
    }
//
//    @Autowired
//    private DataSource dataSource;
//
//    @Bean
//    public JdbcUserDetailsManager jdbcUserDetailsManager() {
//        return new JdbcUserDetailsManager(dataSource);
//    }
//
//    @Bean
//    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
//        List<UserDetails> userDetailsList = new ArrayList<>();
//        userDetailsList.add(User
//                .withUsername("mimly")
//                .password(passwordEncoder().encode("mimly"))
//                .roles("USER")
//                .build()
//        );
//
//        return new InMemoryUserDetailsManager(userDetailsList);
//    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
