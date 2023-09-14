package com.neshan.trafficreporter.component;

import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import com.neshan.trafficreporter.mapper.ReportMapper;
import com.neshan.trafficreporter.model.Report;
import com.neshan.trafficreporter.model.User;
import com.neshan.trafficreporter.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrafficReport implements ReportInterface {
    ReportRepository reportRepository;

    @Override
    public ReportDto createReport(ReportDto reportDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        return ReportMapper.INSTANCE.reportToReportDto(reportRepository.save(
                Report.builder()
                        .ttl(60 * 2.0)
                        .type(reportDto.getType())
                        .isAccept(false)
                        .user(user)
                        .likeCount(0)
                        .location(reportDto.getLocation())
                        .build()
        ));
    }
}
