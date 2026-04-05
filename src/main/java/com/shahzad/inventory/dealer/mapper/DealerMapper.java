package com.shahzad.inventory.dealer.mapper;

import com.shahzad.inventory.dealer.dto.DealerDto;
import com.shahzad.inventory.dealer.entity.Dealer;

public class DealerMapper {
    public static DealerDto toDto(Dealer dealer) {
        return DealerDto.builder()
            .id(dealer.getId())
            .name(dealer.getName())
            .email(dealer.getEmail())
            .subscriptionType(dealer.getSubscriptionType())
            .build();
    }
}