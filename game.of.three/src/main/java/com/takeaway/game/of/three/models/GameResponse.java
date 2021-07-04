package com.takeaway.game.of.three.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@ToString
@Data
public class GameResponse {
    DataPayload payload;
    private GameStatus status;
}
