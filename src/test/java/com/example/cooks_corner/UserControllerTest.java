package com.example.cooks_corner;

import com.example.cooks_corner.controller.UserController;
import com.example.cooks_corner.dto.MyUserResponseDto;
import com.example.cooks_corner.dto.UserEditDto;
import com.example.cooks_corner.dto.UserResponseDto;
import com.example.cooks_corner.exception.NotFoundException;
import com.example.cooks_corner.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.security.Principal;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void testMyAccountSuccess() {
        Principal principal = () -> "test@example.com";
        MyUserResponseDto myUserResponseDto = new MyUserResponseDto();
        when(userService.findMyAccount(anyString())).thenReturn(myUserResponseDto);

        var result = userController.myAccount(principal);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(myUserResponseDto, result.getBody());
    }

    @Test
    void testMyAccountUserNotFound() {
        Principal principal = () -> "test@example.com";
        doThrow(new NotFoundException("User not found")).when(userService).findMyAccount(anyString());

        var exception = assertThrows(NotFoundException.class, () -> userController.myAccount(principal));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testFindUserAccountSuccess() {
        UserResponseDto userResponseDto = new UserResponseDto();
        when(userService.findUserAccount(anyLong())).thenReturn(userResponseDto);

        var result = userController.findUserAccount(1L);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(userResponseDto, result.getBody());
    }

    @Test
    void testFindUserAccountUserNotFound() {
        doThrow(new NotFoundException("User not found")).when(userService).findUserAccount(anyLong());

        var exception = assertThrows(NotFoundException.class, () -> userController.findUserAccount(1L));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void testEditUserSuccess() {
        Principal principal = () -> "test@example.com";
        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());

        doNothing().when(userService).editUser(any(UserEditDto.class), anyString());

        var result = userController.editUser("Test User", "Description", photo, principal);

        assertEquals(200, result.getStatusCode().value());
        assertEquals(Map.of("message", "User updated successfully"), result.getBody());
    }

    @Test
    void testEditUserUserNotFound() {
        Principal principal = () -> "test@example.com";
        MockMultipartFile photo = new MockMultipartFile("photo", "photo.jpg", MediaType.IMAGE_JPEG_VALUE, "test image".getBytes());

        doThrow(new NotFoundException("User not found")).when(userService).editUser(any(UserEditDto.class), anyString());

        var exception = assertThrows(NotFoundException.class, () -> userController.editUser("Test User", "Description", photo, principal));

        assertEquals("User not found", exception.getMessage());
    }
}
