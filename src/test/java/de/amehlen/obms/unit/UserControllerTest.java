package de.amehlen.obms.unit;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.amehlen.obms.controller.UserController;
import de.amehlen.obms.dto.request.UserRequestDTO;
import de.amehlen.obms.dto.response.UserResponseDTO;
import de.amehlen.obms.exception.UserAlreadyExistException;
import de.amehlen.obms.exception.UserNotFoundException;
import de.amehlen.obms.service.UserService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
public class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper mapper;

  @MockBean
  private UserService userServiceMocked;

  @Nested
  @DisplayName("GET test requests to user endpoint")
  class GetTestRequestsToUserEndpoint {

    UserResponseDTO user1 = UserResponseDTO
        .builder()
        .withId(1L)
        .withFirstname("Max")
        .withLastname("Mustermann")
        .withEmail("max@mustermann.de")
        .build();
    UserResponseDTO user2 = UserResponseDTO
        .builder()
        .withId(2L)
        .withFirstname("Erika")
        .withLastname("Mustermann")
        .withEmail("erika@mustermann.de")
        .build();
    UserResponseDTO user3 = UserResponseDTO
        .builder()
        .withId(3L)
        .withFirstname("Julia")
        .withLastname("Musterfrau")
        .withEmail("julia@musterfrau.de")
        .build();

    @Test
    @DisplayName("Should return an empty list of users")
    void shouldReturnAnEmptyListOfUsers() throws Exception {
      when(userServiceMocked.getAllUsers()).thenReturn(new ArrayList<>());
      mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    @DisplayName("Should return a list of all users")
    void shouldReturnAListOfAllUsers() throws Exception {
      List<UserResponseDTO> users = new ArrayList<>(Arrays.asList(user1, user2, user3));
      when(userServiceMocked.getAllUsers()).thenReturn(users);
      mockMvc.perform(get("/users").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", hasSize(3)))
          .andExpect(jsonPath("$[0].firstname", is("Max")))
          .andExpect(jsonPath("$[1].firstname", is("Erika")))
          .andExpect(jsonPath("$[2].firstname", is("Julia")));
    }

    @Test
    @DisplayName("Should return a user given by id")
    void shouldReturnAUserGivenById() throws Exception {
      when(userServiceMocked.getUserById(user1.getId())).thenReturn(user1);
      mockMvc.perform(get("/users/1").contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$", notNullValue()))
          .andExpect(jsonPath("$.firstname", is("Max")));
    }

    @Test
    @DisplayName("Should return exception when no user found by id")
    void shouldReturnExceptionWhenNoUserFoundById() throws Exception {
      Long userId = 4L;
      String exceptionMessage = "User with id 4 not found.";
      when(userServiceMocked.getUserById(userId)).thenThrow(
          new UserNotFoundException(exceptionMessage));
      mockMvc.perform(get("/users/{id}", userId).contentType(MediaType.APPLICATION_JSON))
          .andExpect(status().isNotFound())
          .andExpect(
              result -> assertTrue(result.getResolvedException() instanceof UserNotFoundException))
          .andExpect(result -> assertEquals(exceptionMessage,
              result.getResolvedException().getMessage()));
    }

  }

  @Nested
  @DisplayName("POST test requests to user endpoint")
  class PostTestRequestsToUserEndpoint {

    UserRequestDTO userRequest = UserRequestDTO.builder()
        .withFirstname("Max")
        .withLastname("Mustermann")
        .withEmail("max@mustermann.de")
        .build();
    UserResponseDTO userResponse = UserResponseDTO.builder()
        .withId(1L)
        .withFirstname("Max")
        .withLastname("Mustermann")
        .withEmail("max@mustermann.de")
        .build();

    @Test
    @DisplayName("Should create a new user")
    void shouldCreateANewUser() throws Exception {
      when(userServiceMocked.createNewUser(userRequest)).thenReturn(userResponse);

      mockMvc.perform(post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .content(mapper.writeValueAsString(userRequest)))
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$", notNullValue()))
          .andExpect(jsonPath("$.id", is(1)))
          .andExpect(jsonPath("$.firstname", is("Max")))
          .andExpect(jsonPath("$.lastname", is("Mustermann")))
          .andExpect(jsonPath("$.email", is("max@mustermann.de")));

    }

    @Test
    @DisplayName("Should return exception when user already exist")
    void shouldReturnExceptionWhenUserAlreadyExist() throws Exception {
      String exceptionMessage = "User with email max@mustermann.de already exist.";
      when(userServiceMocked.createNewUser(userRequest)).thenThrow(
          new UserAlreadyExistException(exceptionMessage));

      mockMvc.perform(post("/users")
              .contentType(MediaType.APPLICATION_JSON)
              .accept(MediaType.APPLICATION_JSON)
              .content(mapper.writeValueAsString(userRequest)))
          .andExpect(status().isConflict())
          .andExpect(
              result -> assertTrue(
                  result.getResolvedException() instanceof UserAlreadyExistException))
          .andExpect(result -> assertEquals(exceptionMessage,
              result.getResolvedException().getMessage()));
    }

  }

}
