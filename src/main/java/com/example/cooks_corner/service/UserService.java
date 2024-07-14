package com.example.cooks_corner.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.cooks_corner.dto.*;
import com.example.cooks_corner.entity.Image;
import com.example.cooks_corner.entity.Recipe;
import com.example.cooks_corner.entity.User;
import com.example.cooks_corner.exception.EditException;
import com.example.cooks_corner.exception.FollowException;
import com.example.cooks_corner.exception.NotFoundException;
import com.example.cooks_corner.repository.ImageRepository;
import com.example.cooks_corner.repository.RecipeRepository;
import com.example.cooks_corner.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final CloudinaryService cloudinaryService;
    private final ImageRepository imageRepository;

    public MyUserResponseDto findMyAccount(String email){
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
        MyUserResponseDto myUserResponseDto = modelMapper.map(user, MyUserResponseDto.class);
        myUserResponseDto.setCreatedRecipes(convertToDtoList(user.getCreatedRecipes()));
        myUserResponseDto.setSavedRecipes(convertToDtoList(user.getSavedRecipes()));
        myUserResponseDto.setRecipesCount(user.getCreatedRecipes().size());
        myUserResponseDto.setFollowersCount(user.getFollowers().size());
        myUserResponseDto.setFollowingsCount(user.getFollowings().size());
        return myUserResponseDto;
    }

    private List<RecipeShortResponseDto> convertToDtoList(List<Recipe> recipes){
        List<RecipeShortResponseDto> recipeDtos = new ArrayList<>();
        if(recipes != null){
            for(Recipe recipe : recipes){
                RecipeShortResponseDto dto = modelMapper.map(recipe, RecipeShortResponseDto.class);
                dto.setLikesCount(recipe.getUsersWhoLiked().size());
                dto.setSavesCount(recipe.getUsersWhoSaved().size());
                recipeDtos.add(dto);
            }
        }
        return recipeDtos;
    }

    public UserResponseDto findUserAccount(Long id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        UserResponseDto userResponseDto = modelMapper.map(user, UserResponseDto.class);
        userResponseDto.setCreatedRecipes(convertToDtoList(user.getCreatedRecipes()));
        userResponseDto.setRecipesCount(user.getCreatedRecipes().size());
        userResponseDto.setFollowersCount(user.getFollowers().size());
        userResponseDto.setFollowingsCount(user.getFollowings().size());
        return userResponseDto;
    }

    public void editUser(UserEditDto userEditDto, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (userEditDto.getName() != null) {
            user.setName(userEditDto.getName());
        }

        if (userEditDto.getDescription() != null) {
            user.setDescription(userEditDto.getDescription());
        }

        if (userEditDto.getPhoto() != null && !userEditDto.getPhoto().isEmpty()) {
            try {
                Image userImage = new Image();
                userImage.setUrl(cloudinaryService.uploadFile(userEditDto.getPhoto(), "userImages"));
                imageRepository.save(userImage);
                user.setUserPhoto(userImage);
            } catch (Exception e) {
                throw new RuntimeException("Image upload failed: " + e.getMessage());
            }
        }
        userRepository.save(user);
    }

    public void followUser(Long id, String email){
        User userToFollow = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new NotFoundException("User not found"));
        if(user.getId().equals(userToFollow.getId())){
            throw new FollowException("User cannot follow himself");
        }
        if (userToFollow.getFollowers().contains(user)) {
            throw new FollowException("User is already following this user");
        }
        userToFollow.getFollowers().add(user);
        user.getFollowings().add(userToFollow);
        userRepository.save(user);
        userRepository.save(userToFollow);
    }

    public void unfollowUser(Long id, String email){
        User userToFollow = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
        User user = userRepository.findByEmail(email).
                orElseThrow(() -> new NotFoundException("User not found"));
        if(user.getId().equals(userToFollow.getId())){
            throw new FollowException("User cannot unfollow himself");
        }
        if (!userToFollow.getFollowers().contains(user)) {
            throw new FollowException("User is not following this user");
        }
        userToFollow.getFollowers().remove(user);
        user.getFollowings().remove(userToFollow);
        userRepository.save(user);
        userRepository.save(userToFollow);
    }

    public List<UserShortResponseDto> findUsersByName(String name){
        List<UserShortResponseDto> userDtos = new ArrayList<>();
        List<User> users = userRepository.findAllByNameStartsWithIgnoreCase(name);
        for(User user : users){
            UserShortResponseDto dto = modelMapper.map(user, UserShortResponseDto.class);
            userDtos.add(dto);
        }
        return userDtos;
     }
}
