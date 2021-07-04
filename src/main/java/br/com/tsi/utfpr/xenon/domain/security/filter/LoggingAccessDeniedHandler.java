package br.com.tsi.utfpr.xenon.domain.security.filter;

import java.io.IOException;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LoggingAccessDeniedHandler implements AccessDeniedHandler {

    private static final String ERROR_WHEN_SEND_REDIRECT = "error when send redirect '{}'";

    @Override
    public void handle(HttpServletRequest req, HttpServletResponse res, AccessDeniedException ex) {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .ifPresentOrElse(
                auth -> {
                    log.info(
                        "'{}' was trying to access protected resource: '{}' | exception message: '{}'",
                        auth.getName(),
                        req.getRequestURI(),
                        ex.getMessage()
                    );
                    try {
                        res.sendRedirect(req.getContextPath() + "/access-denied");
                    } catch (IOException exception) {
                        log.error(ERROR_WHEN_SEND_REDIRECT, exception.getMessage());
                    }
                },
                () -> {
                    try {
                        SecurityContextHolder.clearContext();
                        res.sendRedirect(req.getContextPath() + "/access-denied-public");
                    } catch (IOException exception) {
                        log.error(ERROR_WHEN_SEND_REDIRECT, exception.getMessage());
                    }
                }
            );

    }
}
