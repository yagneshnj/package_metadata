package com.example.package_metadata.repository;

import com.example.package_metadata.model.License;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LicenseRepository extends JpaRepository<License, Long> {
    Optional<License> findByName(String name);
}

