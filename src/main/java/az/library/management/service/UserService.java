package az.library.management.service;

import az.library.management.dao.entity.Role;
import az.library.management.dao.entity.User;
import az.library.management.dao.repository.UserRepository;
import az.library.management.mapper.UserMapper;
import az.library.management.model.dto.user.NewUserDTO;
import az.library.management.model.dto.user.UserDTO;
import az.library.management.model.exception.BooksNotReturnedException;
import az.library.management.model.exception.NoUserFoundException;
import az.library.management.model.dto.user.UpdateUserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public Page<UserDTO> getUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserMapper.INSTANCE::mapUserToUserDto);
    }

    public UserDTO getUserById(Long user_id) throws NoUserFoundException {
        User user = userRepository.findById(user_id).
                orElseThrow(() -> new NoUserFoundException("No user exists with id: " + user_id));
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    public UserDTO addUser(NewUserDTO newUserDTO, Role role) {
        User user = UserMapper.INSTANCE.mapNewUserToUser(newUserDTO);
        user.setRole(role);
        userRepository.save(user);
        return UserMapper.INSTANCE.mapUserToUserDto(user);
    }

    public UserDTO updateUser(Long user_id, UpdateUserDTO updateUserDTO) throws NoUserFoundException {
        User user = userRepository.findById(user_id).
                orElseThrow(() -> new NoUserFoundException("No user exists with id: " + user_id));
        user.setEmail(updateUserDTO.getEmail());
        user.setName(updateUserDTO.getName());
        return UserMapper.INSTANCE.mapUserToUserDto(userRepository.save(user));
    }

    public void deleteUser(Long user_id) throws NoUserFoundException, BooksNotReturnedException {
        User user = userRepository.findById(user_id).
                orElseThrow(() -> new NoUserFoundException("No user exist with id: " + user_id));
        if (user.getTransactions() == null || user.getTransactions().isEmpty()) { // if user dont have reserved books
            userRepository.deleteById(user_id);
        } else {
            boolean allBooksReturned = user.getTransactions().stream().anyMatch(transaction -> transaction.getReturnTime() == null);
            if (allBooksReturned) {
                user.setTransactions(null);
                userRepository.deleteById(user_id);
            } else {
                throw new BooksNotReturnedException("User with id: " + user_id + " can't be deleted without returning all books!");
            }
        }
    }
}
