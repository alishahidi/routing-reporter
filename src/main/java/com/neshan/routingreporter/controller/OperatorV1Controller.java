package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.dto.TrafficReportDto;
import com.neshan.routingreporter.service.TrafficReportService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operator")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OperatorV1Controller {
    TrafficReportService trafficReportService;

    @GetMapping("/report/traffic/get")
    public List<TrafficReportDto> getAll(@RequestParam(name = "type", required = false) String type) {
        return trafficReportService.getAll();
    }

    @PutMapping("/report/traffic/accept/{id}")
    public TrafficReportDto accept(@PathVariable Long id) {
        return trafficReportService.accept(id);
    }
}
