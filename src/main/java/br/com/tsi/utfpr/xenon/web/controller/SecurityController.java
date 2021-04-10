package br.com.tsi.utfpr.xenon.web.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SecurityController {

    private static final String ERROR_ACCESS_DENIED = "/error/access-denied";

    @GetMapping("/login")
    public String login() {
        if (isDiffAnonymousUser()) {
            return "redirect:/home";
        }

        return "security/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        return ERROR_ACCESS_DENIED;
    }

    @GetMapping("/access-denied-public")
    public String accessDeniedPublic() {
        return ERROR_ACCESS_DENIED;
    }

    private boolean isDiffAnonymousUser() {
        return !SecurityContextHolder.getContext().getAuthentication().getName()
            .equals("anonymousUser");
    }
}
