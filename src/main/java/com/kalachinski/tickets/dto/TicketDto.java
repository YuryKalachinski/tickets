package com.kalachinski.tickets.dto;

import com.kalachinski.tickets.domains.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TicketDto {

    @Null(groups = ViewsDto.New.class)
    private Long id;

    @NotNull(groups = ViewsDto.Exist.class)
    private Long locationId;

    @NotNull(groups = ViewsDto.Exist.class)
    private Long eventId;

    @NotNull(groups = ViewsDto.Exist.class)
    private Long userId;

    @NotNull(groups = ViewsDto.Exist.class)
    private Integer place;

    @NotNull(groups = ViewsDto.Exist.class)
    private Integer row;

    @Enumerated(EnumType.STRING)
    @NotNull(groups = ViewsDto.Exist.class)
    private TicketStatus ticketStatus;
}
