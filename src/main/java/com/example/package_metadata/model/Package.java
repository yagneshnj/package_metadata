package com.example.package_metadata.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "package")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = false)
public class Package extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String version;

    @ManyToOne
    @JoinColumn(name = "package_manager_id", nullable = false)
    private PackageManager packageManager;
}

