package com.harley.bank.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harley.bank.dtos.TransferDTO;
import com.harley.bank.model.entities.Transfer;
import com.harley.bank.model.services.TransferService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
public class TransferControllerTest {

    static String URL_TRANSFER_API = "/api/transfer";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TransferService transferService;

    @Test
    @DisplayName("Deve agendar uma transferência com sucesso.")
    public void givenValidParams_whenCallScheduleTransfer_thenReturnACreatedSchedule() throws Exception {
        LocalDate schedulingDate = LocalDate.now().plusDays(1);

        TransferDTO transferDTO = TransferDTO.builder().originAccount(123456).destinationAccount(654321)
                .transferValue(100.0).build();

        Transfer transfer = Transfer.builder().id(1L).originAccount(123456).destinationAccount(654321)
                .transferValue(100.0).schedulingDate(schedulingDate).transferDate(LocalDate.now()).build();
        BDDMockito.given(transferService.createTransfer(Mockito.any(Transfer.class))).willReturn(transfer);

        String json = new ObjectMapper().writeValueAsString(transferDTO);

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URL_TRANSFER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("originAccount").value(123456));
    }

    @Test
    @DisplayName("Deve lançar um erro quando os dados forem inválidos.")
    public void givenInvalidParams_whenCallScheduleTransfer_thenThrowAnException() throws Exception {

        String json = new ObjectMapper().writeValueAsString(new TransferDTO());

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .post(URL_TRANSFER_API)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(json);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("erros", hasSize(3)));

    }
}
