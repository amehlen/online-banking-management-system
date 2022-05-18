package de.amehlen.obms.dto.response;

import lombok.Data;

@Data
public class UserResponseDTO {

  private Long id;
  private String firstname;
  private String lastname;
  private String email;

}
