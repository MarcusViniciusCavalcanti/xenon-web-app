package br.com.tsi.utfpr.xenon.web.controller;

import br.com.tsi.utfpr.xenon.application.service.RegistryStudentsApplicationService;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputEmailDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidateTokenDto;
import java.util.Objects;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentRegistryController {

    public static final String REDIRECT_CADASTRO_ESTUDANTE = "redirect:/cadastro-estudante";
    public static final String INPUT_EMAIL = "inputEmail";
    private static final String REDIRECT_NOVO_REGISTRO = "redirect:/novo-registro";
    private static final String REDIRECT_VALIDAR_TOKEN = "redirect:/validar-token";
    private static final String ERROR_REGISTRY = "error-registry";
    private static final String USER_REGISTRY_TOKEN_REGISTRY = "user/registry/token-registry";
    private static final String ERROR = "error";
    private static final String EMAIL = "email";
    private static final String TOKEN = "token";

    private final RegistryStudentsApplicationService registryStudentsApplicationService;

    @GetMapping("/novo-registro")
    public ModelAndView newRegistry(ModelAndView modelAndView, InputEmailDto inputEmailDto) {
        log.info("Execute request to /novo-registro");
        modelAndView.setViewName("user/registry/email-registry");
        modelAndView.addObject(INPUT_EMAIL, inputEmailDto);
        return modelAndView;
    }

    @PostMapping("/incluir-registro")
    public String includeNewRegistry(@Valid InputEmailDto emailDto, BindingResult result,
        RedirectAttributes redirectAttributes) {
        log.info("Execute request to /novo-registro");
        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(ERROR, result.getFieldError(EMAIL));
            return REDIRECT_NOVO_REGISTRO;
        }

        registryStudentsApplicationService.includeNewRegistry(emailDto.getEmail());
        redirectAttributes.addAttribute(EMAIL, emailDto.getEmail());

        return REDIRECT_VALIDAR_TOKEN;
    }

    @GetMapping("/validar-token")
    public ModelAndView validateToken(InputValidateTokenDto inputToken, ModelAndView modelAndView) {
        log.info("Execute request to /validar-token");
        if (Objects.isNull(inputToken.getEmail()) || inputToken.getEmail().isEmpty()) {
            return new ModelAndView(REDIRECT_NOVO_REGISTRO);
        }

        var inputEmail = new InputEmailDto();
        inputEmail.setEmail(inputToken.getEmail());
        modelAndView.setViewName(USER_REGISTRY_TOKEN_REGISTRY);
        modelAndView.addObject("inputToken", inputToken);
        modelAndView.addObject(INPUT_EMAIL, inputEmail);
        return modelAndView;
    }

    @PostMapping(value = "/valide-token")
    public String validateToken(@Valid InputValidateTokenDto inputToken, BindingResult result,
        RedirectAttributes redirectAttributes) {
        log.info("Execute request to /valide-token");
        redirectAttributes.addAttribute(EMAIL, inputToken.getEmail());

        if (result.hasErrors()) {
            redirectAttributes.addFlashAttribute(ERROR, result.getFieldError(TOKEN));
            return REDIRECT_VALIDAR_TOKEN;
        }

        var token = registryStudentsApplicationService.validateToken(inputToken);
        redirectAttributes.addAttribute(TOKEN, token);

        return REDIRECT_CADASTRO_ESTUDANTE;
    }

    @GetMapping("/cadastro-estudante")
    public ModelAndView registryStudent(ModelAndView modelAndView,
        InputNewStudent inputNewStudent) {
        log.info("Execute request to /cadastro-estudante");
        modelAndView.setViewName("user/registry/student-registry");
        modelAndView.addObject("input", inputNewStudent);
        return modelAndView;
    }

    @PostMapping("/cadastre-novo-estudante")
    public String registryNewStudent(@Valid InputNewStudent inputNewStudent, BindingResult result,
        RedirectAttributes redirectAttributes) {
        log.info("Execute request to /cadastre-novo-estudante");
        if (result.hasErrors()) {
            redirectAttributes
                .addFlashAttribute("errors", "Campos com errors verifique e tente novamente");
            result.getFieldErrors().forEach(fieldError -> redirectAttributes
                .addFlashAttribute(fieldError.getField(), fieldError));

            redirectAttributes.addAttribute(TOKEN, inputNewStudent.getToken());
            redirectAttributes.addAttribute(EMAIL, inputNewStudent.getEmail());

            return REDIRECT_CADASTRO_ESTUDANTE;
        }

        registryStudentsApplicationService.registryNewStudent(inputNewStudent);
        return "redirect:/concluido";
    }

    @GetMapping("/concluido")
    public ModelAndView complete(ModelAndView modelAndView) {
        log.info("Execute request to /concluido");
        modelAndView.setViewName("/user/registry/complete.html");
        return modelAndView;
    }
}
