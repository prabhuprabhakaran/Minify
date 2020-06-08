package com.github.prabhuprabhakaran.minify.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.prabhuprabhakaran.minify.utils.Utils;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import static java.util.stream.Collectors.toList;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Users implements UserDetails {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    Long id;

    @NotEmpty
    private String username;

    @NotEmpty
    private String password;

    @Size(max = 2147483647)
    @Column(name = "active")
    String active;

    @Size(max = 2147483647)
    @Column(name = "createdby")
    String createdBy;

    @Size(max = 2147483647)
    @Column(name = "updatedby")
    String updatedBy;

    @Column(name = "createdat")
    Timestamp createdAt;

    @Column(name = "updatedat")
    Timestamp updatedAt;

    @Version
    private Integer version;

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private List<String> roles = new ArrayList<>();

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.roles.stream().map(SimpleGrantedAuthority::new).collect(toList());
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @PrePersist
    private void prePersistFunction() {
        active = "Y";
        createdBy = Utils.getUserPrincipal();
        createdAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    public void preUpdateFunction() {
        updatedBy = Utils.getUserPrincipal();
        updatedAt = Timestamp.from(Instant.now());
    }

}
