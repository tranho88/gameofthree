package com.takeaway.game.of.three.models;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.Positive;

@ToString
@Data
public class GameInitialRequest {
    @Positive
    private Integer number;
}
