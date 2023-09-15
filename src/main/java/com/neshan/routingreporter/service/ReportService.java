package com.neshan.routingreporter.service;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.mapper.AccidentReportMapper;
import com.neshan.routingreporter.mapper.PoliceReportMapper;
import com.neshan.routingreporter.mapper.TrafficReportMapper;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.PoliceReport;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    ReportRepository reportRepository;

    public List<ReportDto> getAll() {
        return reportRepository.findAll().stream()
                .map(report -> {
                    if (report instanceof TrafficReport) {
                        return TrafficReportMapper.INSTANCE.trafficReportToTrafficReportDto((TrafficReport) report);
                    } else if (report instanceof AccidentReport) {
                        return AccidentReportMapper.INSTANCE.accidentReportToAccidentReportDto((AccidentReport) report);
                    } else if (report instanceof PoliceReport) {
                        return PoliceReportMapper.INSTANCE.policeReportToPoliceReportDto((PoliceReport) report);
                    } else {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Report getById(Long id) {
        return reportRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
