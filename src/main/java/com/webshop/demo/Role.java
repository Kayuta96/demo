package com.webshop.demo;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;  // T.ex. ROLE_USER eller ROLE_ADMIN

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    // Standardkonstruktör
    public Role() {}

    // Konstruktör för att skapa en roll med namn
    public Role(String name) {
        this.name = name;
    }

    // Getter och Setter för id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter och Setter för rollens namn
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter och Setter för användare
    public Set<User> getUsers() {
        return users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }
}