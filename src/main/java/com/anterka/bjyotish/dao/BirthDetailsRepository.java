package com.anterka.bjyotish.dao;

import com.anterka.bjyotish.entities.BirthDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BirthDetailsRepository extends JpaRepository<BirthDetails, Long> {
    // Additional methods specific to BirthDetails can be defined here if needed
}
