package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.component.ReportFactory;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.service.ReportService;
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
