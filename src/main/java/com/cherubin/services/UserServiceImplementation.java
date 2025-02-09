package com.cherubin.services;

import com.cherubin.config.JwtProvider;
import com.cherubin.model.User;
import com.cherubin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImplementation implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtProvider jwtProvider;

    @Override
    public User findUserById(Long userId) throws Exception {
        Optional<User> opt = userRepository.findById(userId);

        if (opt.isPresent()) {
            return opt.get();
        }
        throw new Exception("utilisateur introuvable par Id " + userId);
    }

    @Override
    public User findUserByJwt(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);

        if (email == null) {

            throw new Exception("token invalide");
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("utilisateur introuvable par email " + email);
        }
        return user;
    }
}
