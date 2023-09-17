package com.neshan.routingreporter.service;

import com.neshan.routingreporter.component.ReportFactory;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.ReportRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReportService {
    ReportRepository reportRepository;
    RedissonClient redissonClient;

    public List<ReportDto> getAll() {
        return reportRepository.findAll().stream()
                .map(ReportFactory::mapToMapper)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public Report getById(Long id) {
        return reportRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ReportDto create(Report report) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String lockKey = report.getType() + "_report_creation_lock_" + user.getId();
        RLock lock = redissonClient.getLock(lockKey);
        try {
            boolean isLocked = lock.tryLock(40, TimeUnit.SECONDS);
            if (isLocked) {
                if (reportRepository.existsReportByLocationAndExpiredAt(report.getLocation(), user.getId(), report.getType())) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Duplicated request.");
                }
                return ReportFactory.mapToMapper(reportRepository.save(report));
            } else {
                return null;
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    public List<ReportDto> getAllByType(ReportType reportType) {
        return reportRepository.findAllByType(reportType)
                .stream()
                .map(ReportFactory::mapToMapper)
                .collect(Collectors.toList());
    }

    public ReportDto like(Long id, Integer ttl) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() + 1);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(ttl));
        return ReportFactory.mapToMapper(reportRepository.save(report));
    }

    public ReportDto disLike(Long id, Integer ttl) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() - 1);
        report.setExpiredAt(report.getExpiredAt().minusMinutes(ttl));
        return ReportFactory.mapToMapper(reportRepository.save(report));
    }

    public ReportDto accept(Long id, Integer ttl) {
        Report report = reportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        report.setIsAccept(true);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(ttl));
        return ReportFactory.mapToMapper(reportRepository.save(report));
    }

    public List<Integer> findTopHours(Integer limit, ReportType type) {
        return reportRepository.findTopHoursReport(limit, type);
    }
}
