package br.com.tsi.utfpr.xenon.structure.dtos;

import java.util.Optional;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserDTO {
    private Long id;

    private String name;

    private TypeUserDTO typeUser;

    private AccessCardDto accessCard;

    private CarDTO car;

    public Optional<CarDTO> car() {
        return Optional.ofNullable(car);
    }
}
