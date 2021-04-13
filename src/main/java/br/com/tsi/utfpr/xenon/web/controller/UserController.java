package br.com.tsi.utfpr.xenon.web.controller;

import br.com.tsi.utfpr.xenon.application.service.UserApplicationService;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDTO;
import br.com.tsi.utfpr.xenon.web.validations.IsAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    private final UserApplicationService userApplicationService;

    @IsAdmin
    @GetMapping("/usuarios/todos")
    public ModelAndView allUser() {
        log.info("Execute request to /usuarios/todos");
        return new ModelAndView("user/all/index");
    }

    @IsAdmin
    @GetMapping("/users/all")
    public ResponseEntity<Page<UserDto>> findAll(ParamsSearchRequestDTO params) {
        log.info("Execute request to /users/all with params {}", params);
        var page = userApplicationService.findAllPageableUsers(params);
        return ResponseEntity.ok(page);
    }
}
