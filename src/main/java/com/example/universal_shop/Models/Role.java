package com.example.universal_shop.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;


import java.io.Serializable;
import java.util.Set;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "[roles]")
public class Role implements GrantedAuthority, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String userRole;

    @OneToMany(mappedBy = "role", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
    @JsonManagedReference
    private Set<UserRole> userRoles;

    @Override
    public String getAuthority() {
        return getUserRole();
    }
}
