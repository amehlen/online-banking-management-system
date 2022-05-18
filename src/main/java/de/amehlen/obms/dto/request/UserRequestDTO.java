package de.amehlen.obms.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UserRequestDTO {

  @NotEmpty(message = "The firstname of the client may not be empty")
  private String firstname;

  @NotEmpty(message = "The lastname of the client may not be empty")
  private String lastname;

  @NotEmpty(message = "The email of the client may not be empty")
  @Email(message = "Email should be valid")
  private String email;

}
