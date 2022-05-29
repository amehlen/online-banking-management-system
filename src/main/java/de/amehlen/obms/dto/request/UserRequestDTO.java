package de.amehlen.obms.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
@Schema(name = "User Request")
public class UserRequestDTO {

  @NotEmpty(message = "The firstname of the client may not be empty")
  @Schema(
      description = "firstname of the bank user",
      name = "firstname",
      type = "String",
      example = "Max"
  )
  private String firstname;

  @NotEmpty(message = "The lastname of the client may not be empty")
  @Schema(
      description = "lastname of the bank user",
      name = "lastname",
      type = "String",
      example = "Mustermann"
  )
  private String lastname;

  @NotEmpty(message = "The email of the client may not be empty")
  @Email(message = "Email should be valid")
  @Schema(
      description = "email of the bank user",
      name = "email",
      type = "String",
      example = "max@mustermann.de"
  )
  private String email;

}
