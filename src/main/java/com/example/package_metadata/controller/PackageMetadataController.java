package com.example.package_metadata.controller;

import com.example.package_metadata.dto.PackageMetadataRequest;
import com.example.package_metadata.model.PackageMetadata;
import com.example.package_metadata.service.PackageMetadataService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@RequiredArgsConstructor
public class PackageMetadataController {

    private final PackageMetadataService service;

    @GetMapping
    public ResponseEntity<List<PackageMetadata>> getByPackage(
            @RequestParam String packageManager,
            @RequestParam String packageName,
            @RequestParam String packageVersion) {

        List<PackageMetadata> result = service.getByPackage(packageManager, packageName, packageVersion);
        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<PackageMetadata> create(@Valid @RequestBody PackageMetadataRequest request) {
        PackageMetadata saved = service.save(request);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageMetadata> update(@PathVariable Long id, @Valid @RequestBody PackageMetadataRequest request) {
        PackageMetadata updated = service.update(id, request);
        return ResponseEntity.ok(updated);
    }
}
