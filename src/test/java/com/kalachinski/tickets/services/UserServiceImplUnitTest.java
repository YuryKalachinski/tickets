package com.kalachinski.tickets.services;

import com.kalachinski.tickets.domains.User;
import com.kalachinski.tickets.exceptions.NotFoundException;
import com.kalachinski.tickets.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
class UserServiceImplUnitTest {

    private final UserService userService;

    @Autowired
    UserServiceImplUnitTest(UserService userService) {
        this.userService = userService;
    }

    @MockBean
    private UserRepository userRepository;

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Get All Users")
    void getAllUsers() {
        List<User> userList = new ArrayList<>();
        when(userRepository.findAll()).thenReturn(userList);
        Assertions.assertEquals(userService.getAllUsers(), userList);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Get All Users")
    void getAllUsersWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, userService::getAllUsers);
        verify(userRepository, never()).findAll();
    }

    @Test
    @WithMockUser(authorities = "ADMIN")
    @DisplayName("Get User by Id")
    void getUserById() {
        User user = new User();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        Assertions.assertEquals(userService.getUserById(1L), user);
        verify(userRepository, times(1)).findById(anyLong());
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Get User by Id")
    void getUSerByIdWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            userService.getUserById(1L);
        });
        verify(userRepository, never()).findById(anyLong());
    }

    @Test
    @DisplayName("Registration User")
    void registrationUser() {
        User user = User.builder()
                .password("password")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);
        Assertions.assertEquals(userService.registrationUser(user), user);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("Save User")
    @WithMockUser(authorities = "ADMIN")
    void saveUser() {
        User user = User.builder()
                .password("password")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(user);
        Assertions.assertEquals(userService.saveUser(user), user);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Save User")
    void saveUserWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            userService.saveUser(User.builder().build());
        });
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Update User")
    @WithMockUser(authorities = "ADMIN")
    void updateUser() {
        User user = User.builder()
                .password("password")
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);
        userService.updateUser(user, 1L);
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    @DisplayName("NotFoundException from Update User")
    @WithMockUser(authorities = "ADMIN")
    void updateUserWithNotFoundException() {
        User user = User.builder()
                .password("password")
                .build();
        when(userRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> userService.updateUser(user, 1L));
        verify(userRepository, times(1)).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Update User")
    void updateUserWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            userService.updateUser(User.builder().build(), 1L);
        });
        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Delete Exist User")
    @WithMockUser(authorities = "ADMIN")
    void deleteExistUserById() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, times(1)).deleteById(anyLong());
    }

    @Test
    @DisplayName("Delete NotExist User")
    @WithMockUser(authorities = "ADMIN")
    void deleteNotExistUserById() {
        when(userRepository.existsById(anyLong())).thenReturn(false);
        userService.deleteUserById(1L);
        verify(userRepository, times(1)).existsById(anyLong());
        verify(userRepository, never()).deleteById(anyLong());
    }

    @Test
    @DisplayName("AuthenticationCredentialsNotFoundException from Delete User")
    void deleteUserWithAuthenticationCredentialsNotFoundException() {
        Assertions.assertThrows(AuthenticationCredentialsNotFoundException.class, () -> {
            userService.deleteUserById(1L);
        });
        verify(userRepository, never()).findById(anyLong());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Get User by Login")
    void getUserByLogin() {
        User user = new User();
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.of(user));
        Assertions.assertEquals(userService.getUserByLogin("Login"), user);
        verify(userRepository, times(1)).findByLogin(anyString());
    }

    @Test
    @DisplayName("UsernameNotFoundException from Get User by Login")
    void getUserByLoginWithUsernameNotFoundException() {
        when(userRepository.findByLogin(anyString())).thenReturn(Optional.empty());
        Assertions.assertThrows(UsernameNotFoundException.class, () -> {
            userService.getUserByLogin("Login");
        });
        verify(userRepository, times(1)).findByLogin(anyString());
    }
}