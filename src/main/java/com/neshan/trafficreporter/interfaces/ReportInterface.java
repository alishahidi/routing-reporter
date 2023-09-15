package com.neshan.trafficreporter.interfaces;

import com.neshan.trafficreporter.dto.ReportDto;

import java.util.List;

public interface ReportInterface {
    ReportDto create(ReportDto reportDto);

    List<ReportDto> getAll();

    ReportDto like(Long id);

    ReportDto disLike(Long id);

    default ReportDto accept(Long id) {
        return null;
    }
}
