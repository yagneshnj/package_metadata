package com.example.package_metadata.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class PackageMetadata extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "package_id", nullable = false)
    private Package pkg;

    @ManyToOne
    @JoinColumn(name = "license_id", nullable = false)
    private License license;

    @ManyToOne
    @JoinColumn(name = "metadata_source_id", nullable = false)
    private MetadataSource metadataSource;

    @Column(name = "fetch_timestamp", nullable = false)
    private LocalDateTime fetchTimestamp;
}
