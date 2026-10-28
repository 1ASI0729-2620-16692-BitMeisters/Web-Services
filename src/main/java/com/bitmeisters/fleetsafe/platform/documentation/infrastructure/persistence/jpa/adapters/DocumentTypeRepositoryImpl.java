package com.bitmeisters.fleetsafe.platform.documentation.infrastructure.persistence.jpa.adapters;

import com.bitmeisters.fleetsafe.platform.documentation.domain.model.aggregates.DocumentType;
import com.bitmeisters.fleetsafe.platform.documentation.domain.model.valueobjects.DocumentTypeCode;
import com.bitmeisters.fleetsafe.platform.documentation.domain.model.valueobjects.DocumentTypeId;
import com.bitmeisters.fleetsafe.platform.documentation.domain.repositories.DocumentTypeRepository;
import com.bitmeisters.fleetsafe.platform.documentation.infrastructure.persistence.jpa.assemblers.DocumentTypePersistenceAssembler;
import com.bitmeisters.fleetsafe.platform.documentation.infrastructure.persistence.jpa.repositories.DocumentTypePersistenceRepository;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class DocumentTypeRepositoryImpl implements DocumentTypeRepository {

    private final DocumentTypePersistenceRepository documentTypePersistenceRepository;
    private final ApplicationEventPublisher eventPublisher;

    public DocumentTypeRepositoryImpl(DocumentTypePersistenceRepository documentTypePersistenceRepository,
                                      ApplicationEventPublisher eventPublisher) {
        this.documentTypePersistenceRepository = documentTypePersistenceRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public Optional<DocumentType> findById(DocumentTypeId id) {
        return documentTypePersistenceRepository.findById(id.value()).map(DocumentTypePersistenceAssembler::toDomainFromPersistence);
    }

    @Override
    public List<DocumentType> findAll() {
        return documentTypePersistenceRepository.findAll().stream()
                .map(DocumentTypePersistenceAssembler::toDomainFromPersistence)
                .toList();
    }

    @Override
    public boolean existsById(DocumentTypeId id) {
        return documentTypePersistenceRepository.existsById(id.value());
    }

    @Override
    public boolean existsByCode(DocumentTypeCode code) {
        return documentTypePersistenceRepository.existsByCode(code.value());
    }

    @Override
    public DocumentType save(DocumentType documentType) {
        var events = List.copyOf(documentType.domainEvents());
        var saved = documentTypePersistenceRepository.save(DocumentTypePersistenceAssembler.toPersistenceFromDomain(documentType));
        events.forEach(eventPublisher::publishEvent);
        documentType.clearDomainEvents();
        return DocumentTypePersistenceAssembler.toDomainFromPersistence(saved);
    }
}
