package com.example.backed.service;

import com.example.backed.entity.User;
import com.example.backed.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getUsers(String name, LocalDateTime minCreateTime, LocalDateTime maxCreateTime, int pageNum, int pageSize) {
        PageRequest pageable = PageRequest.of(pageNum - 1, pageSize);
        return userRepository.findUsers(name, minCreateTime, maxCreateTime, pageable);
    }

    public int getUserCount(String name, LocalDateTime minCreateTime, LocalDateTime maxCreateTime) {
        return userRepository.countUsers(name, minCreateTime, maxCreateTime);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public void deleteUsers(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }
}