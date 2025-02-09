package com.cherubin.services;

import com.cherubin.model.User;

public interface UserService {
    public User findUserById(Long userId) throws Exception;

    public User findUserByJwt(String jwt) throws Exception;
}
