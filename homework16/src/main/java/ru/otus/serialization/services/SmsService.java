package ru.otus.serialization.services;

import ru.otus.serialization.dtos.SmsStatisticsDto;

import java.util.Map;

public interface SmsService {
    Map<String, SmsStatisticsDto> getStatistics();
}
