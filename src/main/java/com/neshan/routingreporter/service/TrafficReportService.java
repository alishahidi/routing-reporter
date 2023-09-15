package com.neshan.routingreporter.service;

import com.neshan.routingreporter.config.ReportConfig;
import com.neshan.routingreporter.dto.TrafficReportDto;
import com.neshan.routingreporter.mapper.TrafficReportMapper;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.TrafficReportRepository;
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
public class TrafficReportService {
    RedissonClient redissonClient;
    ReportConfig reportConfig;
    TrafficReportRepository trafficReportRepository;

    public TrafficReportDto create(TrafficReportDto reportDto) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Point point = reportDto.getLocation();
        point.setSRID(3857);

        String lockKey = "traffic_report_creation_lock_" + user.getId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(40, TimeUnit.SECONDS);
            if (isLocked) {
                if (trafficReportRepository.existsTrafficReportByLocationAndExpiredAt(point)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Duplicated request.");
                }
                return TrafficReportMapper.INSTANCE.trafficReportToTrafficReportDto(
                        trafficReportRepository.save(
                                TrafficReport.builder()
                                        .expiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitTrafficTtl()))
                                        .isAccept(false)
                                        .user(user)
                                        .likeCount(0)
                                        .location(reportDto.getLocation())
                                        .build())
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

    public List<TrafficReportDto> getAll() {
        return trafficReportRepository.findAll()
                .stream()
                .map(TrafficReportMapper.INSTANCE::trafficReportToTrafficReportDto)
                .toList();
    }

    public TrafficReportDto like(Long id) {
        TrafficReport report = trafficReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() + 1);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(reportConfig.getLikeTrafficTtl()));
        return TrafficReportMapper.INSTANCE.trafficReportToTrafficReportDto(trafficReportRepository.save(report));
    }

    public TrafficReportDto disLike(Long id) {
        TrafficReport report = trafficReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() - 1);
        report.setExpiredAt(report.getExpiredAt().minusMinutes(reportConfig.getDisLikeTrafficTtl()));
        return TrafficReportMapper.INSTANCE.trafficReportToTrafficReportDto(trafficReportRepository.save(report));
    }

    public TrafficReportDto accept(Long id) {
        TrafficReport report = trafficReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        report.setIsAccept(true);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitTrafficTtl()));
        return TrafficReportMapper.INSTANCE.trafficReportToTrafficReportDto(trafficReportRepository.save(report));
    }

    public List<Integer> findTopTrafficHours(Integer limit) {
        return trafficReportRepository.findTopTrafficHours(limit);
    }
}
