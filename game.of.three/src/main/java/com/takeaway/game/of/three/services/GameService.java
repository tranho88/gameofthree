package com.takeaway.game.of.three.services;

import com.takeaway.game.of.three.GameConfig;
import com.takeaway.game.of.three.models.DataPayload;
import com.takeaway.game.of.three.models.GameResponse;
import com.takeaway.game.of.three.models.GameStatus;
import com.takeaway.game.of.three.rest.errorhandlers.exceptions.models.NotAvailableOpponentException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;

/**
 * Game service to process the main business logic
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class GameService {
    private final GameConfig gameConfig;
    private WebClient webClient;

    @PostConstruct
    public void init() {
        webClient = WebClient.builder()
                .baseUrl(gameConfig.getOpponentUri())
                .build();
    }


    /**
     * Check availability of opponent
     * @return status
     */
    public boolean checkAvailableOpponent() {
        try {
            log.info("PING opponent....");
            webClient.get().uri(URI.create(gameConfig.getOpponentUri().concat("/game/health"))).retrieve().bodyToMono(Void.class).block();
            log.info("Opponent:...PONG");
            return true;
        } catch (Exception ex) {
            log.error(ex.getMessage());
            return false;
        }
    }

    /**
     * Init game by init number
     * @param initNumber
     */
    public void initGame(int initNumber) {
        log.info("Game is started. Send the initial number [" + initNumber + "] to opponent");
        //always check the availability of opponent before sending request.
        if (checkAvailableOpponent()) {
            Mono<GameResponse> responseMono = webClient.post()
                    .uri(URI.create(gameConfig.getOpponentUri().concat("/game/play")))
                    .accept(MediaType.APPLICATION_JSON)
                    .bodyValue(new DataPayload(gameConfig.getSelfId(), initNumber, gameConfig.getOpponentId()))
                    .retrieve()
                    .bodyToMono(GameResponse.class);
            responseMono.subscribe();
        } else {
            throw new NotAvailableOpponentException();
        }
    }

    /**
     * Play game logic
     * @param request
     * @return
     */
    public GameResponse playGame(DataPayload request) {
        int newNum = request.getNumber() / 3;
        GameStatus gameStatus;

        if (request.getNumber() % 3 == 0) {
            newNum = request.getNumber() / 3;
        } else if ((request.getNumber() + 1) % 3 == 0) {
            newNum = (request.getNumber() + 1) / 3;
        } else if ((request.getNumber() - 1) % 3 == 0) {
            newNum = (request.getNumber() - 1) / 3;
        }

        DataPayload payload = new DataPayload(gameConfig.getSelfId(), newNum, gameConfig.getOpponentId());
        log.info("Calculated the new number: " + newNum);
        if (newNum == 1) {
            log.info("You are WINNER!");
            gameStatus = GameStatus.DONE;
        } else {
            gameStatus = GameStatus.PLAYING;
            if (gameConfig.getAuto() == Boolean.TRUE) {
                log.info("Auto send to opponent: " + payload);
                if (checkAvailableOpponent()) {
                    Mono<GameResponse> responseMono = webClient.post()
                            .uri(URI.create(gameConfig.getOpponentUri().concat("/game/play")))
                            .accept(MediaType.APPLICATION_JSON)
                            .bodyValue(payload)
                            .retrieve()
                            .bodyToMono(GameResponse.class);
                    ;
                    responseMono.subscribe();
                } else {
                    throw new NotAvailableOpponentException();
                }
            }
        }
        return new GameResponse(payload, gameStatus);
    }
}
