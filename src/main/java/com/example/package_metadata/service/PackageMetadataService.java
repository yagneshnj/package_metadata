package com.example.package_metadata.service;

import com.example.package_metadata.model.PackageMetadata;
import com.example.package_metadata.repository.PackageMetadataRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageMetadataService {

    private final PackageMetadataRepository metadataRepo;

    public List<PackageMetadata> getAll() {
        return metadataRepo.findAll();
    }

    public PackageMetadata save(PackageMetadata metadata) {
        return metadataRepo.save(metadata);
    }

    public PackageMetadata update(Long id, PackageMetadata updated) {
        return metadataRepo.findById(id).map(existing -> {
            existing.setFetchTimestamp(updated.getFetchTimestamp());
            existing.setLicense(updated.getLicense());
            existing.setSource(updated.getSource());
            return metadataRepo.save(existing);
        }).orElseThrow(() -> new EntityNotFoundException("Metadata not found with id: " + id));
    }
}

