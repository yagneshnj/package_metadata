package com.example.package_metadata.controller;

import com.example.package_metadata.model.PackageMetadata;
import com.example.package_metadata.service.PackageMetadataService;
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
    public List<PackageMetadata> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ResponseEntity<PackageMetadata> create(@RequestBody PackageMetadata metadata) {
        PackageMetadata saved = service.save(metadata);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PackageMetadata> update(@PathVariable Long id, @RequestBody PackageMetadata updated) {
        try {
            PackageMetadata result = service.update(id, updated);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

