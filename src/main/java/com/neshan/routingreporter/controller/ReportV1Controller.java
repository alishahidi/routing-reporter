package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.component.ReportFactory;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.repository.ReportRepository;
import com.neshan.routingreporter.request.ReportRequest;
import com.neshan.routingreporter.service.ReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/report")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportV1Controller {
    ReportService reportService;
    ReportFactory reportFactory;

    @PostMapping
    public ReportDto create(@RequestBody ReportRequest request) {
        return reportFactory.makeReport(request.getType()).create(request);
    }

    @PutMapping("/like/{id}")
    public ReportDto likeReport(@PathVariable Long id) {
        return reportFactory.makeReport(reportFactory.findTypeByClass(reportService.getById(id))).like(id);
    }

    @PutMapping("/dislike/{id}")
    public ReportDto disLikeReport(@PathVariable Long id) {
        return reportFactory.makeReport(reportFactory.findTypeByClass(reportService.getById(id))).disLike(id);
    }

    @GetMapping("/top/{limit}/{type}")
    public List<Integer> findTopHours(@PathVariable Integer limit, @PathVariable ReportType type) {
        return reportFactory.makeReport(type).findTopHours(limit);
    }
}
