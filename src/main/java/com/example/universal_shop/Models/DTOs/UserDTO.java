package com.example.universal_shop.Models.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String name;
    private String surname;
    private String phone;
    private String email;
    private String password;
}
