package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class InputEmailDto {

    @NotBlank
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@alunos.utfpr.edu.br$")
    private String email;
}
