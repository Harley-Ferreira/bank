package com.harley.bank.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    @JsonProperty("_id")
    private Long id;

    @NotNull
    private Integer originAccount;

    @NotNull
    private Integer destinationAccount;

    @NotNull
    private Double transferValue;

    private LocalDate transferDate;

    private LocalDate schedulingDate;
}
