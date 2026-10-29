package com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.seed;

import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Company;
import com.bitmeisters.fleetsafe.platform.fleet.domain.model.aggregates.Fleet;
import com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.repositories.CompanyRepository;
import com.bitmeisters.fleetsafe.platform.fleet.infrastructure.persistence.jpa.repositories.FleetRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Creates a first company and fleet when the database has none.
 * <p>
 * Fleet Management has no command to create companies or fleets, yet a vehicle cannot be registered
 * without a fleet. It does nothing once a company exists and can be turned off with
 * {@code fleetsafe.seed.enabled=false}.
 */
@Slf4j
@Component
public class FleetDataInitializer implements ApplicationRunner {

    private final CompanyRepository companyRepository;
    private final FleetRepository fleetRepository;

    @Value("${fleetsafe.seed.enabled:true}")
    private boolean enabled;

    public FleetDataInitializer(CompanyRepository companyRepository, FleetRepository fleetRepository) {
        this.companyRepository = companyRepository;
        this.fleetRepository = fleetRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (!enabled || companyRepository.count() > 0) return;
        var company = companyRepository.save(new Company(
                "Transportes Andinos S.A.C.",
                "202620-111111",
                "Lima, Peru",
                "+51 999 999 999",
                "contacto@transportesandinos.pe"));
        fleetRepository.save(new Fleet(company.getId(), "Flota Lima", "Flota principal de operaciones de Lima"));
        log.info("Seeded the first company and fleet: companyId={}", company.getId());
    }
}
