package com.example.backed.controller;

import com.example.backed.entity.User;
import com.example.backed.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public Map<String, Object> getUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime minCreateTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime maxCreateTime,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "10") int pageSize) {

        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime localMinCreateTime = (minCreateTime != null) ? minCreateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalDateTime() : null;
        LocalDateTime localMaxCreateTime = (maxCreateTime != null) ? maxCreateTime.atZone(ZoneId.of("UTC")).withZoneSameInstant(zoneId).toLocalDateTime() : null;

        List<User> list = userService.getUsers(name, localMinCreateTime, localMaxCreateTime, pageNum, pageSize);
        int total = userService.getUserCount(name, localMinCreateTime, localMaxCreateTime);

        System.out.println("Received: " + "(min)" + minCreateTime + "   (max)" + maxCreateTime);
        System.out.println("Transform: " + "(min)" +localMinCreateTime + "   (max)" + localMaxCreateTime);

        Map<String, Object> result = new HashMap<>();
        result.put("list", list);
        result.put("total", total);
        return result;
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        if (user.getStatus() == null) {
            user.setStatus(0);
        }
        if (user.getCreateTime() == null) {
            user.setCreateTime(LocalDateTime.now());
        }
        return userService.saveUser(user);
    }

    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        User existingUser = userService.getUserById(id);

        // 更新现有用户的信息
        existingUser.setUserName(user.getUserName() != null ? user.getUserName() : existingUser.getUserName());
        existingUser.setTrueName(user.getTrueName() != null ? user.getTrueName() : existingUser.getTrueName());
        existingUser.setPassword(user.getPassword() != null ? user.getPassword() : existingUser.getPassword());
        existingUser.setEmail(user.getEmail() != null ? user.getEmail() : existingUser.getEmail());
        existingUser.setGender(user.getGender() != null ? user.getGender() : existingUser.getGender());
        existingUser.setAddress(user.getAddress() != null ? user.getAddress() : existingUser.getAddress());
        existingUser.setIntroduction(user.getIntroduction() != null ? user.getIntroduction() : existingUser.getIntroduction());
        existingUser.setPhone(user.getPhone() != null ? user.getPhone() : existingUser.getPhone());
        // 更新用户角色
        existingUser.setRole(user.getRole());
        // 更新头像信息
        if (user.getAvatarUrl() != null) {
            existingUser.setAvatarUrl(user.getAvatarUrl()); // 处理 Base64 编码数据并存储为 byte[]
        }
        //existingUser.setRoleIds(user.getRoleIds() != null ? user.getRoleIds() : existingUser.getRoleIds());

        // 仅在需要时更新 createTime
        if (user.getCreateTime() != null) {
            existingUser.setCreateTime(user.getCreateTime());
        }
        return userService.saveUser(user);
    }

    @PostMapping("/upload-avatar")
    public ResponseEntity<Map<String, String>> uploadAvatar(@RequestParam("file") MultipartFile file) {
        Map<String, String> response = new HashMap<>();
        if (file.isEmpty()) {
            response.put("message", "文件为空");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        try {
            // 定义保存路径
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path path = Paths.get("uploads/" + fileName);
            // System.out.println("File will be saved to: " + path.toAbsolutePath());
            // 创建目录并保存文件
            Files.createDirectories(path.getParent());
            Files.copy(file.getInputStream(), path);

            response.put("message", "上传成功");
            response.put("url", "/uploads/" + fileName);  // 可以返回文件的访问路径
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            e.printStackTrace();
            response.put("message", "上传失败");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }


    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    @DeleteMapping
    public void deleteUsers(@RequestBody List<Long> ids) {
        userService.deleteUsers(ids);
    }

    @PatchMapping("/{id}")
    public User updateUserStatus(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        User user = userService.getUserById(id);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        if (updates.containsKey("status")) {
            user.setStatus((Integer) updates.get("status")); // 将status从请求体中解析为int类型
        }
        return userService.saveUser(user);
    }
}