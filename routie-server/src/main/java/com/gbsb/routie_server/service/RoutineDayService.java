package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.RoutineDayDto;
import com.gbsb.routie_server.entity.Routine;
import com.gbsb.routie_server.repository.RoutineDayRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoutineDayService {

    private final RoutineDayRepository routineDayRepository;

    public RoutineDayService(RoutineDayRepository routineDayRepository) {
        this.routineDayRepository = routineDayRepository;
    }

    public List<RoutineDayDto> findRoutinesByDay(String dayOfWeek) {
        return routineDayRepository.findByDayOfWeek(dayOfWeek.toUpperCase());
    }
}
