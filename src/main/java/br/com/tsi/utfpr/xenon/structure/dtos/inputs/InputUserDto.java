package br.com.tsi.utfpr.xenon.structure.dtos.inputs;

import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class InputUserDto {

    @NotBlank(message = "Não deve ser nulo ou branco")
    @Size(min = 5, max = 255, message = "O tamanho deve estar entre 5 à 255 caracteres")
    private String name;

    @NotBlank(message = "Não deve ser nulo ou branco")
    @Size(min = 5, max = 200, message = "O tamanho deve estar entre 5 à 200 caracteres")
    private String username;

    @NotBlank(message = "Não deve ser nulo ou branco")
    private String password;

    @NotEmpty(message = "Lista não pode ser vazia")
    private List<Long> authorities;

    @NotNull(message = "Não deve estar em branco ou não ser outros valores que não sejam: SERVICE, STUDENTS")
    private TypeUserDto type;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private boolean authorizedAccess;

    private String carPlate;

    private String carModel;

}
