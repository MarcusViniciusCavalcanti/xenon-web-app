package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import javax.validation.constraints.Pattern;
import lombok.Data;

@Data
public class InputValidatePlateDto {

    @Pattern(regexp = "[A-Z]{3}[0-9][0-9A-Z][0-9]{2}")
    private String value;
}
