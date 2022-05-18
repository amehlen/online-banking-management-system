package de.amehlen.obms.service;

import de.amehlen.obms.dto.request.UserRequestDTO;
import de.amehlen.obms.dto.response.UserResponseDTO;
import de.amehlen.obms.exception.UserAlreadyExistException;
import de.amehlen.obms.exception.UserNotFoundException;
import de.amehlen.obms.model.User;
import de.amehlen.obms.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ModelMapper modelMapper;

  public List<UserResponseDTO> getAllUsers() {
    return userRepository
        .findAll()
        .stream()
        .map(user -> modelMapper.map(user, UserResponseDTO.class))
        .collect(Collectors.toList());
  }

  public UserResponseDTO getUserById(Long id) {
    return userRepository
        .findById(id)
        .map(user -> modelMapper.map(user, UserResponseDTO.class))
        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
  }

  public UserResponseDTO createNewUser(UserRequestDTO userRequestDTO) {
    Optional<User> userOptional = userRepository.findUserByEmail(userRequestDTO.getEmail());
    if (userOptional.isPresent()) {
      throw new UserAlreadyExistException(
          "User with email " + userRequestDTO.getEmail() + " already exist.");
    }
    User newUser = modelMapper.map(userRequestDTO, User.class);
    userRepository.save(newUser);
    return modelMapper.map(newUser, UserResponseDTO.class);
  }

  public UserResponseDTO updateUser(Long id, UserRequestDTO userRequestDTO) {
    User userOptional = userRepository
        .findById(id)
        .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found."));
    userOptional.setFirstname(userRequestDTO.getFirstname());
    userOptional.setLastname(userRequestDTO.getLastname());
    userOptional.setEmail(userRequestDTO.getEmail());
    userRepository.save(userOptional);
    return modelMapper.map(userOptional, UserResponseDTO.class);
  }

  public void deleteUser(Long id) {
    userRepository.deleteById(id);
  }
}
