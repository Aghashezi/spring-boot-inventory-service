package com.shahzad.inventory.dealer.service;

import com.shahzad.inventory.dealer.dto.DealerDto;
import com.shahzad.inventory.dealer.dto.DealerUpdateRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface DealerService {
    DealerDto createDealer(DealerDto dto, String tenantId);
    DealerDto getDealer(UUID id, String tenantId);
    Page<DealerDto> listDealers(String tenantId, String name, String email, Pageable pageable);
    DealerDto updateDealer(UUID id, DealerUpdateRequest request, String tenantId);
    void deleteDealer(UUID id, String tenantId);
}
