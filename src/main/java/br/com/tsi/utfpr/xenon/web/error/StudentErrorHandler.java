package br.com.tsi.utfpr.xenon.web.error;

import br.com.tsi.utfpr.xenon.domain.user.exception.TokenException;
import br.com.tsi.utfpr.xenon.domain.user.exception.UsernameException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
public class StudentErrorHandler {

    @ExceptionHandler(UsernameException.class)
    public ModelAndView userExist(UsernameException exception) {
        return buildModelAndView(exception);
    }

    @ExceptionHandler(TokenException.class)
    public ModelAndView invalidToken(TokenException exception) {
        return buildModelAndView(exception);
    }

    private ModelAndView buildModelAndView(RuntimeException exception) {
        var model = new ModelAndView("error/error-registry");
        model.setStatus(HttpStatus.BAD_REQUEST);
        model.addObject("reason", exception.getMessage());

        return model;
    }
}
