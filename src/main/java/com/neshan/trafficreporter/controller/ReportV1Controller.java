package com.neshan.trafficreporter.controller;

import com.neshan.trafficreporter.component.ReportFactory;
import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportV1Controller {
    ReportService reportService;
    ReportFactory reportFactory;

    @PostMapping
    public ReportDto reportTraffic(@RequestBody ReportDto reportDto) {
        return reportService.createReport(reportDto);
    }

    @PutMapping("/like/{id}")
    public ReportDto likeReport(@PathVariable Long id){
        return reportFactory.makeReport(reportService.getTypeById(id)).like(id);
    }

    @PutMapping("/dislike/{id}")
    public ReportDto disLikeReport(@PathVariable Long id){
        return reportFactory.makeReport(reportService.getTypeById(id)).disLike(id);
    }
}
