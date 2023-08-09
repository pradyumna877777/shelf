package com.example.Shelf.model;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity(name = "app_user")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    private String email;

}