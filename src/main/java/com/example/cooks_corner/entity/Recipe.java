package com.example.cooks_corner.entity;

import com.example.cooks_corner.entity.enums.Category;
import com.example.cooks_corner.entity.enums.Difficulty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "recipes")
public class Recipe {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recipe_id")
    private Long id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @ManyToOne
    @JoinColumn(name = "image_id")
    private Image image;
    @Enumerated(EnumType.STRING)
    private Category category;
    @Column(name = "difficulty")
    @Enumerated(EnumType.STRING)
    private Difficulty difficulty;
    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL)
    private List<Ingredient> ingredients;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "cooking_time")
    private String cookingTime;
    @ManyToMany
    @JoinTable(
            name = "user_liked_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> usersWhoLiked;

    @ManyToMany
    @JoinTable(
            name = "user_saved_recipes",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> usersWhoSaved;
}
