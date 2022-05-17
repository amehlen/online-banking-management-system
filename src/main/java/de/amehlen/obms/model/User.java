package de.amehlen.obms.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Long id;

  @NotEmpty(message = "The firstname of the client may not be empty")
  private String firstname;

  @NotEmpty(message = "The lastname of the client may not be empty")
  private String lastname;

  @NotEmpty(message = "The email of the client may not be empty")
  @Email(message = "Email should be valid")
  private String email;


}
