package sk.tuke.gamestudio.entity;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name = "GameUser")
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username")
        }
)
@NamedQuery(
        name = "GameUser.findByUsername",
        query = "SELECT u FROM GameUser u WHERE u.username = :username"
)
@NamedQuery(
        name = "GameUser.existsByUsername",
        query = "SELECT COUNT(u) FROM GameUser u WHERE u.username = :username"
)
public class User {
  @Id
  @GeneratedValue
  private int ident;
  private String username;
  private String passwordHash;
//  private Set<String> roles = new HashSet<>();
  private String role = "USER";
  private Date createdAt;

  public User() { }
  public User(String username, String passwordHash, String role, Date createdAt) {
    this.username = username;
    this.passwordHash = passwordHash;
    this.role = role;
    this.createdAt = createdAt;
  }
  public User(int ident, String username, String passwordHash, String role, Date createdAt) {
    this.ident = ident;
    this.username = username;
    this.passwordHash = passwordHash;
    this.role = role;
    this.createdAt = createdAt;
  }

  public int getIdent() { return ident; }
  public void setIdent(int ident) { this.ident = ident; }
  public String getUsername() { return username; }
  public void setUsername(String username) { this.username = username; }
  public String getPasswordHash() { return passwordHash; }
  public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
  public String getRole() { return role; }
  public void setRole(String role) { this.role = role; }
  public Date getCreatedAt() { return createdAt; }
  public void setCreatedAt(Date createdAt) { this.createdAt = createdAt; }
}
