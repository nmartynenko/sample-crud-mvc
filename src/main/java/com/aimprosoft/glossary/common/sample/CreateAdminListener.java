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
public class CreateAdminListener implements InitializingBean{

    private Logger _logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Override
    public void afterPropertiesSet() throws Exception {
        if (userService.countByRole(UserRole.ADMIN) == 0){
            _logger.info("Start adding sample admin");
            User user = new User();
            user.setEmail("admin@example.com");
            user.setPassword("admin");
            user.setName("Sample Admin");
            user.setRole(UserRole.ADMIN);

            userService.add(user);

            _logger.info("Sample admin has been added successfully");
        }
    }
}
