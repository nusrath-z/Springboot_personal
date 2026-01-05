package com.nusrath.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;
    
    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;


    /**Using Lombok's @Data, @NoArgsConstructor and @AllArgsConstructor annotation to
     * generate boilerplate code **/

    // public User() {}

    // public User(String name, String email, String password) {
    //     this.name = name;
    //     this.email = email;
    //     this.password = password;
    // }

    // public Long getId() { return id; }
    // public String getName() { return name; }
    // public void setName(String name) { this.name = name; }
    // public String getEmail() { return email; }
    // public void setEmail(String email) { this.email = email; }
    // public String getPassword() { return password; }
    // public void setPassword(String password) { this.password = password; }


}
