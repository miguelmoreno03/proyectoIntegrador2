package com.dh.userservice.entities;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Response to my user request with relevant properties")
public class AppUserResponseDTO {
    private Long id;
    @NotNull
    @Size(min = 1, max = 250)
    private String first_name ;
    @NotNull
    @Size(min = 1, max = 250)
    private String last_name ;
    @NotNull
    @Size(min = 1, max = 250)
    private String dni ;
    @NotNull
    @Size(min = 1, max = 250)
    private String email ;
    @NotNull
    @Size(min = 1, max = 250)
    private String phone ;
}
