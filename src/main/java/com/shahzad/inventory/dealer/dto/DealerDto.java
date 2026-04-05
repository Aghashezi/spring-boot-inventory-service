package com.shahzad.inventory.dealer.dto;

import com.shahzad.inventory.dealer.entity.Dealer.SubscriptionType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class DealerDto {
    private UUID id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotNull(message = "Subscription type is required")
    private SubscriptionType subscriptionType;
}
