package com.kalachinski.tickets.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto {

    @Null(groups = ViewsDto.New.class)
    private Long id;

    @NotNull(groups = ViewsDto.Exist.class)
    private String name;

    @NotNull(groups = ViewsDto.Exist.class)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm")
    private LocalDateTime dateTime;

    @NotNull(groups = ViewsDto.Exist.class)
    private Long locationId;
}