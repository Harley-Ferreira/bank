package com.harley.bank.utils;

import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.ArrayList;
import java.util.List;

public class ApiErros {

    @Getter
    private List<String> erros;
    public ApiErros(BindingResult bindingResult) {
        this.erros = new ArrayList<>();
        bindingResult.getAllErrors().forEach(error ->  this.erros.add(error.getDefaultMessage()));
    }
}
