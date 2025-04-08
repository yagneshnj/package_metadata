package com.example.package_metadata.dto;

import com.example.package_metadata.config.StringTrimDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PackageMetadataRequest {

    @JsonDeserialize(using = StringTrimDeserializer.class)
    @NotBlank(message = "Package manager must not be blank")
    private String packageManager;

    @JsonDeserialize(using = StringTrimDeserializer.class)
    @NotBlank(message = "Package name must not be blank")
    private String packageName;

    @JsonDeserialize(using = StringTrimDeserializer.class)
    @NotBlank(message = "Package version must not be blank")
    private String packageVersion;

    @JsonDeserialize(using = StringTrimDeserializer.class)
    @NotBlank(message = "License must not be blank")
    private String license;

    @JsonDeserialize(using = StringTrimDeserializer.class)
    @NotBlank(message = "Source must not be blank")
    private String source;

    @NotNull(message = "Fetch timestamp is required")
    private LocalDateTime fetchTimestamp;
}
