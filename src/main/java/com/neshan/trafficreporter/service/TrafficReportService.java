package com.neshan.trafficreporter.service;

import com.neshan.trafficreporter.config.ReportConfig;
import com.neshan.trafficreporter.dto.ReportDto;
import com.neshan.trafficreporter.enums.ReportType;
import com.neshan.trafficreporter.interfaces.ReportInterface;
import com.neshan.trafficreporter.mapper.ReportMapper;
import com.neshan.trafficreporter.model.Report;
import com.neshan.trafficreporter.model.User;
import com.neshan.trafficreporter.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Point;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TrafficReportService implements ReportInterface {
    ReportRepository reportRepository;
    RedissonClient redissonClient;
    ReportConfig reportConfig;

    @Override
    public ReportDto create(ReportDto reportDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Point point = reportDto.getLocation();
        point.setSRID(3857);

        String lockKey = "traffic_report_creation_lock_" + user.getId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(40, TimeUnit.SECONDS);
            if (isLocked) {
                if (reportRepository.existsReportByLocationAndExpiredAt(point)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Duplicated request.");
                }
                return ReportMapper.INSTANCE.reportToReportDto(reportRepository.save(
                        Report.builder()
                                .expiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitTrafficTtl()))
                                .type(reportDto.getType())
                                .isAccept(false)
                                .user(user)
                                .likeCount(0)
                                .location(reportDto.getLocation())
                                .build()
                ));
            } else {
                return null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public List<ReportDto> getAll() {
        return reportRepository.findAllByType(ReportType.TRAFFIC)
                .stream()
                .map(ReportMapper.INSTANCE::reportToReportDto)
                .toList();
    }

    @Override
    public ReportDto like(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() + 1);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(reportConfig.getLikeTrafficTtl()));
        return ReportMapper.INSTANCE.reportToReportDto(reportRepository.save(report));
    }

    @Override
    public ReportDto disLike(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() - 1);
        report.setExpiredAt(report.getExpiredAt().minusMinutes(reportConfig.getDisLikeTrafficTtl()));
        return ReportMapper.INSTANCE.reportToReportDto(reportRepository.save(report));
    }

    @Override
    public ReportDto accept(Long id) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setIsAccept(true);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitTrafficTtl()));
        return ReportMapper.INSTANCE.reportToReportDto(reportRepository.save(report));
    }
}
