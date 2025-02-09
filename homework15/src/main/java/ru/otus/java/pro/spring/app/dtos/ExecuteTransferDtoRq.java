package ru.otus.java.pro.spring.app.dtos;

public record ExecuteTransferDtoRq(String targetClientId, String sourceAccount, String targetAccount, String message, int amount) {
}