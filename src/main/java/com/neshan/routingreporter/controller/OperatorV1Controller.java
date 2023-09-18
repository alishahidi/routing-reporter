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
    ReportFactory reportFactory;
    ReportService reportService;

    @GetMapping("/report/get")
    public List<ReportDto> getAll(@RequestParam(name = "type", required = false) String type, @RequestParam(name = "accept", required = false) Boolean accept) {
        Boolean isAccept = accept != null && accept;
        if (type == null) {
            return reportService.getAll(isAccept);
        }
        return reportFactory.makeReport(ReportType.valueOf(type.toUpperCase())).getAll(isAccept);
    }

    @PutMapping("/report/accept/{id}")
    public ReportDto accept(@PathVariable Long id) {
        return reportFactory.makeReport(reportService.getById(id).getType()).accept(id);
    }
}
