package com.gbsb.routie_server.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HealthDataDto {
    private int stepCount;
    private double calories;
}
