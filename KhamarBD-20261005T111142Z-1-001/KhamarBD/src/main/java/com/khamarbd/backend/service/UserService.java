package com.khamarbd.backend.service;

import com.khamarbd.backend.dto.UserRequestDto;
import com.khamarbd.backend.entity.User;
import com.khamarbd.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User saveUser(UserRequestDto dto) {
        User user = new User();
        user.setName(dto.getName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());
        user.setPasswordHash(dto.getPassword());
        user.setRole(dto.getPrimaryRole());
        user.setDivision(dto.getDivision());
        user.setDistrict(dto.getDistrict());
        user.setUpazila(dto.getUpazila());
        user.setBusinessName(dto.getBusinessName());
        return userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone).orElse(null);
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }
}
