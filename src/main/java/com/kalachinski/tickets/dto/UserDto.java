package com.kalachinski.tickets.dto;

import com.kalachinski.tickets.domains.Role;
import com.kalachinski.tickets.domains.State;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

    @Null(groups = ViewsDto.New.class)
    private Long id;

    @NotNull(groups = ViewsDto.Exist.class)
    @NotEmpty
    private List<Role> roles;

    @Null
    private LocalDateTime creationDate;

    @NotNull(groups = ViewsDto.Exist.class)
    private String login;

    @NotNull(groups = ViewsDto.Exist.class)
    private String password;

    private String confirmPassword;

    @NotNull(groups = ViewsDto.Exist.class)
    private String firstName;

    @NotNull(groups = ViewsDto.Exist.class)
    private String lastName;

    @NotNull(groups = ViewsDto.Exist.class)
    private State state;
}
