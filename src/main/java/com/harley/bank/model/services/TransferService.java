package com.harley.bank.model.services;

import com.harley.bank.model.entities.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TransferService {

    Transfer createTransfer(Transfer transfer);

    Page<Transfer> getTransfersList(Transfer transfer, Pageable pageable);

    Page<Transfer> getTransfersList(Pageable pageable);

    Transfer descontaTaxa(Transfer transfer);
}
