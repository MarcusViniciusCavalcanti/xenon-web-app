package br.com.tsi.utfpr.xenon.web.error;

import br.com.tsi.utfpr.xenon.domain.security.exception.AuthoritiesNotAllowedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class UserErrorHandler {

    @ExceptionHandler(AuthoritiesNotAllowedException.class)
    public ModelAndView authoritiesNotAllowedException(AuthoritiesNotAllowedException exception) {
        var model = new ModelAndView("error/error-registry");
        var inputUserDto = exception.getInputUserDto();

        var translatedName = inputUserDto.getType().getTranslaterName();
        var msg = String.format(
            "O tipo de Usuário: [%s] não poderá ser atribuido o perfil Administrador",
            translatedName);

        model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        model.addObject("reason", msg);

        return model;
    }

    @ExceptionHandler(WebException.class)
    public ModelAndView runtimeException(WebException exception) {
        log.error("Error: ", exception);
        var model = new ModelAndView("error/error-registry");
        model.setStatus(HttpStatus.UNPROCESSABLE_ENTITY);
        model.addObject("reason", "Não foi possível executar ação, por favor tente mais tarde");

        return model;
    }
}
