package com.takeaway.game.of.three;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.takeaway.game.of.three.models.DataPayload;
import com.takeaway.game.of.three.models.GameResponse;
import com.takeaway.game.of.three.models.GameStatus;
import com.takeaway.game.of.three.rest.GameApi;
import com.takeaway.game.of.three.rest.errorhandlers.RestExceptionHandler;
import com.takeaway.game.of.three.rest.errorhandlers.exceptions.models.NotAvailableOpponentException;
import com.takeaway.game.of.three.services.GameService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GameOfThreeApplicationTests {
	private MockMvc mockMvc;

	@Autowired
	private GameApi gameApi;

	private final ObjectMapper objectMapper = new ObjectMapper();
	@MockBean
	private GameService gameService;

	@Before
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(gameApi)
				.setControllerAdvice(new RestExceptionHandler())
				.build();
	}

	@Test
	public void testInitGameValidWithAvailableOpponent() throws Exception {
		//mock
		Mockito.when(gameService.checkAvailableOpponent()).thenReturn(true);
		Mockito.doNothing().when(gameService).initGame(Mockito.anyInt());

		Map<String, Object> request =Map.of("number", 100);
		mockMvc.perform(MockMvcRequestBuilders.post("/game/init")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsBytes(request))
				).andExpect(MockMvcResultMatchers.status().isCreated());
	}


	@Test
	public void testInitGameValidWithNOTAvailableOpponent() throws Exception {
		//mock
		Mockito.doThrow(NotAvailableOpponentException.class).when(gameService).initGame(Mockito.anyInt());

		Map<String, Object> request =Map.of("number", 100);
		mockMvc.perform(MockMvcRequestBuilders.post("/game/init")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request))
		).andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testPlayGameValidRequest() throws Exception {
		//mock
		GameResponse response = new GameResponse(new DataPayload(1, 100, 2), GameStatus.PLAYING);
		Mockito.doReturn(response).when(gameService).playGame(Mockito.any(DataPayload.class));

		Map<String, Object> request =Map.of("number", 100, "sourcePlayerId", 1, "destinationPlayerId",2);
		mockMvc.perform(MockMvcRequestBuilders.post("/game/play")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request))
		).andExpect(MockMvcResultMatchers.status().isOk());
	}


	@Test
	public void testPlayGameInvalidRequest() throws Exception {

		Map<String, Object> request =Map.of("number", -100, "sourcePlayerId", 1, "destinationPlayerId",2);
		mockMvc.perform(MockMvcRequestBuilders.post("/game/play")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsBytes(request))
		).andExpect(MockMvcResultMatchers.status().isBadRequest());
	}

}
