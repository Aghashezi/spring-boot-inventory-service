package com.shahzad.inventory.vehicle.repository;

import com.shahzad.inventory.vehicle.entity.Vehicle;
import com.shahzad.inventory.vehicle.entity.Vehicle.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface VehicleRepository extends JpaRepository<Vehicle, UUID> {
    Optional<Vehicle> findByIdAndTenantId(UUID id, String tenantId);
    Page<Vehicle> findAllByTenantId(String tenantId, Pageable pageable);
    Page<Vehicle> findAllByTenantIdAndModelContainingIgnoreCaseAndStatusAndPriceBetween(
        String tenantId, String model, Status status, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable);
    Page<Vehicle> findAllByTenantIdAndModelContainingIgnoreCaseAndPriceBetween(
        String tenantId, String model, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable);
    Page<Vehicle> findAllByTenantIdAndDealer_SubscriptionType(
        String tenantId, com.shahzad.inventory.dealer.entity.Dealer.SubscriptionType subscriptionType, Pageable pageable);
}
