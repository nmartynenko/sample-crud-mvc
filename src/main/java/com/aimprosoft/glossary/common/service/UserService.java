package com.aimprosoft.glossary.common.service;

import com.aimprosoft.glossary.common.model.impl.User;

public interface UserService {

    void addUser(User user);

    User getUserByEmail(String username);

}
