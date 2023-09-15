package com.neshan.routingreporter.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class ReportConfig {
    @Value("${report.traffic.init.ttl}")
    private Integer initTrafficTtl;
    @Value("${report.traffic.like.ttl}")
    private Integer likeTrafficTtl;
    @Value("${report.traffic.dis-like.ttl}")
    private Integer disLikeTrafficTtl;

    @Value("${report.police.init.ttl}")
    private Integer initPoliceTtl;
    @Value("${report.police.like.ttl}")
    private Integer likePoliceTtl;
    @Value("${report.police.dis-like.ttl}")
    private Integer disLikePoliceTtl;

    @Value("${report.accident.init.ttl}")
    private Integer initAccidentTtl;
    @Value("${report.accident.like.ttl}")
    private Integer likeAccidentTtl;
    @Value("${report.accident.dis-like.ttl}")
    private Integer disLikeAccidentTtl;
}
