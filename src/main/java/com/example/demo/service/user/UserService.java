package com.example.demo.service.user;

import com.example.demo.entity.user.User;
import com.example.demo.repository.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<User> getAll() {
        List<User> list = new ArrayList<>();
        this.userRepository.findAll().forEach(list::add);

        return list;
    }

    public User getById(Long id) {
        return this.userRepository.findById(id).orElse(null);
    }

    public User register(String email, String password) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("Пользователь с таким email уже зарегистрирован");
        }

        User user = new User();
        user.setEmail(email);
        user.setPassword(this.passwordEncoder.encode(password));

        return userRepository.save(user);
    }

    public User login(String email, String password) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new RuntimeException("Пользователь с таким email не найден");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Неверный пароль");
        }

        return user;
    }


    public User update() {
        return this.userRepository.save(null);
    }

    public void delete(Long id) {
        this.userRepository.deleteById(id);
    }
}