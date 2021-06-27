package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateUser {

    private static final String MSG_NOT_FOUND_PATTER = "User by id %d not found";

    private final UserRepository userRepository;

    public InputUserDto findUserUpdate(Long id) {
        return userRepository.findById(id)
            .map(user -> {
                var input = new InputUserDto();
                var roleIds = user.getAccessCard().getRoles().stream()
                    .map(Role::getId)
                    .collect(Collectors.toList());

                input.setUsername(user.getAccessCard().getUsername());
                input.setName(user.getName());
                input.setAuthorities(roleIds);
                input.setAccountNonExpired(user.getAccessCard().isAccountNonExpired());
                input.setAccountNonLocked(user.getAccessCard().isAccountNonLocked());
                input.setCredentialsNonExpired(user.getAccessCard().isCredentialsNonExpired());
                input.setEnabled(user.getAccessCard().isEnabled());
                input.setType(TypeUserDto.valueOf(user.getTypeUser().name()));

                user.car().ifPresent(car -> {
                    input.setCarModel(car.getModel());
                    input.setCarPlate(car.getPlate());
                });

                return input;
            })
            .orElseThrow(
                () -> new EntityNotFoundException(String.format(MSG_NOT_FOUND_PATTER, id)));
    }
}
