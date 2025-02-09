package com.cherubin.controller;

import com.cherubin.config.JwtProvider;
import com.cherubin.model.User;
import com.cherubin.repository.UserRepository;
import com.cherubin.request.LoginRequest;
import com.cherubin.response.AuthResponse;
import com.cherubin.services.CustomeUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomeUserDetailService customeUserDetailService;

    @Autowired
    private JwtProvider jwtProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public AuthResponse createUser(@RequestBody User user) throws Exception {

        String email = user.getEmail();
        String password = user.getPassword();
        String fullName = user.getFullName();

        User isExisteEmail = userRepository.findByEmail(email);
        if (isExisteEmail != null) {
            throw new Exception("L'email existe deja");
        }

        User createdUser = new User();
        createdUser.setEmail(email);
        createdUser.setPassword(passwordEncoder.encode(password));
        createdUser.setFullName(fullName);

        User savedUser = userRepository.save(createdUser);

        Authentication authentication = new UsernamePasswordAuthenticationToken(email, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setJwt(token);
        res.setMessage("utilisateur cree");
        return res;

    }

    @PostMapping("/signin")
    public AuthResponse signinHandler(@RequestBody LoginRequest loginRequest) throws Exception {

        String username = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Authentication authentication = authenticate(username, password);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String token = jwtProvider.generateToken(authentication);

        AuthResponse res = new AuthResponse();
        res.setJwt(token);
        res.setMessage("utilisateur connecte");
        return res;
    }

    private Authentication authenticate(String username, String password) {

        UserDetails userDetails = customeUserDetailService.loadUserByUsername(username);

        if (userDetails == null) {
            throw new BadCredentialsException("Utilisateur introuvable " + username);
        }

        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Mot de passe incorrect");
        }

        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}
