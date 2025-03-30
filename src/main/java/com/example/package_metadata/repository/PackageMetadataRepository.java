package com.example.package_metadata.repository;

import com.example.package_metadata.model.PackageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackageMetadataRepository extends JpaRepository<PackageMetadata, Long> {
}

