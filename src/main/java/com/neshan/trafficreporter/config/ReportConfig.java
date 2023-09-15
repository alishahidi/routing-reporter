package com.neshan.trafficreporter.config;

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

}
