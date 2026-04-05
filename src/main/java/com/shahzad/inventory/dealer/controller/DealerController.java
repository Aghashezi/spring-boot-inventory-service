package com.shahzad.inventory.dealer.controller;

import com.shahzad.inventory.common.tenant.TenantContext;
import com.shahzad.inventory.dealer.dto.DealerDto;
import com.shahzad.inventory.dealer.dto.DealerUpdateRequest;
import com.shahzad.inventory.dealer.service.DealerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/dealers")
@RequiredArgsConstructor
@Tag(name = "Dealer", description = "Dealer management APIs")
public class DealerController {
    private final DealerService dealerService;

    @PostMapping
    @Operation(summary = "Create dealer")
    public ResponseEntity<DealerDto> create(@Valid @RequestBody DealerDto dto) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(dealerService.createDealer(dto, tenantId));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get dealer by id")
    public ResponseEntity<DealerDto> get(@PathVariable UUID id) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(dealerService.getDealer(id, tenantId));
    }

    @GetMapping
    @Operation(summary = "List dealers with optional filters")
    public ResponseEntity<?> list(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            Pageable pageable) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(dealerService.listDealers(tenantId, name, email, pageable));
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Update dealer")
    public ResponseEntity<DealerDto> update(@PathVariable UUID id, @Valid @RequestBody DealerUpdateRequest request) {
        String tenantId = TenantContext.getTenantId();
        return ResponseEntity.ok(dealerService.updateDealer(id, request, tenantId));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete dealer")
    public ResponseEntity<Map<String, String>> delete(@PathVariable UUID id) {
        String tenantId = TenantContext.getTenantId();
        dealerService.deleteDealer(id, tenantId);
        return ResponseEntity.ok(Map.of("message", "Dealer deleted successfully"));
    }
}
