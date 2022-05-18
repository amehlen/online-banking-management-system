package de.amehlen.obms.controller;

import de.amehlen.obms.dto.request.UserRequestDTO;
import de.amehlen.obms.dto.response.UserResponseDTO;
import de.amehlen.obms.model.User;
import de.amehlen.obms.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "users", description = "Endpoints for getting and manipulating bank users")
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  private UserService userService;

  @Operation(
      summary = "Get a list of all bank users",
      description = "Get a list of all bank users saved in the database",
      tags = {"users"},
      responses = {
          @ApiResponse(
              description = "List of all users",
              responseCode = "200",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = User.class)
              )
          )
      }
  )
  @GetMapping
  public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
    List<UserResponseDTO> users = userService.getAllUsers();
    return new ResponseEntity<>(users, HttpStatus.OK);
  }

  @Operation(
      summary = "Get a selected user by id",
      description = "Get a selected user by id from the database",
      tags = {"users"},
      responses = {
          @ApiResponse(
              description = "Selected user",
              responseCode = "200",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = User.class)
              )
          )
      }
  )
  @GetMapping("/{id}")
  public ResponseEntity<UserResponseDTO> getUserById(@PathVariable(value = "id") Long id) {
    UserResponseDTO user = userService.getUserById(id);
    return new ResponseEntity<>(user, HttpStatus.OK);
  }

  @Operation(
      summary = "Add a new user",
      description = "Add a new user to the database",
      tags = {"users"},
      responses = {
          @ApiResponse(
              description = "New added user",
              responseCode = "201",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = User.class)
              )
          )
      }
  )
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseEntity<UserResponseDTO> createNewUser(@Valid @RequestBody UserRequestDTO userRequestDTO) {
    UserResponseDTO user = userService.createNewUser(userRequestDTO);
    return new ResponseEntity<>(user, HttpStatus.CREATED);
  }

  @Operation(
      summary = "Update a selected user",
      description = "Update a selected user in the database",
      tags = {"users"},
      responses = {
          @ApiResponse(
              description = "Updated user",
              responseCode = "200",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = User.class)
              )
          )
      }
  )
  @PutMapping("/{id}")
  public ResponseEntity<UserResponseDTO> updateUser(@PathVariable(value = "id") Long id,
      @Valid @RequestBody UserRequestDTO userRequestDTO) {
    UserResponseDTO updatedUser = userService.updateUser(id, userRequestDTO);
    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
  }

  @Operation(
      summary = "Delete a selected user",
      description = "Delete a selected user in the database",
      tags = {"users"}
  )
  @DeleteMapping("/{id}")
  public void deleteUser(@PathVariable(value = "id") Long id) {
    userService.deleteUser(id);
  }

}
