package com.harley.bank.model.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "transfer")
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Transfer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "conta_origem", nullable = false)
    private Integer originAccount;

    @Column(name = "conta_destino", nullable = false)
    private Integer destinationAccount;

    @Column(name = "valor_transferencia", nullable = false)
    private Double transferValue;

    @Column(name = "data_transferencia", nullable = false)
    private LocalDate transferDate;

    @Column(name = "data_agendamento", nullable = false)
    private LocalDate schedulingDate;

}
