package br.com.tsi.utfpr.xenon.web.controller;

import br.com.tsi.utfpr.xenon.structure.dtos.inputs.ParamsSearchRequestDTO;
import br.com.tsi.utfpr.xenon.web.validations.IsAdmin;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserController {

    @IsAdmin
    @GetMapping("/usuarios/todos")
    public ModelAndView allUser() {
        log.info("Execute request to /usuarios/todos");
        return new ModelAndView("user/all/index");
    }

    @IsAdmin
    @GetMapping("/users/all")
    public ResponseEntity<?> findAll(ParamsSearchRequestDTO params) {
        return ResponseEntity.noContent().build();
    }
}
