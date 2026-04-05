package com.shahzad.inventory.vehicle.controller;

import com.shahzad.inventory.common.tenant.TenantContext;
import com.shahzad.inventory.vehicle.dto.VehicleDto;
import com.shahzad.inventory.vehicle.service.VehicleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
@Tag(name = "Vehicle", description = "Vehicle management APIs")
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    @Operation(summary = "Create vehicle")
    public ResponseEntity<VehicleDto> create(@Valid @RequestBody VehicleDto dto) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(vehicleService.createVehicle(dto, tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get vehicle by id")
    public ResponseEntity<VehicleDto> get(@PathVariable UUID id) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(vehicleService.getVehicle(id, tenantId));
    }

    @GetMapping
    @Operation(summary = "List vehicles with filters")
    public ResponseEntity<Page<VehicleDto>> list(
            @RequestParam(required = false) String model,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) BigDecimal priceMin,
            @RequestParam(required = false) BigDecimal priceMax,
            @RequestParam(required = false) String subscription,
            Pageable pageable) {
        String tenantId = TenantContext.getTenantId();
        if (subscription != null) {
            return ResponseEntity.ok(vehicleService.listVehiclesBySubscription(tenantId, subscription, pageable));
        }
        return ResponseEntity.ok(vehicleService.listVehicles(tenantId, model, status, priceMin, priceMax, pageable));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update vehicle")
    public ResponseEntity<VehicleDto> update(@PathVariable UUID id, @Valid @RequestBody VehicleDto dto) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(vehicleService.updateVehicle(id, dto, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete vehicle")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        String tenantId = TenantContext.getTenantId();
        vehicleService.deleteVehicle(id, tenantId);
        return ResponseEntity.ok(Map.of("message", "Vehicle deleted successfully"));
    }
}
