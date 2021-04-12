package br.com.tsi.utfpr.xenon.structure.dtos;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccessCardDTO {

    private String username;

    private boolean accountNonExpired;

    private boolean accountNonLocked;

    private boolean credentialsNonExpired;

    private boolean enabled;

    private List<RoleDTO> roles;
}
