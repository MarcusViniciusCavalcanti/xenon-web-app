package br.com.tsi.utfpr.xenon.structure.dtos;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AccessCardDto {

    private final String username;
    private final boolean accountNonExpired;
    private final boolean accountNonLocked;
    private final boolean credentialsNonExpired;
    private final boolean enabled;
    private final List<RoleDTO> roles;
}
