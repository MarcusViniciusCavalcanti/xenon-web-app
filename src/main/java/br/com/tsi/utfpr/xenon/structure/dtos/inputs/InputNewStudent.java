package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class InputNewStudent {

    @NotBlank(message = "não deve estar em branco")
    private String token;

    @NotBlank(message = "não deve estar em branco")
    @Size(min = 10, max = 255, message = "tamanho deve ser entre 10 e 255")
    private String name;

    @NotBlank(message = "não deve estar em branco")
    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@alunos.utfpr.edu.br$")
    private String email;

    @NotBlank(message = "não deve estar em branco")
    @Size(min = 5, max = 8, message = "tamanho deve ser entre 5 e 8")
    private String password;

    @NotBlank(message = "não deve estar em branco")
    @Size(min = 5, max = 8, message = "tamanho deve ser entre 5 e 8")
    private String confirmPassword;

    @NotBlank(message = "não deve estar em branco")
    @Size(min = 3, max = 15, message = "tamanho deve ser entre 3 e 15")
    private String modelCar;

    @Pattern(regexp = "[aA-zZ]{3}[0-9][0-9aA-zZ][0-9]{2}")
    private String plateCar;
}
