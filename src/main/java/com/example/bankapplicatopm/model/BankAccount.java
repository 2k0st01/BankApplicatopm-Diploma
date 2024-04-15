package com.example.bankapplicatopm.model;

import com.example.bankapplicatopm.role.AccountType;
import com.example.bankapplicatopm.role.UserRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.Collections;

@Entity
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class BankAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "entity_seq_gen")
    @SequenceGenerator(name = "entity_seq_gen", sequenceName = "entity_seq", initialValue = 10000000, allocationSize = 1)
    private Long id;
    private String IBAN;
    private String specialName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;
    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    @Column(nullable = false)
    private String firstName;
    @Column(nullable = false)
    private String lastName;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private String address;

    private Boolean locked = false;
    private Boolean enabled = false;
    private Boolean phoneValid = false;

    public BankAccount(String firstName,
                       String lastName,
                       String email,
                       String password,
                       String phone,
                       LocalDate dateOfBirth,
                       UserRole userRole,
                       AccountType accountType) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.userRole = userRole;
        this.accountType = accountType;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority(userRole.name());
        return Collections.singletonList(authority);
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return !locked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }
}
