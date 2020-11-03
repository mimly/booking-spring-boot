package mimly.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@Slf4j(topic = "** Main **")
public class MainController {

//    @GetMapping("/")
//    public String get() {
//        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
//            return "redirect:/index";
//        }
//        return "redirect:/login";
//    }
}