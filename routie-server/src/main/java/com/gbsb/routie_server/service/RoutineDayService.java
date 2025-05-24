package com.gbsb.routie_server.service;

import com.gbsb.routie_server.dto.RoutineDayDto;
import com.gbsb.routie_server.repository.RoutineDayRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineDayService {

    private final RoutineDayRepository routineDayRepository;

    public RoutineDayService(RoutineDayRepository routineDayRepository) {
        this.routineDayRepository = routineDayRepository;
    }
    public List<RoutineDayDto> findRoutinesByDay(String dayOfWeek) {
        return routineDayRepository.findByDayOfWeek(dayOfWeek.toUpperCase());
    }
    public List<RoutineDayDto> findRoutinesByUserAndDay(String userId, String dayOfWeek) {
        return routineDayRepository
                .findByUserIdAndDayOfWeek(userId, dayOfWeek.toUpperCase());
    }
}
