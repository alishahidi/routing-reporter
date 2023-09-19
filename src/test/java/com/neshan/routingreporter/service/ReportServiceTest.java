package com.neshan.routingreporter.service;

import com.neshan.routingreporter.dto.ReportDto;
import com.neshan.routingreporter.dto.TrafficReportDto;
import com.neshan.routingreporter.enums.AccidentType;
import com.neshan.routingreporter.enums.ReportType;
import com.neshan.routingreporter.enums.Role;
import com.neshan.routingreporter.enums.TrafficType;
import com.neshan.routingreporter.model.AccidentReport;
import com.neshan.routingreporter.model.Report;
import com.neshan.routingreporter.model.TrafficReport;
import com.neshan.routingreporter.model.User;
import com.neshan.routingreporter.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class ReportServiceTest {
    @Mock
    private ReportRepository reportRepository;

    @InjectMocks
    private ReportService reportService;

    private final GeometryFactory geometryFactory = new GeometryFactory();

    private final List<Report> reports = List.of(
            AccidentReport.builder()
                    .id(1L)
                    .type(ReportType.ACCIDENT)
                    .accidentType(AccidentType.HEAVY)
                    .location(geometryFactory.createPoint(new Coordinate(35.7004, 51.3376)))
                    .build(),
            TrafficReport.builder()
                    .id(2L)
                    .type(ReportType.TRAFFIC)
                    .trafficType(TrafficType.SOFT)
                    .location(geometryFactory.createPoint(new Coordinate(36.7004, 51.3376)))
                    .build()
    );

    @Mock
    private Authentication authentication;

    private final User user = User.builder()
            .id(1L)
            .name("Ali Shahidi")
            .role(Role.ADMIN)
            .password("testpass")
            .username("alishahidi")
            .build();

    @Mock
    private RLock lock;

    @Mock
    private RedissonClient redissonClient;

    @Test
    void getAllReportTest() {
        Mockito.when(reportRepository.findAllByAccept(false)).thenReturn(reports);

        List<ReportDto> result = reportService.getAll(false);
        assertThat(result).hasSize(2);
        assertThat(result.get(0).getId()).isEqualTo(1L);
        assertThat(result.get(0).getType()).isEqualTo(ReportType.ACCIDENT);
        assertThat(result.get(0)).isInstanceOf(ReportDto.class);
    }

    @Test
    void getByIdTest() {
        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(reports.get(0)));

        Report report = reportService.getById(1L);
        assertThat(report.getId()).isEqualTo(1);
        assertThat(report.getType()).isEqualTo(ReportType.ACCIDENT);
    }

    @Test
    void getByIdExceptionTest() {
        Mockito.when(reportRepository.findById(3L)).thenReturn(Optional.ofNullable(null));

        assertThatThrownBy(() -> reportService.getById(3L))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createReportSuccessTest() throws InterruptedException {
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(redissonClient.getLock(ArgumentMatchers.anyString())).thenReturn(lock);
        Mockito.when(lock.tryLock(40, TimeUnit.SECONDS)).thenReturn(true);

        Report savedReport = reports.get(1);
        Mockito.when(reportRepository.save(ArgumentMatchers.any(Report.class))).thenReturn(savedReport);

        ReportDto result = reportService.create(reports.get(1));

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2);
    }

    @Test
    void createReportDuplicatedTest() throws InterruptedException {
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(redissonClient.getLock(ArgumentMatchers.anyString())).thenReturn(lock);
        Mockito.when(lock.tryLock(40, TimeUnit.SECONDS)).thenReturn(true);

        Mockito.when(reportRepository.existsReportByLocationAndExpiredAt(ArgumentMatchers.any(), ArgumentMatchers.anyLong(), ArgumentMatchers.any()))
                .thenReturn(true);

        assertThatThrownBy(() -> reportService.create(reports.get(0)))
                .isInstanceOf(ResponseStatusException.class);
    }

    @Test
    void createReportLockNotAcquiredTest() throws InterruptedException {
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(redissonClient.getLock(ArgumentMatchers.anyString())).thenReturn(lock);
        Mockito.when(lock.tryLock(40, TimeUnit.SECONDS)).thenReturn(false);

        assertThat(reportService.create(reports.get(1))).isNull();
    }

    @Test
    void getAllByTypeTest() {
        Mockito.when(reportRepository.findAllAcceptByType(ReportType.TRAFFIC, false)).thenReturn(List.of(reports.get(1)));

        List<ReportDto> reports = reportService.getAllByType(ReportType.TRAFFIC, false);
        TrafficReportDto trafficReportDto = (TrafficReportDto) reports.get(0);

        assertThat(trafficReportDto.getTrafficType()).isEqualTo(TrafficType.SOFT);
    }

    @Test
    void likeReportTest() throws InterruptedException {
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Report report = reports.get(0);
        report.setLikeCount(0);

        Mockito.when(redissonClient.getLock(ArgumentMatchers.anyString())).thenReturn(lock);
        Mockito.when(lock.tryLock(40, TimeUnit.SECONDS)).thenReturn(true);

        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        Mockito.when(reportRepository.save(ArgumentMatchers.any(Report.class))).thenReturn(report);
        ReportDto result = reportService.like(ReportType.ACCIDENT, 1L, 30);

        assertThat(result).isNotNull();
        assertThat(result.getLikeCount()).isEqualTo(1);
    }

    @Test
    void disLikeReportTest() throws InterruptedException {
        Mockito.when(authentication.getPrincipal()).thenReturn(user);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Report report = reports.get(0);
        report.setLikeCount(0);
        report.setExpiredAt(LocalDateTime.now());

        Mockito.when(redissonClient.getLock(ArgumentMatchers.anyString())).thenReturn(lock);
        Mockito.when(lock.tryLock(40, TimeUnit.SECONDS)).thenReturn(true);

        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        Mockito.when(reportRepository.save(ArgumentMatchers.any(Report.class))).thenReturn(report);
        ReportDto result = reportService.disLike(ReportType.ACCIDENT, 1L, 30);

        assertThat(result).isNotNull();
        assertThat(result.getLikeCount()).isEqualTo(-1);
    }

    @Test
    void acceptReportTest() {
        Report report = reports.get(0);
        report.setIsAccept(true);
        report.setExpiredAt(LocalDateTime.now());

        Mockito.when(reportRepository.findById(1L)).thenReturn(Optional.of(report));
        Mockito.when(reportRepository.save(ArgumentMatchers.any(Report.class))).thenReturn(report);
        ReportDto result = reportService.accept(1L, 30);

        assertThat(result).isNotNull();
        assertThat(result.getIsAccept()).isEqualTo(true);
    }
}