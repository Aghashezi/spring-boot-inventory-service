package com.shahzad.inventory.vehicle.mapper;

import com.shahzad.inventory.vehicle.dto.VehicleDto;
import com.shahzad.inventory.vehicle.entity.Vehicle;

public class VehicleMapper {
    public static VehicleDto toDto(Vehicle vehicle) {
        return VehicleDto.builder()
            .id(vehicle.getId())
            .dealerId(vehicle.getDealer().getId())
            .model(vehicle.getModel())
            .price(vehicle.getPrice())
            .status(vehicle.getStatus())
            .build();
    }
}