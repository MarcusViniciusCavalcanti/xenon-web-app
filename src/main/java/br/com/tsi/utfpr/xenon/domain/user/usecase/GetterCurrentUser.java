package br.com.tsi.utfpr.xenon.domain.user.usecase;

import br.com.tsi.utfpr.xenon.domain.security.entity.AccessCard;
import br.com.tsi.utfpr.xenon.domain.user.factory.UserFactory;
import br.com.tsi.utfpr.xenon.domain.user.repository.UserRepository;
import br.com.tsi.utfpr.xenon.structure.dtos.UserDto;
import javax.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class GetterCurrentUser {

    private final UserRepository userRepository;
    private final UserFactory userFactory;

    public UserDto currentUser() {
        var accessCard =
            (AccessCard) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var user = userRepository.findById(accessCard.getUser().getId())
            .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

        return userFactory.createUserDto(user);
    }
}
