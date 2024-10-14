package com.example.universal_shop.Models.DTOs;

import com.example.universal_shop.Models.Role;
import com.example.universal_shop.Models.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEditDTO {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phone;
    private boolean enabled;
    private boolean locked;
    private long role_id;
    private Set<UserRole> userRoles;
    private List<Role> roles;

    public UserEditDTO(String name, String surname, String email, String phone, boolean enabled, boolean locked,
                      Set<UserRole> userRoles, List<Role> roles) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.phone = phone;
        this.enabled = enabled;
        this.locked = locked;
        this.userRoles = userRoles;
        this.roles = roles;
    }
}
