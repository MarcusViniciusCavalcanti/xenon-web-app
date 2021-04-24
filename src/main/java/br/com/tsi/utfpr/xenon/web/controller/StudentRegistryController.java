package br.com.tsi.utfpr.xenon.web.controller;

import br.com.tsi.utfpr.xenon.application.service.RegistryStudentsApplicationService;
import br.com.tsi.utfpr.xenon.structure.dtos.ErrorDto;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultCheckerDto.Result;
import br.com.tsi.utfpr.xenon.structure.dtos.ResultUsernameCheckerDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputEmailDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputNewStudent;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidatePlateDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputValidateTokenDto;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentRegistryController {

    private final RegistryStudentsApplicationService registryStudentsApplicationService;

    @GetMapping("/novo-registro")
    public String newRegistry() {
        return "user/registry/new-registry";
    }

    @PostMapping(value = "/registry", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> includeNewRegistry(@Valid @RequestBody InputEmailDto emailDto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            var error = ResultUsernameCheckerDto.builder()
                .result(ResultCheckerDto.builder()
                    .value(Result.USER_CAN_BE_REGISTERED)
                    .reason(
                        "E-email inválido por favor use o institucional ex: seu_email@alunos.utfpr.edu.br")
                    .build())
                .username(emailDto.getValue())
                .build();
            return ResponseEntity.badRequest().body(error);
        }

        registryStudentsApplicationService.includeNewRegistry(emailDto.getValue());

        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/validate-token", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> validateToken(
        @Valid @RequestBody InputValidateTokenDto inputValidateTokenDto,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildResponseError(
                String.format("Token inválido %s", inputValidateTokenDto.getToken()));
        }

        return ResponseEntity.accepted().build();
    }

    @PostMapping(value = "/validate-plate", consumes = "application/json", produces = "application/json")
    public ResponseEntity<Boolean> plateExist(@Valid @RequestBody InputValidatePlateDto input,
        BindingResult bindingResult) {
        log.info("Running request to /validate-plate");

        if (bindingResult.hasErrors()) {
            return ResponseEntity.ok(false);
        }

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // TODO #1 criar validador para placa de carror.
        return ResponseEntity.ok(true);
    }

    @PostMapping(value = "/registry-students", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> createNewStudents(@Valid @RequestBody InputNewStudent inputNewStudent,
        BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return buildResponseError(
                "Informações inválidas por favor verifique as informações e tente novamente");
        }

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    private ResponseEntity<?> buildResponseError(String msg) {
        var error = ErrorDto.builder()
            .msg(msg)
            .build();

        return ResponseEntity.badRequest().body(error);
    }
}
