package com.cs499.assignment2.repository;

import com.cs499.assignment2.domain.Statistics;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Statistics entity.
 */
@SuppressWarnings("unused")
public interface StatisticsRepository extends JpaRepository<Statistics,Long> {

}
