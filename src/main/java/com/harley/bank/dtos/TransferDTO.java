package com.harley.bank.dtos;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransferDTO {

    private Long id;

    @NotNull
    private Integer originAccount;

    @NotNull
    private Integer destinationAccount;

    @NotNull
    private Double transferValue;


    private String schedulingDate;
}
