package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.dto.AccidentReportDto;
import com.neshan.routingreporter.dto.PoliceReportDto;
import com.neshan.routingreporter.service.AccidentReportService;
import com.neshan.routingreporter.service.PoliceReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report/police")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PoliceReportV1Controller {
    PoliceReportService policeReportService;

    @PostMapping
    public PoliceReportDto create(@RequestBody PoliceReportDto reportDto) {
        return policeReportService.create(reportDto);
    }

    @PutMapping("/like/{id}")
    public PoliceReportDto likeReport(@PathVariable Long id) {
        return policeReportService.like(id);
    }

    @PutMapping("/dislike/{id}")
    public PoliceReportDto disLikeReport(@PathVariable Long id) {
        return policeReportService.disLike(id);
    }

    @GetMapping("/top/{limit}")
    public List<Integer> findTopPoliceHours(@PathVariable Integer limit) {
        return policeReportService.findTopPoliceHours(limit);
    }
}
