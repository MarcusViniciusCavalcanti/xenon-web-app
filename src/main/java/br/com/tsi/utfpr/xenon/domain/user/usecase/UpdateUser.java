package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.security.entity.Role;
import br.com.tsi.utfpr.xenon.domain.user.aggregator.UpdateDataUser;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.domain.user.service.FileService;
import br.com.tsi.utfpr.xenon.structure.dtos.TypeUserDto;
import br.com.tsi.utfpr.xenon.structure.dtos.inputs.InputUserDto;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateUser {

    private static final String MSG_NOT_FOUND_PATTER = "User by id %d not found";

    private final UserRepository userRepository;
    private final FileService fileService;
    private final UpdateDataUser updateDataUser;

    public InputUserDto findUserUpdate(Long id) {
        log.info("Running get user to update by id {}", id);
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

    @Transactional(propagation = Propagation.MANDATORY)
    public void update(Long id, InputUserDto userDto) {
        log.info("Running update user by id {}");
        userRepository.findById(id).ifPresentOrElse(
            updateDataUser.process(userDto).andThen(userRepository::save),
            () -> {
                throw new EntityNotFoundException(String.format(MSG_NOT_FOUND_PATTER, id));
            }
        );
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public Boolean updateAvatar(Long id, MultipartFile avatar) {
        log.info("Running save avatar user to id {}", id);
        var user = userRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException(String.format(MSG_NOT_FOUND_PATTER, id)));

        try {
            if (StringUtils.isBlank(user.getAvatar())) {
                var statusRegistry = user.getStatusRegistry();
                var newStatusRegistry = statusRegistry + 25;
                user.setStatusRegistry(newStatusRegistry);
            }

            var pathAvatar = fileService.saveAvatar(id, avatar.getInputStream());
            user.setAvatar(pathAvatar.toFile().getCanonicalPath());

            userRepository.save(user);

            return Boolean.TRUE;
        } catch (IOException | NullPointerException e) {
            log.error("Error in save disk avatar to id {}", id, e);
            return Boolean.FALSE;
        }
    }
}
