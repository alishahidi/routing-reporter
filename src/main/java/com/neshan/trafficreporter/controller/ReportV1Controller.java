package com.neshan.trafficreporter.controller;

import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportV1Controller {
    ReportService reportService;

    @PostMapping
    public ReportDto reportTraffic(@RequestBody ReportDto reportDto) {
        return reportService.createReport(reportDto);
    }
}
