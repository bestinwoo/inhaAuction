package project.inhaAuction.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class authController {
    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String register() {
        return "auth/register";
    }
    @RequestMapping(value = "login", method = RequestMethod.GET)
    public String login() {
        return "auth/login";
    }
}
