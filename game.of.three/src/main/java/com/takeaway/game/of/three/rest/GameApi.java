package com.takeaway.game.of.three.rest;

import com.takeaway.game.of.three.GameConfig;
import com.takeaway.game.of.three.models.DataPayload;
import com.takeaway.game.of.three.models.GameInitialRequest;
import com.takeaway.game.of.three.models.GameResponse;
import com.takeaway.game.of.three.services.GameService;
import com.takeaway.game.of.three.utils.RandomHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import javax.validation.Valid;

/**
 * A main API class for the game
 */
@RestController
@RequestMapping("/game")
@RequiredArgsConstructor
@Slf4j
public class GameApi {
    private final GameConfig gameConfig;
    private final GameService gameService;
    /**
     * Init game with the init number if the game is configured as manual
     * @param request
     */
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/init")
    public void init(@RequestBody @Valid GameInitialRequest request){
        Integer initNumber = gameConfig.getAuto()==Boolean.TRUE? RandomHelper.getRandomNumber(1, gameConfig.getMaxRange()) :
                request.getNumber();
        gameService.initGame(initNumber);
    }

    /**
     * The play API to generate the new number and check if the player wins.
     * @param request
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "/play", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<GameResponse> play(@RequestBody @Valid DataPayload request){
        log.info("Received: " + request);
        return Mono.just(gameService.playGame(request));
    }

    @GetMapping("/health")
    public Mono<Void> healthCheck(){
        return Mono.empty();
    }


}
