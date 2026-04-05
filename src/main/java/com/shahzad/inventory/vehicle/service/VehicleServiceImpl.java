package com.shahzad.inventory.vehicle.service;

import com.shahzad.inventory.common.exception.ForbiddenException;
import com.shahzad.inventory.dealer.entity.Dealer;
import com.shahzad.inventory.dealer.repository.DealerRepository;
import com.shahzad.inventory.vehicle.dto.VehicleDto;
import com.shahzad.inventory.vehicle.entity.Vehicle;
import com.shahzad.inventory.vehicle.entity.Vehicle.Status;
import com.shahzad.inventory.vehicle.mapper.VehicleMapper;
import com.shahzad.inventory.vehicle.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final DealerRepository dealerRepository;

    @Override
    @Transactional
    public VehicleDto createVehicle(VehicleDto dto, String tenantId) {
        Dealer dealer = dealerRepository.findById(dto.getDealerId())
                .filter(d -> d.getTenantId().equals(tenantId))
                .orElseThrow(() -> new ForbiddenException("Dealer not found or access denied"));
        Vehicle vehicle = Vehicle.builder()
                .tenantId(tenantId)
                .dealer(dealer)
                .model(dto.getModel())
                .price(dto.getPrice())
                .status(dto.getStatus())
                .build();
        vehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle created: {}", vehicle.getId());
        return VehicleMapper.toDto(vehicle);
    }

    @Override
    public VehicleDto getVehicle(UUID id, String tenantId) {
        Vehicle vehicle = vehicleRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ForbiddenException("Vehicle not found or access denied"));
        return VehicleMapper.toDto(vehicle);
    }

    @Override
    public Page<VehicleDto> listVehicles(String tenantId, String model, String status, BigDecimal priceMin, BigDecimal priceMax, Pageable pageable) {
        Status statusEnum = null;
        if (status != null) {
            try {
                statusEnum = Status.valueOf(status);
            } catch (IllegalArgumentException e) {
                throw new ForbiddenException("Invalid status value");
            }
        }
        if (priceMin == null) priceMin = BigDecimal.ZERO;
        if (priceMax == null) priceMax = new BigDecimal("999999999");
        if (model == null) model = "";
        if (statusEnum != null) {
            return vehicleRepository.findAllByTenantIdAndModelContainingIgnoreCaseAndStatusAndPriceBetween(
                    tenantId, model, statusEnum, priceMin, priceMax, pageable).map(VehicleMapper::toDto);
        } else {
            return vehicleRepository.findAllByTenantIdAndModelContainingIgnoreCaseAndPriceBetween(
                    tenantId, model, priceMin, priceMax, pageable).map(VehicleMapper::toDto);
        }
    }

    @Override
    public Page<VehicleDto> listVehiclesBySubscription(String tenantId, String subscriptionType, Pageable pageable) {
        Dealer.SubscriptionType subType;
        try {
            subType = Dealer.SubscriptionType.valueOf(subscriptionType);
        } catch (IllegalArgumentException e) {
            throw new ForbiddenException("Invalid subscription type");
        }
        return vehicleRepository.findAllByTenantIdAndDealer_SubscriptionType(tenantId, subType, pageable)
                .map(VehicleMapper::toDto);
    }

    @Override
    @Transactional
    public VehicleDto updateVehicle(UUID id, VehicleDto dto, String tenantId) {
        Vehicle vehicle = vehicleRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ForbiddenException("Vehicle not found or access denied"));
        if (dto.getModel() != null) vehicle.setModel(dto.getModel());
        if (dto.getPrice() != null) vehicle.setPrice(dto.getPrice());
        if (dto.getStatus() != null) vehicle.setStatus(dto.getStatus());
        vehicle = vehicleRepository.save(vehicle);
        log.info("Vehicle updated: {}", vehicle.getId());
        return VehicleMapper.toDto(vehicle);
    }

    @Override
    @Transactional
    public void deleteVehicle(UUID id, String tenantId) {
        Vehicle vehicle = vehicleRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ForbiddenException("Vehicle not found or access denied"));
        vehicleRepository.delete(vehicle);
        log.info("Vehicle deleted: {}", vehicle.getId());
    }
}
