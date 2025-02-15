package ru.otus.java.pro.spring.app.dtos;

public record TransferDto(String id, String clientId, String targetClientId, String sourceAccount, String targetAccount, String message,
                          int amount) {
}