package com.aimprosoft.glossary.common.sample;

import com.aimprosoft.glossary.common.model.UserRole;
import com.aimprosoft.glossary.common.model.impl.User;
import com.aimprosoft.glossary.common.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateUserListener implements InitializingBean{

    private Logger _logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        _logger.info("Start adding sample user");
        User user = new User();
        user.setEmail("user@example.com");
        user.setPassword("user");
        user.setName("Sample User");
        user.setRole(UserRole.USER);

        userService.addUser(user);

        _logger.info("Sample user has been added successfully");
    }
}
