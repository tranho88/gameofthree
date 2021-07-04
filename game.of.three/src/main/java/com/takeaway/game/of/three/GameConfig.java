package com.takeaway.game.of.three;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "game-config")
@Data
public class GameConfig {
    private Integer opponentId;
    private String opponentUri;
    private Integer selfId;
    private Boolean auto;
    private Integer maxRange;
}
