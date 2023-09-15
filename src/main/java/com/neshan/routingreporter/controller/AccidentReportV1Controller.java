package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.dto.AccidentReportDto;
import com.neshan.routingreporter.service.AccidentReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report/accident")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccidentReportV1Controller {
    AccidentReportService accidentReportService;

    @PostMapping
    public AccidentReportDto create(@RequestBody AccidentReportDto reportDto) {
        return accidentReportService.create(reportDto);
    }

    @PutMapping("/like/{id}")
    public AccidentReportDto likeReport(@PathVariable Long id) {
        return accidentReportService.like(id);
    }

    @PutMapping("/dislike/{id}")
    public AccidentReportDto disLikeReport(@PathVariable Long id) {
        return accidentReportService.disLike(id);
    }

    @GetMapping("/top/{limit}")
    public List<Integer> findTopTrafficHours(@PathVariable Integer limit) {
        return accidentReportService.findTopAccidentHours(limit);
    }
}
