package com.example.package_metadata.service;

import com.example.package_metadata.dto.PackageMetadataRequest;
import com.example.package_metadata.model.*;
import com.example.package_metadata.model.Package;
import com.example.package_metadata.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PackageMetadataService {

    private final PackageMetadataRepository metadataRepo;
    private final PackageRepository packageRepo;
    private final PackageManagerRepository packageManagerRepo;
    private final LicenseRepository licenseRepo;
    private final MetadataSourceRepository metadataSourceRepo;

    public PackageMetadata save(PackageMetadataRequest request) {
        // Normalize to lowercase
        String normalizedPackageManager = request.getPackageManager().toLowerCase();
        String normalizedPackageName = request.getPackageName().toLowerCase();
        String normalizedPackageVersion = request.getPackageVersion().toLowerCase(); // Version may be case-sensitive, you decide

        PackageManager packageManager = packageManagerRepo.findByName(normalizedPackageManager)
                .orElseGet(() -> packageManagerRepo.save(PackageManager.builder().name(normalizedPackageManager).build()));

        Package pkg = packageRepo.findByNameAndVersionAndPackageManager(
                        normalizedPackageName,
                        normalizedPackageVersion,
                        packageManager)
                .orElseGet(() -> packageRepo.save(
                        Package.builder()
                                .name(normalizedPackageName)
                                .version(normalizedPackageVersion)
                                .packageManager(packageManager)
                                .build()));

        License license = licenseRepo.findByName(request.getLicense())
                .orElseGet(() -> licenseRepo.save(License.builder().name(request.getLicense()).build()));

        MetadataSource metadataSource = metadataSourceRepo.findByName(request.getSource())
                .orElseGet(() -> metadataSourceRepo.save(MetadataSource.builder().name(request.getSource()).build()));

        Optional<PackageMetadata> existingMetadataOpt = metadataRepo.findByPkgAndMetadataSource(pkg, metadataSource);

        PackageMetadata metadata = existingMetadataOpt.map(existing -> {
            existing.setFetchTimestamp(request.getFetchTimestamp());
            existing.setLicense(license);
            return existing;
        }).orElseGet(() -> PackageMetadata.builder()
                .pkg(pkg)
                .license(license)
                .metadataSource(metadataSource)
                .fetchTimestamp(request.getFetchTimestamp())
                .build());

        return metadataRepo.save(metadata);
    }


    public List<PackageMetadata> getByPackage(String packageManagerName, String packageName, String packageVersion) {
        String normalizedPackageManager = packageManagerName.toLowerCase();
        String normalizedPackageName = packageName.toLowerCase();
        String normalizedPackageVersion = packageVersion.toLowerCase(); // Optional: normalize if needed

        PackageManager packageManager = packageManagerRepo.findByName(normalizedPackageManager)
                .orElseThrow(() -> new EntityNotFoundException("PackageManager not found: " + packageManagerName));

        Package pkg = packageRepo.findByNameAndVersionAndPackageManager(normalizedPackageName, normalizedPackageVersion, packageManager)
                .orElseThrow(() -> new EntityNotFoundException("Package not found: " + packageName + " version: " + packageVersion));

        return metadataRepo.findByPkg(pkg);
    }


    public PackageMetadata update(Long id, PackageMetadataRequest request) {
        return metadataRepo.findById(id).map(existing -> {
            PackageManager packageManager = packageManagerRepo.findByName(request.getPackageManager())
                    .orElseGet(() -> packageManagerRepo.save(PackageManager.builder().name(request.getPackageManager()).build()));

            Package pkg = packageRepo.findByNameAndVersionAndPackageManager(
                            request.getPackageName(),
                            request.getPackageVersion(),
                            packageManager)
                    .orElseGet(() -> packageRepo.save(
                            Package.builder()
                                    .name(request.getPackageName())
                                    .version(request.getPackageVersion())
                                    .packageManager(packageManager)
                                    .build()));

            License license = licenseRepo.findByName(request.getLicense())
                    .orElseGet(() -> licenseRepo.save(License.builder().name(request.getLicense()).build()));

            MetadataSource metadataSource = metadataSourceRepo.findByName(request.getSource())
                    .orElseGet(() -> metadataSourceRepo.save(MetadataSource.builder().name(request.getSource()).build()));

            existing.setPkg(pkg);
            existing.setLicense(license);
            existing.setMetadataSource(metadataSource);
            existing.setFetchTimestamp(request.getFetchTimestamp());

            return metadataRepo.save(existing);
        }).orElseThrow(() -> new EntityNotFoundException("Package metadata not found with id: " + id));
    }
}

