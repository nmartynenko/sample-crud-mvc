package com.aimprosoft.glossary.common.persistence;

import com.aimprosoft.glossary.common.model.impl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface UserPersistence extends JpaRepository<User, Long>{

    User findByEmail(String email);

}
