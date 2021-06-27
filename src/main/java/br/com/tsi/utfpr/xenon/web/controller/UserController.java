package br.com.tsi.utfpr.xenon.web.controller;

import br.com.tsi.utfpr.xenon.application.service.SecurityApplicationService;
import br.com.tsi.utfpr.xenon.application.service.UserApplicationService;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDto;
import br.com.tsi.utfpr.xenon.web.validations.IsAdmin;
import java.io.IOException;
import java.nio.file.Files;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserApplicationService userApplicationService;
    private final SecurityApplicationService securityApplicationService;

    @IsAdmin
    @GetMapping("/usuarios/todos")
    public ModelAndView allUser() {
        log.info("Execute request to /usuarios/todos");
        var modelAndView = new ModelAndView("user/all/index");
        modelAndView.addObject("type", TypeUserDto.values());
        modelAndView.addObject("roles", securityApplicationService.getAllRoles());
        return modelAndView;
    }

    @IsAdmin
    @GetMapping("/users/all")
    public ResponseEntity<Page<UserDto>> findAll(ParamsSearchRequestDto params) {
        log.info("Execute request to /users/all with params {}", params.toString());
        var page = userApplicationService.findAllPageableUsers(params);
        return ResponseEntity.ok(page);
    }

    @IsAdmin
    @GetMapping("/usuarios/novo")
    public ModelAndView createNew(ModelAndView modelAndView) {
        var input = new InputUserDto();
        var roles = securityApplicationService.getAllRoles();

        modelAndView.setViewName("user/create/index");
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("input", input);
        return modelAndView;
    }

    @IsAdmin
    @PostMapping("/usuarios/novo-cadastro")
    public String saveNewUser(@Valid InputUserDto userDto, BindingResult result,
        RedirectAttributes redirectAttributes) {
        log.info("Execute request to /usuarios/novo-cadastro");
        if (result.hasErrors()) {
            log.info("Execute request invalid");
            redirectAttributes
                .addFlashAttribute("errors", "Campos com errors verifique e tente novamente");
            result.getFieldErrors().forEach(fieldError -> {
                log.debug("field error: {}", fieldError);
                redirectAttributes.addFlashAttribute(fieldError.getField(), fieldError);
            });

            return "redirect:/usuarios/novo";
        }

        userApplicationService.saveNewUser(userDto);
        return "redirect:/usuarios/todos";
    }

    @GetMapping("/perfil")
    public ModelAndView profile(ModelAndView modelAndView) {
        log.info("Execute request to /profile");
        var currentUser = securityApplicationService.currentUser();
        modelAndView.setViewName("user/profile/profile");
        modelAndView.addObject("user", currentUser);
        return modelAndView;
    }

    @ResponseBody
    @GetMapping("/user/avatar/{userId}")
    public byte[] getAvatar(@PathVariable("userId") Long id) throws IOException {
        log.info("Execute request to /user/avatar/{}", id);
        var file = userApplicationService.getAvatar(id);
        return Files.readAllBytes(file.toPath());
    }

    @IsAdmin
    @GetMapping("/usuario/atualizar/{id}")
    public ModelAndView updateUser(@PathVariable("id") Long id, ModelAndView modelAndView) {
        log.info("Execute request to /usuario/atualizar/{}", id);
        var input = userApplicationService.findUserUpdate(id);
        var roles = securityApplicationService.getAllRoles();

        modelAndView.setViewName("user/update/index");
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("input", input);
        return modelAndView;
    }
}
