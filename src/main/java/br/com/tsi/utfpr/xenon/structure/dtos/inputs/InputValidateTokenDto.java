package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class InputValidateTokenDto {

    @Pattern(regexp = "([a-f0-9]{8}(-[a-f0-9]{4}){4}[a-f0-9]{8})")
    private String token;
}
