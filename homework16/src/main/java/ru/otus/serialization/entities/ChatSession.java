package ru.otus.serialization.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class ChatSession {
    @JsonProperty("chat_id")
    private Integer chatId;

    @JsonProperty("chat_identifier")
    private String chatIdentifier;

    @JsonProperty("display_name")
    private String displayName;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    @JsonProperty("members")
    private List<Member> members;

    @JsonProperty("messages")
    private List<Message> messages;
}
