package com.aimprosoft.glossary.common.persistence;

import com.aimprosoft.glossary.common.model.UserRole;
import com.aimprosoft.glossary.common.model.impl.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserPersistence extends JpaRepository<User, Long>{

    User findByEmail(String email);

    @Query("select count(u) from User u where u.role = :role")
    long countByRole(@Param("role") UserRole role);

}
