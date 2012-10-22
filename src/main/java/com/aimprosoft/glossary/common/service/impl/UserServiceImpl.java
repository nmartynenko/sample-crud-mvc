package com.aimprosoft.glossary.common.service.impl;

import com.aimprosoft.glossary.common.model.impl.User;
import com.aimprosoft.glossary.common.persistence.UserPersistence;
import com.aimprosoft.glossary.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService{

    @Autowired
    private UserPersistence userPersistence;

    @Override
    public void addUser(User user) {
        userPersistence.save(user);
    }

    @Override
    public User getUserByEmail(String username) {
        return userPersistence.findByEmail(username);
    }
}
