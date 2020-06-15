package com.hadarych.pp311.service;

import com.hadarych.pp311.entity.Role;

import java.util.Collection;

public interface RoleService {

    Role addRole(Role role);
    void checkRoleEmpty(String role);
    Role getById(Long id);
    Role getByRole(String role);
    Collection getAllRoles();
    int countRoles(String roleName);
    void deleteRole(Role role);
    void dropTable();

}
