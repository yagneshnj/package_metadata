package com.example.package_metadata.repository;

import com.example.package_metadata.model.Package;
import com.example.package_metadata.model.PackageMetadata;
import com.example.package_metadata.model.MetadataSource;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PackageMetadataRepository extends JpaRepository<PackageMetadata, Long> {
    List<PackageMetadata> findByPkg(Package pkg);

    Optional<PackageMetadata> findByPkgAndMetadataSource(Package pkg, MetadataSource metadataSource);
}



