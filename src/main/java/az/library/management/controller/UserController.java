package az.library.management.controller;


import az.library.management.dao.entity.Role;
import az.library.management.model.dto.user.NewUserDTO;
import az.library.management.model.exception.BooksNotReturnedException;
import az.library.management.model.exception.NoUserFoundException;
import az.library.management.model.dto.user.UpdateUserDTO;
import az.library.management.model.dto.user.UserDTO;
import az.library.management.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UserController {

    private final UserService userService;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO addUser(@RequestBody @Valid NewUserDTO newUserDTO) {
        return userService.registerUser(newUserDTO, Role.ROLE_USER);
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.CREATED)
    UserDTO addAdmin(@RequestBody @Valid NewUserDTO newUserDTO) {
        return userService.registerUser(newUserDTO,Role.ROLE_ADMIN);
    }

    @GetMapping()
    Page<UserDTO> getUsers(Pageable pageable) {
        return userService.getUsers(pageable);
    }

    @GetMapping("/{user_id}")
    UserDTO getUserById(@PathVariable Long user_id) throws NoUserFoundException {
        return userService.getUserById(user_id);
    }

    @PutMapping("/{user_id}")
    UserDTO updateUser(@PathVariable Long user_id, @RequestBody @Valid UpdateUserDTO updateUserDTO) throws NoUserFoundException {
        return userService.updateUser(user_id, updateUserDTO);
    }

    @DeleteMapping("/{user_id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long user_id) throws NoUserFoundException, BooksNotReturnedException {
        userService.deleteUser(user_id);
    }
}
