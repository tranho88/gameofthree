package com.takeaway.game.of.three.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class DataPayload {
    @NotNull
    private Integer sourcePlayerId;

    @NotNull
    @Positive
    private Integer number;

    @NotNull
    private Integer destinationPlayerId;
}
