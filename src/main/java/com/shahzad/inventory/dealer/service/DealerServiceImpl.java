package com.shahzad.inventory.dealer.service;

import com.shahzad.inventory.common.exception.ForbiddenException;
import com.shahzad.inventory.dealer.dto.DealerDto;
import com.shahzad.inventory.dealer.dto.DealerUpdateRequest;
import com.shahzad.inventory.dealer.entity.Dealer;
import com.shahzad.inventory.dealer.mapper.DealerMapper;
import com.shahzad.inventory.dealer.repository.DealerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DealerServiceImpl implements DealerService {
    private final DealerRepository dealerRepository;

    @Override
    public DealerDto createDealer(DealerDto dto, String tenantId) {
        Dealer dealer = Dealer.builder()
                .tenantId(tenantId)
                .name(dto.getName())
                .email(dto.getEmail())
                .subscriptionType(dto.getSubscriptionType())
                .build();
        dealer = dealerRepository.save(dealer);
        return DealerMapper.toDto(dealer);
    }

    @Override
    public DealerDto getDealer(UUID id, String tenantId) {
        Dealer dealer = dealerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ForbiddenException("Dealer not found or access denied"));
        return DealerMapper.toDto(dealer);
    }

    @Override
    public Page<DealerDto> listDealers(String tenantId, String name, String email, Pageable pageable) {
        return dealerRepository.findAllByTenantIdWithFilters(tenantId, name, email, pageable)
                .map(DealerMapper::toDto);
    }

    @Override
    public DealerDto updateDealer(UUID id, DealerUpdateRequest request, String tenantId) {
        Dealer dealer = dealerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ForbiddenException("Dealer not found or access denied"));
        dealer.setName(request.getName());
        dealer.setEmail(request.getEmail());
        dealer = dealerRepository.save(dealer);
        return DealerMapper.toDto(dealer);
    }

    @Override
    public void deleteDealer(UUID id, String tenantId) {
        Dealer dealer = dealerRepository.findByIdAndTenantId(id, tenantId)
                .orElseThrow(() -> new ForbiddenException("Dealer not found or access denied"));
        dealerRepository.delete(dealer);
    }
}
