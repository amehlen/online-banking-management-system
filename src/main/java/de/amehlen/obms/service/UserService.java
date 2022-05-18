package de.amehlen.obms.service;

import de.amehlen.obms.dto.request.UserRequestDTO;
import de.amehlen.obms.dto.response.UserResponseDTO;
import de.amehlen.obms.exception.UserAlreadyExistException;
import de.amehlen.obms.exception.UserNotFoundException;
import de.amehlen.obms.model.User;
import de.amehlen.obms.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  private static final Logger log = LoggerFactory.getLogger(UserService.class);

  private UserRepository userRepository;
  private ModelMapper modelMapper;

  @Autowired
  public UserService(UserRepository userRepository, ModelMapper modelMapper) {
    this.userRepository = userRepository;
    this.modelMapper = modelMapper;
  }

  public List<UserResponseDTO> getAllUsers() {
    log.info("Load all bank users from database");
    return userRepository
        .findAll()
        .stream()
        .map(user -> modelMapper.map(user, UserResponseDTO.class))
        .toList();
  }

  public UserResponseDTO getUserById(Long id) {
    log.info("Load bank user with id {} from database", id);
    return userRepository
        .findById(id)
        .map(user -> modelMapper.map(user, UserResponseDTO.class))
        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
  }

  public UserResponseDTO createNewUser(UserRequestDTO userRequestDTO) {
    log.info("Create new bank user {}", userRequestDTO);
    Optional<User> userOptional = userRepository.findUserByEmail(userRequestDTO.getEmail());
    if (userOptional.isPresent()) {
      log.info("Bank user with email {} already exist", userRequestDTO.getEmail());
      throw new UserAlreadyExistException(
          "User with email " + userRequestDTO.getEmail() + " already exist.");
    }
    User newUser = modelMapper.map(userRequestDTO, User.class);
    userRepository.save(newUser);
    log.info("New user saved to database {}", newUser);
    return modelMapper.map(newUser, UserResponseDTO.class);
  }

  public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
    User userOptional = userRepository
        .findById(id)
        .orElseThrow(() -> {
          log.info("Bank user with id {} not found in database", id);
          return new UserNotFoundException("User with id " + id + " not found.");
        });
    userOptional.setFirstname(userRequestDTO.getFirstname());
    userOptional.setLastname(userRequestDTO.getLastname());
    userOptional.setEmail(userRequestDTO.getEmail());
    userRepository.save(userOptional);
    UserResponseDTO userResponseDTO = modelMapper.map(userOptional, UserResponseDTO.class);
    log.info("Bank user updated in database {}", userResponseDTO);
    return userResponseDTO;
  }

  public void deleteUser(Long id) {
    log.info("Delete bank user with id {}", id);
    userRepository.deleteById(id);
  }
}
