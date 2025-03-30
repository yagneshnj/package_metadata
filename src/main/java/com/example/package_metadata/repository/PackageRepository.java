package com.example.package_metadata.repository;

import com.example.package_metadata.model.Package;
import com.example.package_metadata.model.PackageManager;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PackageRepository extends JpaRepository<Package, Long> {
    Optional<Package> findByNameAndVersionAndPackageManager(String name, String version, PackageManager packageManager);
}

