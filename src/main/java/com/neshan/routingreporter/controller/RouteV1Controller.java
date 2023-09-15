package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.dto.RouteDto;
import com.neshan.routingreporter.service.ReportService;
import com.neshan.routingreporter.service.RouteService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/route")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RouteV1Controller {
    RouteService routeService;
    ReportService reportService;

    @GetMapping("/reports")
    @Transactional
    public List<ReportDto> getReports(@RequestBody RouteDto routeDto) {
        routeService.createRoute(routeDto);
        return reportService.getAllReportAroundRoute(routeDto);
    }

    @GetMapping
    public List<RouteDto> getAll(){
        return routeService.getAll();
    }
}