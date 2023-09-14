package com.neshan.trafficreporter.repository;

import com.neshan.trafficreporter.model.Route;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
}
