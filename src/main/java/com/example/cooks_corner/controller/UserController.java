package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.MyUserResponseDto;
import com.example.cooks_corner.dto.UserEditDto;
import com.example.cooks_corner.dto.UserResponseDto;
import com.example.cooks_corner.dto.UserShortResponseDto;
import com.example.cooks_corner.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {
    private final UserService userService;

    @GetMapping("/my-account")
    public ResponseEntity<?>  myAccount(Principal principal){
        MyUserResponseDto myUserResponseDto = userService.findMyAccount(principal.getName());
        return ResponseEntity.ok(myUserResponseDto);
    }

    @GetMapping("{userId}")
    public ResponseEntity<?> findUserAccount(@PathVariable("userId") Long id){
        UserResponseDto userResponseDto = userService.findUserAccount(id);
        return ResponseEntity.ok(userResponseDto);
    }

    @PostMapping("/edit")
    public ResponseEntity<?> editUser(@RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "description", required = false) String description,
                                      @RequestParam(name = "photo", required = false) MultipartFile photo,
                                      Principal principal){
        UserEditDto userEditDto = UserEditDto.builder().name(name).description(description).photo(photo).build();
        userService.editUser(userEditDto, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @PostMapping("/followings/add/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") Long id, Principal principal){
        userService.followUser(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User added to followings successfully"));
    }

    @PostMapping("/followings/remove/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable("userId") Long id, Principal principal){
        userService.unfollowUser(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User removed from followings successfully"));
    }

    @GetMapping("/search")
    public ResponseEntity<?> findUserByName(@RequestParam(name = "name") String name){
        List<UserShortResponseDto> users = userService.findUsersByName(name);
        return ResponseEntity.ok(users);
    }


}
