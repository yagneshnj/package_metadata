package com.example.package_metadata.loader;

import com.example.package_metadata.model.*;
import com.example.package_metadata.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PackageManagerRepository packageManagerRepo;
    private final LicenseRepository licenseRepo;
    private final PackageRepository packageRepo;
    private final PackageMetadataRepository metadataRepo;

    @Override
    public void run(String... args) throws Exception {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                getClass().getResourceAsStream("/package_metadata.csv"), StandardCharsets.UTF_8))) {

            List<String> lines = reader.lines().skip(1).toList(); // skip header

            for (String line : lines) {
                String[] fields = line.split(",");

                String managerName = fields[0].trim();
                String pkgName = fields[1].trim();
                String version = fields[2].trim();
                String licenseName = fields[3].trim();
                LocalDateTime timestamp = LocalDateTime.parse(fields[4].trim());
                String source = fields[5].trim();

                PackageManager pm = packageManagerRepo.findByName(managerName)
                        .orElseGet(() -> packageManagerRepo.save(
                                PackageManager.builder().name(managerName).build()));

                License license = licenseRepo.findByName(licenseName)
                        .orElseGet(() -> licenseRepo.save(
                                License.builder().name(licenseName).build()));

                com.example.package_metadata.model.Package pkg = packageRepo.findByNameAndVersionAndPackageManager(pkgName, version, pm)
                        .orElseGet(() -> packageRepo.save(
                                com.example.package_metadata.model.Package.builder()
                                        .name(pkgName)
                                        .version(version)
                                        .packageManager(pm)
                                        .build()));

                PackageMetadata meta = PackageMetadata.builder()
                        .pkg(pkg)
                        .license(license)
                        .fetchTimestamp(timestamp)
                        .source(source)
                        .build();

                metadataRepo.save(meta);
            }

            System.out.println("📦 Loaded " + lines.size() + " package metadata rows.");
        }
    }
}

