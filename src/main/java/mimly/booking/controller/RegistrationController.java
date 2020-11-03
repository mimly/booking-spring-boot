//package mimly.booking.controller;
//
//import lombok.extern.slf4j.Slf4j;
//import mimly.booking.model.RegistrationDTO;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.provisioning.UserDetailsManager;
//import org.springframework.stereotype.Controller;
//import org.springframework.validation.BindingResult;
//import org.springframework.validation.ObjectError;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import javax.validation.Valid;
//import java.util.ArrayList;
//import java.util.List;
//
//@Controller
//@RequestMapping("/registration")
//@Slf4j(topic = "** Registration **")
//public class RegistrationController {
//
//    private final UserDetailsManager userDetailsManager;
//    private final PasswordEncoder passwordEncoder;
//
//    @Autowired
//    public RegistrationController(@Qualifier("jdbcUserDetailsManager") UserDetailsManager userDetailsManager, PasswordEncoder passwordEncoder) {
//        this.userDetailsManager = userDetailsManager;
//        this.passwordEncoder = passwordEncoder;
//    }
//
//    @GetMapping
//    public String get() {
//        return "registration";
//    }
//
//    @PostMapping
//    public String post(@Valid RegistrationDTO registrationDTO, BindingResult bindingResult) {
//        if (bindingResult.hasErrors()) {
//            List<ObjectError> errors = bindingResult.getAllErrors();
//            String error = errors.get(errors.size() - 1).getDefaultMessage();
//            return "redirect:/registration?error=" + StringUtils.capitalize(error);
//        }
//
//        if (this.userDetailsManager.userExists(registrationDTO.getUsername())) {
//            return "redirect:/registration?error=User already exists";
//        }
//
//        List<GrantedAuthority> authorities = new ArrayList<>();
//        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
//        UserDetails user = new User(
//                registrationDTO.getUsername(),
//                this.passwordEncoder.encode(registrationDTO.getPassword()),
//                authorities
//        );
//        this.userDetailsManager.createUser(user);
//        log.info(this.userDetailsManager.loadUserByUsername(registrationDTO.getUsername()).toString());
//        return "redirect:/registration?success=New account created";
//    }
//}


//import org.springframework.boot.web.servlet.error.ErrorController;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.stereotype.Component;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//@Component
//@RequestMapping("/")
//class MainErrorController implements ErrorController {
//
//    @GetMapping("/error")
//    public String get() {
//        log.warn("404 Not Found");
//        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//            return "redirect:/index";
//        }
//        return "redirect:/login";
//    }
//
//    @Override
//    public String getErrorPath() {
//        return "/error";
//    }
//}
//