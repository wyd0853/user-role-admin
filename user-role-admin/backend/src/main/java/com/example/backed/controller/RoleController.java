package com.example.backed.controller;

import com.example.backed.entity.Role;
import com.example.backed.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/roles")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @GetMapping
    public Map<String, Object> getRoles(
            @RequestParam(required = false) String name,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        List<Role> list = roleService.getRoles(name, pageNum, pageSize);
        int total = roleService.getRoleCount(name);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @PostMapping
    public Role createRole(@RequestBody Role role) {
        return roleService.saveRole(role);
    }

    @PutMapping("/{id}")
    public Role updateRole(@PathVariable Long id, @RequestBody Role role) {
        Role existingRole = roleService.getRoleById(id);
        if (existingRole != null) {
            existingRole.setName(role.getName() != null ? role.getName() : existingRole.getName());
            existingRole.setDescription(role.getDescription() != null ? role.getDescription() : existingRole.getDescription());
            existingRole.setUpdateTime(LocalDateTime.now());  // 更新 updateTime
            return roleService.saveRole(existingRole);
        } else {
            throw new RuntimeException("角色ID不存在" + id);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteRole(@PathVariable Long id) {
        roleService.deleteRole(id);
    }

    @GetMapping("/all")
    public List<Role> getAllRoles() {
        return roleService.getAllRoles();
    }

}
