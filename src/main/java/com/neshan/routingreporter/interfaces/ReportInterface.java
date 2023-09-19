package com.neshan.routingreporter.interfaces;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.request.ReportRequest;

import java.util.List;

public interface ReportInterface {
    ReportDto create(ReportRequest request);

    ReportDto like(Long id);

    ReportDto disLike(Long id);

    List<Integer> findTopHours(Integer limit);

    default ReportDto accept(Long id) {
        return null;
    }
}