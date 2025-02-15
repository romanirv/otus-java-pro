package ru.otus.java.pro.spring.app.services;

import ru.otus.java.pro.spring.app.dtos.ExecuteTransferDtoRq;
import ru.otus.java.pro.spring.app.entities.Transfer;

import java.util.List;
import java.util.Optional;

public interface TransfersService {
    Optional<Transfer> getTransferById(String id, String clientId);

    List<Transfer> getAllTransfers(String clientId);

    void execute(String clientId, ExecuteTransferDtoRq executeTransferDtoRq);
}
