package ru.otus.serialization.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SmsStatisticsDto implements Serializable {
    private String chatIdentifier = "";
    private List<String> lastList = new ArrayList<>();
    private List<MessageDto> messages = new ArrayList<>();
}
