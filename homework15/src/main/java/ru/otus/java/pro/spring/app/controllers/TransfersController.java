package ru.otus.java.pro.spring.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.otus.java.pro.spring.app.dtos.ExecuteTransferDtoRq;
import ru.otus.java.pro.spring.app.dtos.TransferDto;
import ru.otus.java.pro.spring.app.dtos.TransfersPageDto;
import ru.otus.java.pro.spring.app.entities.Transfer;
import ru.otus.java.pro.spring.app.exceptions_handling.ResourceNotFoundException;
import ru.otus.java.pro.spring.app.services.TransfersService;

import java.util.function.Function;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/transfers")
public class TransfersController {
    private final TransfersService transfersService;

    private static final Function<Transfer, TransferDto> ENTITY_TO_DTO = t -> new TransferDto(t.getId(), t.getClientId(), t.getTargetClientId(), t.getSourceAccount(), t.getTargetAccount(), t.getMessage(), t.getAmount());

    @GetMapping
    public TransfersPageDto getAllTransfers(@RequestHeader(name = "client-id") String clientId) {
        return new TransfersPageDto(
                transfersService
                        .getAllTransfers(clientId)
                        .stream()
                        .map(ENTITY_TO_DTO).collect(Collectors.toList())
        );
    }

    @GetMapping("/{id}")
    public TransferDto getTransferById(@RequestHeader(name = "client-id") String clientId, @PathVariable String id) {
        return ENTITY_TO_DTO.apply(transfersService.getTransferById(id, clientId).orElseThrow(() -> new ResourceNotFoundException("Перевод не найден")));
    }

    @PostMapping
    public void executeTransfer(@RequestHeader(name = "client-id") String clientId, @RequestBody ExecuteTransferDtoRq executeTransferDtoRq) {
        transfersService.execute(clientId, executeTransferDtoRq);
    }
}
