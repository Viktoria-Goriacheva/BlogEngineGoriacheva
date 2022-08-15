package main.repository;

import java.util.Set;
import java.util.stream.Collectors;
import main.model.Permission;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public enum Role {
  USER(Set.of(Permission.USER)),
  MODERATOR(Set.of(Permission.USER, Permission.MODERATE));

  private final Set<Permission> permissions;

  Role(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  public Set<Permission> getPermissions() {
    return permissions;
  }

  public Set<SimpleGrantedAuthority> getAutorities() {
    return permissions.stream()
        .map(p -> new SimpleGrantedAuthority(p.getPermission()))
        .collect(Collectors.toSet());
  }
}
