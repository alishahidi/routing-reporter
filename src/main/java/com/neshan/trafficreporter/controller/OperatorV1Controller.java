package com.neshan.trafficreporter.controller;

import com.neshan.trafficreporter.component.ReportFactory;
import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.enums.ReportType;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import com.neshan.trafficreporter.service.ReportService;
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
    ReportService reportService;
    ReportFactory reportFactory;

    @GetMapping("/report/get")
    public List<ReportDto> getAllReport(@RequestParam(name = "type", required = false) String type) {
        if (type == null) {
            return reportService.getAllReport();
        }
        return reportFactory.makeReport(ReportType.valueOf(type.toUpperCase())).getAllReport();
    }

    @PutMapping("/report/accept/{id}")
    public ReportDto acceptReport(@RequestBody ReportDto reportDto, @PathVariable Long id) {
        return reportFactory.makeReport(reportDto.getType()).accept(id);
    }
}
