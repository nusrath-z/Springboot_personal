package com.nusrath.ecommerce.dto;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {
    
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
