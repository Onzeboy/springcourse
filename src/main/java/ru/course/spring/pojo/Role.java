package ru.course.spring.pojo;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name ="roleName", nullable = false, unique = true)
    private String roleName;

    @OneToMany(mappedBy = "userRole", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<User> users;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
