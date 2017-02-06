package com.cs499.assignment2.repository;

import com.cs499.assignment2.domain.Sport;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Sport entity.
 */
@SuppressWarnings("unused")
public interface SportRepository extends JpaRepository<Sport,Long> {

}
