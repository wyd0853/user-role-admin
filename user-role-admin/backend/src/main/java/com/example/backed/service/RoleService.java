package com.example.backed.service;

import com.example.backed.entity.Role;
import com.example.backed.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> getRoles(String name, int pageNum, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNum - 1, pageSize);
        if (name != null && !name.isEmpty()) {
            return roleRepository.findByNameContaining(name, pageable);
        } else {
            return roleRepository.findAll(pageable).getContent();
        }
    }

    public int getRoleCount(String name) {
        if (name != null && !name.isEmpty()) {
            return roleRepository.countByNameContaining(name);
        } else {
            return (int) roleRepository.count();
        }
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public Role saveRole(Role role) {
        if (role.getId() == null) { // 新角色，设置创建时间
            role.setCreateTime(LocalDateTime.now());
        }
        role.setUpdateTime(LocalDateTime.now()); // 每次保存都更新更新时间
        return roleRepository.save(role);
    }

    public void deleteRole(Long id) {
        roleRepository.deleteById(id);
    }

    public Role getRoleById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }
}