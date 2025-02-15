package ru.otus.serialization.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MessageDto implements Serializable {
    private String belongNumber;
    private String sendDate;
    private String text;
}
