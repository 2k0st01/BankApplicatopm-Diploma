package com.example.bankapplicatopm.model;

import com.example.bankapplicatopm.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javax.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.time.LocalDate;
import java.util.*;

@Entity
@Data
@NoArgsConstructor
public class BankAccount implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_account_seq_gen")
    @SequenceGenerator(name = "bank_account_seq_gen", sequenceName = "bank_account_seq", initialValue = 10000000, allocationSize = 1    )
    private Long id;
    private String IBAN;
    private String specialName;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

//    @Column(nullable = false)
    private String firstName;
//    @Column(nullable = false)
    private String lastName;
//    @Column(nullable = false)
    private String email;
//    @Column(nullable = false)
    private String password;
//    @Column(nullable = false)
    private String phone;
//    @Column(nullable = false)
    private LocalDate dateOfBirth;

    private String address;

    private Boolean locked = false;
    private Boolean enabled = false;
//    private Boolean phoneValid = false;
//    private boolean mFaEnabled = false;
//    private String secret;


    @OneToMany(mappedBy = "account")
    @JsonManagedReference
    private List<Wallet> wallets = new ArrayList<>();

    public BankAccount(String firstName,
                       String lastName,
                       String email,
                       String password,
                       String phone,
                       LocalDate dateOfBirth,
                       UserRole userRole) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.userRole = userRole;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankAccount that = (BankAccount) o;
        return Objects.equals(id, that.id) && Objects.equals(IBAN, that.IBAN) && Objects.equals(specialName, that.specialName) && userRole == that.userRole && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(password, that.password) && Objects.equals(phone, that.phone) && Objects.equals(dateOfBirth, that.dateOfBirth) && Objects.equals(address, that.address) && Objects.equals(locked, that.locked) && Objects.equals(enabled, that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, IBAN, specialName, userRole, firstName, lastName, email, password, phone, dateOfBirth, address, locked, enabled);
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
