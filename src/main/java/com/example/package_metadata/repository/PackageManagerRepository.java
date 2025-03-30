package com.example.package_metadata.repository;

import com.example.package_metadata.model.PackageManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageManagerRepository extends JpaRepository<PackageManager, Long> {
    Optional<PackageManager> findByName(String name);
}

