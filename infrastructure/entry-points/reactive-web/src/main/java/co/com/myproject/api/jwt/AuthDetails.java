package co.com.myproject.api.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.stream.Stream;

@Getter
@Setter
@Component
public class AuthDetails implements UserDetails {

    private String username;
    private String password;
    private Boolean status;
    private String roles;

    public AuthDetails() {}

    public AuthDetails(String username, String password, Boolean status, String roles) {
        this.username = username;
        this.password = password;
        this.status = status;
        this.roles = roles;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.of(roles.split(", "))
                //.map(SimpleGrantedAuthority::new)  Así se puede agregar un authority
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))  //Así se asigna un rol de spring, con el prefijo ROLE_
                .toList();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return status;
    }

    @Override
    public boolean isAccountNonLocked() {
        return status;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return status;
    }

    @Override
    public boolean isEnabled() {
        return status;
    }

    @Override
    public String toString() {
        return "AuthDetails{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", status=" + status +
                ", roles='" + roles + '\'' +
                '}';
    }
}
