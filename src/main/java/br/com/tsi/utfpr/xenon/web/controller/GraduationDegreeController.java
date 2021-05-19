package br.com.tsi.utfpr.xenon.web.controller;

import br.com.tsi.utfpr.xenon.structure.dtos.GraduationDegreeDto;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@Controller
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GraduationDegreeController {

    @GetMapping("/graduations-degree")
    public ResponseEntity<List<GraduationDegreeDto>> findAllGraduations() {
        log.info("Execute request to /graduations-degree");
        var list = List.of(
            GraduationDegreeDto.builder().name("Sistema para Internet").id(1).build(),
            GraduationDegreeDto.builder().name("Eng. Civil").id(2).build(),
            GraduationDegreeDto.builder().name("Eng. Mecânica").id(2).build()
        );

        return ResponseEntity.ok(list);
    }
}
