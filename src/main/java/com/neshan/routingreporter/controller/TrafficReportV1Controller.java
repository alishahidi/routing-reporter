package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.dto.TrafficReportDto;
import com.neshan.routingreporter.service.TrafficReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report/traffic")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrafficReportV1Controller {
    TrafficReportService trafficReportService;

    @PostMapping
    public TrafficReportDto create(@RequestBody TrafficReportDto reportDto) {
        return trafficReportService.create(reportDto);
    }

    @PutMapping("/like/{id}")
    public TrafficReportDto likeReport(@PathVariable Long id) {
        return trafficReportService.like(id);
    }

    @PutMapping("/dislike/{id}")
    public TrafficReportDto disLikeReport(@PathVariable Long id) {
        return trafficReportService.disLike(id);
    }

    @GetMapping("/top/{limit}")
    public List<Integer> findTopTrafficHours(@PathVariable Integer limit){
        return trafficReportService.findTopTrafficHours(limit);
    }
}
