package com.cs499.assignment2.repository;

import com.cs499.assignment2.domain.Background;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Background entity.
 */
@SuppressWarnings("unused")
public interface BackgroundRepository extends JpaRepository<Background,Long> {

}
