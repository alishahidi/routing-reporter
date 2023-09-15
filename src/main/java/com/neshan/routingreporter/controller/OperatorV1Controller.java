package com.neshan.routingreporter.controller;

import com.neshan.routingreporter.component.ReportFactory;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.service.ReportService;
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
        return reportFactory.makeReport(ReportType.valueOf(type.toUpperCase())).getAll();
    }

    @PutMapping("/report/accept/{id}")
    public ReportDto acceptReport(@PathVariable Long id) {
        return reportFactory.makeReport(reportService.getTypeById(id)).accept(id);
    }
}