package com.shahzad.inventory.dealer.repository;

import com.shahzad.inventory.dealer.entity.Dealer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface DealerRepository extends JpaRepository<Dealer, UUID> {
    Optional<Dealer> findByIdAndTenantId(UUID id, String tenantId);
    Page<Dealer> findAllByTenantId(String tenantId, Pageable pageable);
    long countByTenantIdAndSubscriptionType(String tenantId, Dealer.SubscriptionType type);
    long countBySubscriptionType(Dealer.SubscriptionType type); // for global admin

    @Query("SELECT d FROM Dealer d WHERE d.tenantId = :tenantId " +
           "AND (COALESCE(:name, '') = '' OR LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%'))) " +
           "AND (COALESCE(:email, '') = '' OR LOWER(d.email) LIKE LOWER(CONCAT('%', :email, '%')))")
    Page<Dealer> findAllByTenantIdWithFilters(@Param("tenantId") String tenantId,
                                              @Param("name") String name,
                                              @Param("email") String email,
                                              Pageable pageable);
}
