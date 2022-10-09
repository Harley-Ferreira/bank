package com.harley.bank.controllers;

import com.harley.bank.dtos.TransferDTO;
import com.harley.bank.model.entities.Transfer;
import com.harley.bank.model.services.TransferService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.print.Book;
import java.util.List;

@RestController
@RequestMapping("/api/transfer")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;
    private final ModelMapper modelMapper;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TransferDTO createTransfer(@RequestBody @Valid TransferDTO transferDTO) {
        Transfer transfer = modelMapper.map(transferDTO, Transfer.class);

        Transfer savedTransfer = transferService.createTransfer(transfer);

        return modelMapper.map(savedTransfer, TransferDTO.class);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErros handleValidationException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        return new ApiErros(bindingResult);
    }
}