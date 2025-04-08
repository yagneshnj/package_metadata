package com.example.package_metadata.repository;

import com.example.package_metadata.model.MetadataSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MetadataSourceRepository extends JpaRepository<MetadataSource, Long> {
    Optional<MetadataSource> findByName(String name);
}

