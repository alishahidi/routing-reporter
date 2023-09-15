package com.neshan.routingreporter.service;

import com.neshan.routingreporter.config.ReportConfig;
import com.neshan.routingreporter.dto.AccidentReportDto;
import com.neshan.routingreporter.dto.PoliceReportDto;
import com.neshan.routingreporter.mapper.AccidentReportMapper;
import com.neshan.routingreporter.mapper.PoliceReportMapper;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.PoliceReport;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.AccidentReportRepository;
import com.neshan.routingreporter.repository.PoliceReportRepository;
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
public class PoliceReportService {
    RedissonClient redissonClient;
    ReportConfig reportConfig;
    PoliceReportRepository policeReportRepository;

    public PoliceReportDto create(PoliceReportDto reportDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Point point = reportDto.getLocation();
        point.setSRID(3857);

        String lockKey = "police_report_creation_lock_" + user.getId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(40, TimeUnit.SECONDS);
            if (isLocked) {
                if (policeReportRepository.existsPoliceReportByLocationAndExpiredAt(point)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Duplicated request.");
                }
                return PoliceReportMapper.INSTANCE.policeReportToPoliceReportDto(
                        policeReportRepository.save(
                                PoliceReport.builder()
                                        .expiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitPoliceTtl()))
                                        .user(user)
                                        .likeCount(0)
                                        .location(reportDto.getLocation())
                                        .build()
                        )
                );
            } else {
                return null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public List<PoliceReportDto> getAll() {
        return policeReportRepository.findAll()
                .stream()
                .map(PoliceReportMapper.INSTANCE::policeReportToPoliceReportDto)
                .toList();
    }

    public PoliceReportDto like(Long id) {
        PoliceReport report = policeReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() + 1);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(reportConfig.getLikePoliceTtl()));
        return PoliceReportMapper.INSTANCE.policeReportToPoliceReportDto(policeReportRepository.save(report));
    }

    public PoliceReportDto disLike(Long id) {
        PoliceReport report = policeReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() - 1);
        report.setExpiredAt(report.getExpiredAt().minusMinutes(reportConfig.getDisLikePoliceTtl()));
        return PoliceReportMapper.INSTANCE.policeReportToPoliceReportDto(policeReportRepository.save(report));
    }

    public List<Integer> findTopPoliceHours(Integer limit) {
        return policeReportRepository.findTopPoliceHours(limit);
    }
}
