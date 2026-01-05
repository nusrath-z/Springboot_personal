package com.nusrath.ecommerce.dto;

import com.nusrath.ecommerce.model.Role;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailResponse {

    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    
}
