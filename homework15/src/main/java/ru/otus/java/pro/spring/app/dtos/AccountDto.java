package ru.otus.java.pro.spring.app.dtos;

public record AccountDto (String id, String number, String clientId,
                          int balance, boolean isBlocked) {
}
