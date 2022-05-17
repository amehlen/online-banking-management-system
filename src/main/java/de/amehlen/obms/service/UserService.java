package de.amehlen.obms.service;

import de.amehlen.obms.exception.UserAlreadyExistException;
import de.amehlen.obms.exception.UserNotFoundException;
import de.amehlen.obms.model.User;
import de.amehlen.obms.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

  public User getUserById(Long id) {
    return userRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
  }

  public User createNewUser(User user) {
    Optional<User> userOptional = userRepository.findUserByEmail(user.getEmail());
    if (userOptional.isPresent()) {
      throw new UserAlreadyExistException(
          "User with email " + user.getEmail() + " already exist.");
    }
    return userRepository.save(user);
  }

  public User updateUser(Long id, User user) {
    User userOptional = userRepository.findById(id)
        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
    userOptional.setFirstname(user.getFirstname());
    userOptional.setLastname(user.getLastname());
    userOptional.setEmail(user.getEmail());
    return userRepository.save(userOptional);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
