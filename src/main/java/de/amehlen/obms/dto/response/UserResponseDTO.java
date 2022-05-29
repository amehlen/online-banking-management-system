package de.amehlen.obms.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder(setterPrefix = "with")
@Schema(name = "User Response")
public class UserResponseDTO {

  @Schema(
      description = "bank user id",
      name = "id",
      type = "Long",
      example = "1"
  )
  private Long id;

  @Schema(
      description = "firstname of the bank user",
      name = "firstname",
      type = "String",
      example = "Max"
  )
  private String firstname;

  @Schema(
      description = "lastname of the bank user",
      name = "lastname",
      type = "String",
      example = "Mustermann"
  )
  private String lastname;

  @Schema(
      description = "email of the bank user",
      name = "email",
      type = "String",
      example = "max@mustermann.de"
  )
  private String email;

}
