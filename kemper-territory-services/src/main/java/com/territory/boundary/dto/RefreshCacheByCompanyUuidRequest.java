package com.territory.boundary.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record RefreshCacheByCompanyUuidRequest (
    @NotBlank(message = "companyUuid is mandatory")
    @Pattern(regexp = "^[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}$", message = "companyUuid must be a valid UUID")
    String companyUuid
) {
}
