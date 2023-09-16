package com.neshan.routingreporter.service;

import com.neshan.routingreporter.config.ReportConfig;
import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.interfaces.ReportInterface;
import com.neshan.routingreporter.mapper.AccidentReportMapper;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.AccidentReportRepository;
import com.neshan.routingreporter.request.ReportRequest;
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
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccidentReportService implements ReportInterface {
    RedissonClient redissonClient;
    ReportConfig reportConfig;
    AccidentReportRepository accidentReportRepository;

    @Override
    public ReportDto create(ReportRequest request) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        ReportDto reportDto = request.getReport();
        Point point = reportDto.getLocation();
        point.setSRID(3857);

        String lockKey = "accident_report_creation_lock_" + user.getId();
        RLock lock = redissonClient.getLock(lockKey);

        try {
            boolean isLocked = lock.tryLock(40, TimeUnit.SECONDS);
            if (isLocked) {
                if (accidentReportRepository.existsAccidentReportByLocationAndExpiredAt(point)) {
                    throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Duplicated request.");
                }
                return AccidentReportMapper.INSTANCE.accidentReportToAccidentReportDto(
                        accidentReportRepository.save(
                                AccidentReport.builder()
                                        .expiredAt(LocalDateTime.now().plusMinutes(reportConfig.getInitAccidentTtl()))
                                        .user(user)
                                        .likeCount(0)
                                        .isAccept(true)
                                        .accidentType(request.getAccidentType())
                                        .type(ReportType.ACCIDENT)
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

    @Override
    public List<ReportDto> getAll() {
        return accidentReportRepository.findAll()
                .stream()
                .map(AccidentReportMapper.INSTANCE::accidentReportToAccidentReportDto)
                .collect(Collectors.toList());
    }

    @Override
    public ReportDto like(Long id) {
        AccidentReport report = accidentReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() + 1);
        report.setExpiredAt(LocalDateTime.now().plusMinutes(reportConfig.getLikeAccidentTtl()));
        return AccidentReportMapper.INSTANCE.accidentReportToAccidentReportDto(accidentReportRepository.save(report));
    }

    @Override
    public ReportDto disLike(Long id) {
        AccidentReport report = accidentReportRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        report.setLikeCount(report.getLikeCount() - 1);
        report.setExpiredAt(report.getExpiredAt().minusMinutes(reportConfig.getDisLikeAccidentTtl()));
        return AccidentReportMapper.INSTANCE.accidentReportToAccidentReportDto(accidentReportRepository.save(report));
    }

    @Override
    public List<Integer> findTopHours(Integer limit) {
        return accidentReportRepository.findTopAccidentHours(limit);
    }
}
