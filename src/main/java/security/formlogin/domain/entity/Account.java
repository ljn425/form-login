package security.formlogin.domain.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@ToString(exclude = {"userRoles"})
@Builder
@EqualsAndHashCode(of = "id")
@NoArgsConstructor
@AllArgsConstructor
public class Account implements Serializable {

    @Id @GeneratedValue
//    @Column(name = "account_id")
    private Long id;
    private String username;
    private String password;
    private String email;
    private int age;

    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.ALL})
    @JoinTable(name = "account_roles", joinColumns = { @JoinColumn(name = "account_id")}, inverseJoinColumns = {@JoinColumn(name= "role_id")})
    private Set<Role> userRoles = new HashSet<>();
    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateUserRoles(Set<Role> roles) {
        userRoles = roles;
    }
}
