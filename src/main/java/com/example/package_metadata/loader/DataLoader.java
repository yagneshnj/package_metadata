package com.example.package_metadata.loader;

import com.example.package_metadata.dto.PackageMetadataRequest;
import com.example.package_metadata.service.PackageMetadataService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final PackageMetadataService service;

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
        }
    }
}
