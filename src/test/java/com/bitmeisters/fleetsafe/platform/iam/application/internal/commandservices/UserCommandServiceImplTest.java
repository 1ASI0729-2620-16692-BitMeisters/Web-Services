package com.bitmeisters.fleetsafe.platform.iam.application.internal.commandservices;

import com.bitmeisters.fleetsafe.platform.iam.application.commandservices.UserCommandFailure;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.aggregates.User;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.commands.ChangeUserRoleCommand;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.commands.DeactivateUserCommand;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.commands.RegisterUserCommand;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.CompanyId;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.EmailAddress;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.PersonName;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.Role;
import com.bitmeisters.fleetsafe.platform.iam.domain.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserCommandServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserCommandServiceImpl userCommandService;

    private final RegisterUserCommand registerSupervisor = new RegisterUserCommand(
            new CompanyId(UUID.randomUUID()),
            new EmailAddress("supervisor@transportesandinos.pe"),
            "$2a$10$hash",
            new PersonName("Rosa", "Quispe"),
            Role.FLEET_SUPERVISOR);

    @Test
    void registersAUserWithANewEmail() {
        when(userRepository.existsByEmail(registerSupervisor.email())).thenReturn(false);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = userCommandService.handle(registerSupervisor);

        assertThat(result.success()).get().extracting(User::getRole).isEqualTo(Role.FLEET_SUPERVISOR);
    }

    @Test
    void rejectsAnEmailThatIsAlreadyRegistered() {
        when(userRepository.existsByEmail(registerSupervisor.email())).thenReturn(true);

        var result = userCommandService.handle(registerSupervisor);

        assertThat(result.failure()).get().isInstanceOf(UserCommandFailure.EmailAlreadyRegistered.class);
        verify(userRepository, never()).save(any());
    }

    @Test
    void changesTheRoleOfAnExistingUser() {
        var id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(new User(registerSupervisor)));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        var result = userCommandService.handle(new ChangeUserRoleCommand(id, Role.ADMINISTRATOR));

        assertThat(result.success()).get().extracting(User::getRole).isEqualTo(Role.ADMINISTRATOR);
    }

    @Test
    void reportsAUserThatDoesNotExist() {
        var id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        var result = userCommandService.handle(new ChangeUserRoleCommand(id, Role.DRIVER));

        assertThat(result.failure()).get().isInstanceOf(UserCommandFailure.NotFound.class);
    }

    @Test
    void rejectsDeactivatingAUserTwice() {
        var user = new User(registerSupervisor);
        user.deactivate();
        var id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        var result = userCommandService.handle(new DeactivateUserCommand(id));

        assertThat(result.failure()).get().isInstanceOf(UserCommandFailure.AlreadyInactive.class);
        verify(userRepository, never()).save(any());
    }
}
