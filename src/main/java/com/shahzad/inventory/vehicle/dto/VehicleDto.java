package com.shahzad.inventory.vehicle.dto;

import com.shahzad.inventory.vehicle.entity.Vehicle.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Schema(description = "Vehicle DTO")
public class VehicleDto {
    @Schema(description = "Vehicle ID")
    private UUID id;

    @NotNull
    @Schema(description = "Dealer ID")
    private UUID dealerId;

    @NotBlank
    @Schema(description = "Model name")
    private String model;

    @NotNull
    @DecimalMin(value = "0.01", message = "Price must be greater than 0")
    @Schema(description = "Vehicle price")
    private BigDecimal price;

    @NotNull
    @Schema(description = "Vehicle status")
    private Status status;
}
