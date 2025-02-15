package ru.otus.serialization.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Message {
    @JsonProperty("ROWID")
    private Integer rowid;

    @JsonProperty("attributedBody")
    private String attributedBody;

    @JsonProperty("belong_number")
    private String belongNumber;

    @JsonProperty("date")
    private Long date;

    @JsonProperty("date_read")
    private Long dateRead;

    @JsonProperty("guid")
    private String guid;

    @JsonProperty("handle_id")
    private Integer handleId;

    @JsonProperty("has_dd_results")
    private Boolean hasDdResults;

    @JsonProperty("is_deleted")
    private Boolean isDeleted;

    @JsonProperty("is_from_me")
    private Boolean isFromMe;

    @JsonProperty("send_date")
    private String sendDate;

    @JsonProperty("send_status")
    private Integer sendStatus;

    @JsonProperty("service")
    private String service;

    @JsonProperty("text")
    private String text;
}
