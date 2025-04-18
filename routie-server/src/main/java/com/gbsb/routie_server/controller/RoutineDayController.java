package com.gbsb.routie_server.controller;

import com.gbsb.routie_server.dto.RoutineDayDto;
import com.gbsb.routie_server.service.RoutineDayService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/routines")
public class RoutineDayController {

    private final RoutineDayService routineDayService;

    public RoutineDayController(RoutineDayService routineDayService) {
        this.routineDayService = routineDayService;

    }

    @GetMapping
    public List<RoutineDayDto> getRoutineDays(@RequestParam String dayOfWeek) {
        return routineDayService.findRoutinesByDay(dayOfWeek);
    }
}
