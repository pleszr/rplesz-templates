package com.territory.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Staff extends AbstractTerritory {
    private UUID uuid;
    private String code;
    private String type;
    @JsonIgnore
    private UUID currentEmployeeUuid;
    private List<Agency> agencies;

    private String employeeID;
    private String writingNumber;
    private String firstName;
    private String middleInitial;
    private String lastName;
    private String email;

    public Staff createFromTerritory(Territory territory) {
        return Staff.builder()
                .uuid(territory.getUuid())
                .code(territory.getCode())
                .type(territory.getType())
                .type(territory.getType())
                .currentEmployeeUuid(!territory.getCurrentEmployeeUuid().isEmpty() ? UUID.fromString(territory.getCurrentEmployeeUuid()):null)
                .employeeID(territory.getEmployeeID())
                .firstName(territory.getFirstName())
                .middleInitial(territory.getMiddleInitial())
                .lastName(territory.getLastName())
                .email(territory.getEmail())
                .build();
    }
}