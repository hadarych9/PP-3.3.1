package com.hadarych.pp311.service.impl;

import com.hadarych.pp311.entity.Role;
import com.hadarych.pp311.repository.RoleRepository;
import com.hadarych.pp311.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    RoleRepository repository;

    @Override
    public Role addRole(Role role) {
        return repository.saveAndFlush(role);
    }

    @Override
    public void checkRoleEmpty(String role) {
        if(repository.getByRole(role) == null){
            String roleName = "";
            if(role.equals("admin")) roleName = "Администратор";
            else if (role.equals("user")) roleName = "Пользователь";
            repository.saveAndFlush(new Role(role, roleName));
        }
    }

    @Override
    public Role getById(Long id) {
        return repository.getOne(id);
    }

    @Override
    public Role getByRole(String role) {
        checkRoleEmpty(role);
        return repository.getByRole(role);
    }

    @Override
    public Collection getAllRoles() {
        return repository.findAll();
    }

    @Override
    public int countRoles(String roleName) {
        return repository.getByRole(roleName).getUsers().size();
    }

    @Override
    public void deleteRole(Role role) {
        repository.delete(role);
    }

    @Override
    public void dropTable() {
        repository.deleteAll();
    }
}
