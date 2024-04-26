package com.territory.boundary.dto;

import jakarta.validation.constraints.NotBlank;

public record BuildStructureByWorkdayIdRequest(
        @NotBlank(message = "workdayId is mandatory")
        String workdayId
){
}

