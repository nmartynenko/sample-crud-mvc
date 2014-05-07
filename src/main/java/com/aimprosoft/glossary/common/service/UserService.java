package com.aimprosoft.glossary.common.service;

import com.aimprosoft.glossary.common.model.UserRole;
import com.aimprosoft.glossary.common.model.impl.User;

public interface UserService extends BaseCrudService<User> {

    User getUserByEmail(String username);

    long countByRole(UserRole role);

}
