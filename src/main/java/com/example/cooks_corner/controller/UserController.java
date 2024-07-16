package com.example.cooks_corner.controller;

import com.example.cooks_corner.dto.MyUserResponseDto;
import com.example.cooks_corner.dto.UserEditDto;
import com.example.cooks_corner.dto.UserResponseDto;
import com.example.cooks_corner.dto.UserShortResponseDto;
import com.example.cooks_corner.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
@Tag(
        name = "User Management",
        description = "Endpoints for managing users, including account details, followings, and searching users."
)
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Get My Account Details",
            description = "Fetch the details of the logged-in user's account. The Principal parameter is used to identify the currently authenticated user.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account details retrieved successfully",
                            content = @Content(schema = @Schema(implementation = MyUserResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to access this resource", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @GetMapping("/my-account")
    public ResponseEntity<?>  myAccount(Principal principal){
        MyUserResponseDto myUserResponseDto = userService.findMyAccount(principal.getName());
        return ResponseEntity.ok(myUserResponseDto);
    }

    @Operation(
            summary = "Find User Account Details",
            description = "Fetch the details of a user's account by user ID.",
            parameters = @Parameter(
                    name = "userId",
                    description = "Unique identifier of the user",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Account details retrieved successfully", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to access this resource", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @GetMapping("{userId}")
    public ResponseEntity<?> findUserAccount(@PathVariable("userId") Long id){
        UserResponseDto userResponseDto = userService.findUserAccount(id);
        return ResponseEntity.ok(userResponseDto);
    }

    @Operation(
            summary = "Edit User Details",
            description = "Edit the details of the logged-in user's account. The Principal parameter is used to identify the currently authenticated user.",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "User details to be updated",
                    required = true,
                    content = @Content(
                            schema = @Schema(implementation = UserEditDto.class)
                    )
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User updated successfully", content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to edit this resource", content = @Content)
            }
    )
    @PostMapping("/edit")
    public ResponseEntity<?> editUser(@RequestParam(name = "name", required = false) String name,
                                      @RequestParam(name = "description", required = false) String description,
                                      @RequestPart(name = "photo", required = false) MultipartFile photo,
                                      Principal principal){
        UserEditDto userEditDto = UserEditDto.builder().name(name).description(description).photo(photo).build();
        userService.editUser(userEditDto, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User updated successfully"));
    }

    @Operation(
            summary = "Follow a User",
            description = "Add a user to the followings list of the logged-in user. The Principal parameter is used to identify the currently authenticated user.",
            parameters = @Parameter(
                    name = "userId",
                    description = "Unique identifier of the user to follow",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User added to followings successfully", content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to follow this user", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @PostMapping("/followings/add/{userId}")
    public ResponseEntity<?> followUser(@PathVariable("userId") Long id, Principal principal){
        userService.followUser(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User added to followings successfully"));
    }

    @Operation(
            summary = "Unfollow a User",
            description = "Remove a user from the followings list of the logged-in user. The Principal parameter is used to identify the currently authenticated user.",
            parameters = @Parameter(
                    name = "userId",
                    description = "Unique identifier of the user to unfollow",
                    required = true,
                    schema = @Schema(type = "integer", format = "int64", example = "1")
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "User removed from followings successfully", content = @Content(schema = @Schema(implementation = Map.class))),
                    @ApiResponse(responseCode = "401", description = "Unauthorized - User is not authenticated", content = @Content),
                    @ApiResponse(responseCode = "403", description = "Forbidden - User does not have permission to unfollow this user", content = @Content),
                    @ApiResponse(responseCode = "404", description = "User not found", content = @Content)
            }
    )
    @PostMapping("/followings/remove/{userId}")
    public ResponseEntity<?> unfollowUser(@PathVariable("userId") Long id, Principal principal){
        userService.unfollowUser(id, principal.getName());
        return ResponseEntity.ok(Map.of("message", "User removed from followings successfully"));
    }

    @Operation(
            summary = "Search Users by Name",
            description = "Find users by their name. Returns a list of users with their basic details.",
            parameters = @Parameter(
                    name = "name",
                    description = "Name of the user to search for",
                    required = true,
                    schema = @Schema(type = "string", example = "John")
            ),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Users found successfully",
                            content = @Content(
                                    schema = @Schema(implementation = UserShortResponseDto[].class),
                                    examples = @ExampleObject(
                                            value = "[{\"id\": 1, \"name\": \"John Doe\", \"userPhotoUrl\": \"https://res.cloudinary.com/demo/image/upload/v1620000000/sample.jpg\"}]"
                                    )
                            )
                    ),
                    @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
                    @ApiResponse(responseCode = "404", description = "No users found", content = @Content)
            }
    )
    @GetMapping("/search")
    public ResponseEntity<?> findUserByName(@RequestParam(name = "name") String name){
        List<UserShortResponseDto> users = userService.findUsersByName(name);
        return ResponseEntity.ok(users);
    }


}
