package br.com.tsi.utfpr.xenon.unit.web.controller;

import static org.junit.jupiter.api.Assertions.*;

import br.com.tsi.utfpr.xenon.web.controller.GraduationDegreeController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("Test - Unidade - SecurityController")
@ExtendWith(MockitoExtension.class)
class GraduationDegreeControllerTest {

    @InjectMocks
    private GraduationDegreeController graduationDegreeController;

    @Test
    @DisplayName("Deve retonar lista de cursos disponíveis")
    void shouldReturnAllCurse() {
        var degrees = graduationDegreeController.findAllGraduations();
        assertEquals(3, degrees.getBody().size());
    }
}
