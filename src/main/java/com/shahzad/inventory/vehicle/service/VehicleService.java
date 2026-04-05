package com.shahzad.inventory.vehicle.service;

import com.shahzad.inventory.vehicle.dto.VehicleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface VehicleService {
    VehicleDto createVehicle(VehicleDto dto, String tenantId);
    VehicleDto getVehicle(UUID id, String tenantId);
    Page<VehicleDto> listVehicles(String tenantId, String model, String status, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable);
    Page<VehicleDto> listVehiclesBySubscription(String tenantId, String subscriptionType, Pageable pageable);
    VehicleDto updateVehicle(UUID id, VehicleDto dto, String tenantId);
    void deleteVehicle(UUID id, String tenantId);
}
