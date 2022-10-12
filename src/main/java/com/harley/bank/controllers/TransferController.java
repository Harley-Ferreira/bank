package com.harley.bank.controllers;

import com.harley.bank.dtos.TransferDTO;
import com.harley.bank.model.entities.Transfer;
import com.harley.bank.model.services.TransferService;
import com.harley.bank.utils.ApiErros;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@Api("Agendamento de Transferência API")
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final ModelMapper modelMapper;

    @ApiOperation("CRIAR UM NOVO AGENDAMENTO DE TRANSFERÊNCIA")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferDTO createTransfer(@RequestBody @Valid TransferDTO transferDTO) {
        Transfer transfer = modelMapper.map(transferDTO, Transfer.class);

        Transfer savedTransfer = transferService.createTransfer(transfer);

        return modelMapper.map(savedTransfer, TransferDTO.class);
    }

    @ApiOperation("LISTAR TODAS AS TRANSFERÊNCIAS AGENDADAS")
    @GetMapping
    public Page<TransferDTO> getTransfersList(@RequestParam(value = "page", defaultValue = "0") Integer page,
                                               @RequestParam(value = "size", defaultValue = "10") Integer sizePage) {
        PageRequest pageable = PageRequest.of(page, sizePage);
        Page<Transfer> transferList = transferService.getTransfersList(pageable);
        List<TransferDTO> transferDTOList = transferList.getContent()
                .stream()
                .map(t -> modelMapper.map(t, TransferDTO.class))
                .toList();

        return new PageImpl<>(transferDTOList, pageable, transferList.getTotalElements());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return new ApiErros(bindingResult);
    }
}
