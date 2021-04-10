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
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException e) {
        Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication())
            .ifPresentOrElse(
                auth -> {
                    log.info(
                        "'{}' was trying to access protected resource: '{}' | exception message: '{}'",
                        auth.getName(),
                        request.getRequestURI(),
                        e.getMessage()
                    );
                    try {
                        response.sendRedirect(request.getContextPath() + "/access-denied");
                    } catch (IOException ex) {
                        log.error(ERROR_WHEN_SEND_REDIRECT, ex.getMessage());
                    }
                },
                () -> {
                    try {
                        SecurityContextHolder.clearContext();
                        response.sendRedirect(request.getContextPath() + "/access-denied-public");
                    } catch (IOException ex) {
                        log.error(ERROR_WHEN_SEND_REDIRECT, ex.getMessage());
                    }
                }
            );

    }
}
