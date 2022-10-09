package com.harley.bank.repositories;

import com.harley.bank.model.entities.Transfer;
import com.harley.bank.model.repositories.TransferRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
public class TransferRepositoryTest {

    @Autowired
    TestEntityManager entityManager;

    @Autowired
    TransferRepository transferRepository;

    @Test
    @DisplayName("Deve salvar uma transferencia com sucesso.")
    public void givenAValidTransfer_WhenCallSave_ThenReturnSavedTransfer() {
        LocalDate dateNow = LocalDate.now();
        Transfer transfer = Transfer.builder().originAccount(123456).destinationAccount(654321).transferDate(dateNow).build();

        Transfer returnedTransfer  = transferRepository.save(transfer);

        Assertions.assertThat(returnedTransfer.getId()).isNotNull();
    }
}
