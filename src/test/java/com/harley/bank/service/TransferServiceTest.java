package com.harley.bank.service;

import com.harley.bank.model.entities.Transfer;
import com.harley.bank.model.repositories.TransferRepository;
import com.harley.bank.model.services.TransferService;
import com.harley.bank.model.services.implementations.RegraNegocioException;
import com.harley.bank.model.services.implementations.TransferServiceImp;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class TransferServiceTest {

    @MockBean
    TransferRepository transferRepository;
    TransferService transferService;

    @BeforeEach
    public void init() {
        this.transferService = new TransferServiceImp(transferRepository);
    }

    @Test
    @DisplayName("Deve salvar um transferência com sucesso.")
    public void givenATransfer_WhenCallCreateTransfer_ThenSaveAndReturnATranfer() {
        LocalDate dateNow = LocalDate.now();
        Transfer transfer = Transfer.builder().originAccount(123456).destinationAccount(654321).transferValue(100.0).schedulingDate(dateNow).build();
        Transfer expectedTransfer = Transfer.builder().id(1L).originAccount(123456).transferValue(100.0).destinationAccount(654321).transferDate(dateNow).schedulingDate(dateNow).build();
        when(transferRepository.save(any(Transfer.class))).thenReturn(expectedTransfer);

        Transfer returnedTransfer = transferService.createTransfer(transfer);

        assertThat(returnedTransfer.getId()).isNotNull();
        assertThat(returnedTransfer.getTransferDate()).isNotNull();
        assertThat(returnedTransfer.getTransferValue()).isEqualTo(100.0);

        verify(transferRepository, times(1)).save(transfer);
    }


    @Test
    @DisplayName("Deve lançar erro ao calcular taxa de transferencia.")
    public void givenInvalidSchedulingDate_WhenCallDescontaTaxa_ThenThrowAnException() {
        LocalDate schedulingDate = LocalDate.now().minusDays(1);
        Transfer transfer = Transfer.builder().schedulingDate(schedulingDate).transferValue(1000.0).build();

        Throwable error = catchThrowableOfType(() -> transferService.createTransfer(transfer), RegraNegocioException.class);
        Assertions.assertThat(error).isInstanceOf(RegraNegocioException.class).hasMessage("A data de transferência deve ser superior a data de hoje.");
        verify(transferRepository, never()).save(transfer);
    }

    @Test
    @DisplayName("Deve lançar erro ao calcular taxa de transferencia.")
    public void givenInvalidSchedulingDateOrTransferValue_WhenCallDescontaTaxa_ThenThrowAnException() {
        LocalDate schedulingDate = LocalDate.now().plusDays(100);
        Transfer transfer = Transfer.builder().schedulingDate(schedulingDate).transferValue(1000.0).build();

        Throwable error = catchThrowableOfType(() -> transferService.createTransfer(transfer), RegraNegocioException.class);
        Assertions.assertThat(error).isInstanceOf(RegraNegocioException.class).hasMessage("Não foi possível calcular uma taxa para os parâmetros passados.");
        verify(transferRepository, never()).save(transfer);
    }
}
