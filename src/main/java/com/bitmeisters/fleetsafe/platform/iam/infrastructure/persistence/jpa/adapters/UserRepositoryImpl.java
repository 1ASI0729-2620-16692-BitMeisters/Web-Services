package com.bitmeisters.fleetsafe.platform.iam.infrastructure.persistence.jpa.adapters;

import com.bitmeisters.fleetsafe.platform.iam.domain.model.aggregates.User;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.CompanyId;
import com.bitmeisters.fleetsafe.platform.iam.domain.model.valueobjects.EmailAddress;
import com.bitmeisters.fleetsafe.platform.iam.domain.repositories.UserRepository;
import com.bitmeisters.fleetsafe.platform.iam.infrastructure.persistence.jpa.assemblers.UserPersistenceAssembler;
import com.bitmeisters.fleetsafe.platform.iam.infrastructure.persistence.jpa.repositories.UserPersistenceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final UserPersistenceRepository userPersistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    public UserRepositoryImpl(UserPersistenceRepository userPersistenceRepository, ApplicationEventPublisher eventPublisher) {
        this.userPersistenceRepository = userPersistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<User> findById(UUID id) {
        return userPersistenceRepository.findById(id).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public Optional<User> findByEmail(EmailAddress email) {
        return userPersistenceRepository.findByEmail(email.value()).map(UserPersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<User> findAllByCompanyId(CompanyId companyId) {
        return userPersistenceRepository.findAllByCompanyIdOrderByLastNameAscFirstNameAsc(companyId.value()).stream()
                .map(UserPersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public User save(User user) {
        var events = List.copyOf(user.domainEvents());
        var saved = userPersistenceRepository.save(UserPersistenceAssembler.toPersistenceFromDomain(user));
        events.forEach(eventPublisher::publishEvent);
        user.clearDomainEvents();
        return UserPersistenceAssembler.toDomainFromPersistence(saved);
    }

    @Override
    public boolean existsByEmail(EmailAddress email) {
        return userPersistenceRepository.existsByEmail(email.value());
    }
}
