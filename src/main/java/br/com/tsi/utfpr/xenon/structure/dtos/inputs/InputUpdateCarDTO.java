package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class InputUpdateCarDTO {

    @NotBlank(message = "Não deve ser nulo ou branco")
    private String carModel;

    @NotBlank(message = "Não deve ser nulo ou branco")
    private String carPlate;

}
