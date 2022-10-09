package com.harley.bank.controllers;

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
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.Arrays;

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
        TransferDTO transferDTO = TransferDTO.builder().originAccount(123456).destinationAccount(654321)
                .transferValue(100.0).build();

        Transfer transfer = getTransfer();
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

    @Test
    @DisplayName("Deve retornar uma lista com todos os agendamentos feitos pelo usuário.")
    public void givenAnyParam_WhenCallGetTransfersList_ThenReturnAPageTransferDTO() throws Exception {
        Transfer transfer = getTransfer();

        BDDMockito.given(transferService.getTransfersList(Mockito.any(Transfer.class), Mockito.any(Pageable.class)))
                .willReturn(new PageImpl<>(Arrays.asList(transfer), PageRequest.of(0, 100), 1));

        String queryString = String.format("?page=0&size=100");

        MockHttpServletRequestBuilder requestBuilder = MockMvcRequestBuilders
                .get(URL_TRANSFER_API.concat(queryString))
                .accept(MediaType.APPLICATION_JSON);

        mockMvc.perform(requestBuilder)
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", Matchers.hasSize(1)))
                .andExpect(jsonPath("totalElements").value(1))
                .andExpect(jsonPath("pageable.pageSize").value(100))
                .andExpect(jsonPath("pageable.pageNumber").value(0));
    }

    private Transfer getTransfer() {
        LocalDate schedulingDate = LocalDate.now().plusDays(1);
        return Transfer.builder()
                .id(1L)
                .originAccount(123456)
                .destinationAccount(654321)
                .transferValue(100.0)
                .schedulingDate(schedulingDate)
                .transferDate(LocalDate.now()).build();
    }
}
