package com.aimprosoft.glossary.common.service.impl;

import com.aimprosoft.glossary.common.model.UserRole;
import com.aimprosoft.glossary.common.model.impl.User;
import com.aimprosoft.glossary.common.persistence.UserPersistence;
import com.aimprosoft.glossary.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends BaseCrudServiceImpl<User, UserPersistence> implements UserService{

    @Autowired(required = false)
    private PasswordEncoder passwordEncoder = NoOpPasswordEncoder.getInstance();

    @Override
    public void add(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        super.add(user);
    }

    @Override
    public User getUserByEmail(String username) {
        return persistence.findByEmail(username);
    }

    @Override
    public long countByRole(UserRole role) {
        return persistence.countByRole(role);
    }
}
