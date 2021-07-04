package br.com.tsi.utfpr.xenon.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
public class SecurityController {

    private static final String ERROR_ACCESS_DENIED = "/error/access-denied";

    @GetMapping("/login")
    public String login() {
        log.info("Execute request to /login");
        return "security/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied() {
        log.info("Execute request to /access-denied");
        return ERROR_ACCESS_DENIED;
    }

    @GetMapping("/access-denied-public")
    public String accessDeniedPublic() {
        log.info("Execute request to /access-denied-public");
        return ERROR_ACCESS_DENIED;
    }

}
