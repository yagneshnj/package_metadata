package com.example.package_metadata.loader;

import com.example.package_metadata.dto.PackageMetadataRequest;
import com.example.package_metadata.model.MetadataSource;
import com.example.package_metadata.service.PackageMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import com.example.package_metadata.model.*;
import com.example.package_metadata.model.Package;
import com.example.package_metadata.repository.*;


@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PackageMetadataService service;
    private final PackageManagerRepository packageManagerRepository;
    private final LicenseRepository licenseRepository;
    private final MetadataSourceRepository metadataSourceRepository;
    private final PackageRepository packageRepository;
    private final PackageMetadataRepository packageMetadataRepository;

    @Override
    public void run(String... args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/package_metadata.csv"), StandardCharsets.UTF_8))) {

            List<String> lines = reader.lines().skip(1).toList(); // Skip header

            for (String line : lines) {
                String[] fields = line.split(",");

                PackageMetadataRequest request = new PackageMetadataRequest();
                request.setPackageManager(fields[0].trim());
                request.setPackageName(fields[1].trim());
                request.setPackageVersion(fields[2].trim());
                request.setLicense(fields[3].trim());
                request.setFetchTimestamp(LocalDateTime.parse(fields[4].trim()));
                request.setSource(fields[5].trim());

                service.save(request);
            }

            System.out.println("📦 Loaded " + lines.size() + " package metadata records.");

            ensureExportDirectoryExists();
            exportPackageManagers();
            exportLicenses();
            exportMetadataSources();
            exportPackages();
            exportPackageMetadata();

        }
    }

    private void ensureExportDirectoryExists() throws IOException {
        java.nio.file.Path exportPath = java.nio.file.Paths.get("export");
        if (!java.nio.file.Files.exists(exportPath)) {
            java.nio.file.Files.createDirectories(exportPath);
        }
    }


    private void exportPackageManagers() throws IOException {
        List<PackageManager> managers = packageManagerRepository.findAll();
        try (FileWriter writer = new FileWriter("export/package_manager.csv")) {
            writer.append("id,name,created_at,updated_at\n");
            for (PackageManager pm : managers) {
                writer.append(String.format("%d,%s,%s,%s\n",
                        pm.getId(),
                        pm.getName(),
                        pm.getCreatedAt(),
                        pm.getUpdatedAt()
                ));
            }
        }
    }

    private void exportLicenses() throws IOException {
        List<License> licenses = licenseRepository.findAll();
        try (FileWriter writer = new FileWriter("export/license.csv")) {
            writer.append("id,name,created_at,updated_at\n");
            for (License lic : licenses) {
                writer.append(String.format("%d,%s,%s,%s\n",
                        lic.getId(),
                        lic.getName(),
                        lic.getCreatedAt(),
                        lic.getUpdatedAt()
                ));
            }
        }
    }

    private void exportMetadataSources() throws IOException {
        List<MetadataSource> sources = metadataSourceRepository.findAll();
        try (FileWriter writer = new FileWriter("export/metadata_source.csv")) {
            writer.append("id,name,created_at,updated_at\n");
            for (MetadataSource source : sources) {
                writer.append(String.format("%d,%s,%s,%s\n",
                        source.getId(),
                        source.getName(),
                        source.getCreatedAt(),
                        source.getUpdatedAt()
                ));
            }
        }
    }
    private void exportPackages() throws IOException {
        List<Package> packages = packageRepository.findAll();
        try (FileWriter writer = new FileWriter("export/package.csv")) {
            writer.append("id,package_manager_id,name,version,created_at,updated_at\n");
            for (Package pkg : packages) {
                writer.append(String.format("%d,%d,%s,%s,%s,%s\n",
                        pkg.getId(),
                        pkg.getPackageManager().getId(),
                        pkg.getName(),
                        pkg.getVersion(),
                        pkg.getCreatedAt(),
                        pkg.getUpdatedAt()
                ));
            }
        }
    }

    private void exportPackageMetadata() throws IOException {
        List<PackageMetadata> metadataList = packageMetadataRepository.findAll();
        try (FileWriter writer = new FileWriter("export/package_metadata.csv")) {
            writer.append("id,package_id,license_id,metadata_source_id,fetch_timestamp,created_at,updated_at\n");
            for (PackageMetadata metadata : metadataList) {
                writer.append(String.format("%d,%d,%s,%d,%s,%s,%s\n",
                        metadata.getId(),
                        metadata.getPkg().getId(),
                        metadata.getLicense() != null ? metadata.getLicense().getId() : "NULL",
                        metadata.getMetadataSource().getId(),
                        metadata.getFetchTimestamp(),
                        metadata.getCreatedAt(),
                        metadata.getUpdatedAt()
                ));
            }
        }
    }

}
