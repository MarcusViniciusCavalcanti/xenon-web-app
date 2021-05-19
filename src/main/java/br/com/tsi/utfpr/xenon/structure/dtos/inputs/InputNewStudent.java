package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
public class InputNewStudent {

    @NotBlank
    private String tokenRegistry;

    @NotBlank
    @Size(min = 10, max = 255)
    private String name;

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@alunos.utfpr.edu.br$")
    private String email;

    @NotBlank
    @Size(min = 5, max = 8)
    private String password;

    @NotBlank
    @Size(min = 5, max = 8)
    private String confirmPassword;

    @NotBlank
    @Size(min = 5, max = 15)
    private String modelCar;

    @Pattern(regexp = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}")
    private String plateCar;
}
