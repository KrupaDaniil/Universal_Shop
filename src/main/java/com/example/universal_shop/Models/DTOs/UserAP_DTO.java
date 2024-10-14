package com.example.universal_shop.Models.DTOs;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserAP_DTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private boolean enabled;
    private boolean locked;
    private long role_id;

    public UserAP_DTO(String name, String surname, String email, String phone, boolean enabled, boolean locked) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.enabled = enabled;
        this.locked = locked;
    }

}
