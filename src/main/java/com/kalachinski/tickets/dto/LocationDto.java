package com.kalachinski.tickets.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    @Null(groups = ViewsDto.New.class)
    private Long id;

    @NotNull(groups = ViewsDto.Exist.class)
    private String name;

    @NotNull(groups = ViewsDto.Exist.class)
    private Integer numberOfPlace;

    @NotNull(groups = ViewsDto.Exist.class)
    private Integer numberOfRow;
}
