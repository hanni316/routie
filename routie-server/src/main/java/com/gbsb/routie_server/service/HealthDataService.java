package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.HealthDataDto;
import com.gbsb.routie_server.entity.HealthData;
import com.gbsb.routie_server.repository.HealthDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HealthDataService {
    private final HealthDataRepository repository;

    public void save(HealthDataDto dto) {
        HealthData data = new HealthData();
        data.setStepCount(dto.getStepCount());
        data.setCalories(dto.getCalories());
        repository.save(data);
    }
}

