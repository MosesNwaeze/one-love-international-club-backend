package com.api.vdtcommsws.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public abstract class BaseDto {

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;
    private UUID createdById;
    private UUID updatedById;
    private UUID deletedById;
    private UUID restoredById;

}
