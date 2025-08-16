package com.aimprosoft.glossary.common.security;

import com.aimprosoft.glossary.common.model.UserRole;
import com.aimprosoft.glossary.common.model.impl.User;
import com.aimprosoft.glossary.common.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class GlossaryUserDetailsService implements UserDetailsService{

    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userService.getUserByEmail(username);

        if (user == null){
            throw new UsernameNotFoundException("User not found: " + username);
        }

        GlossaryUserDetails userDetails = new GlossaryUserDetails(
                username, user.getPassword(), getGrantedAuthorities(user)
        );

        //set actual DB user for possible further purposes
        userDetails.setUser(user);

        return userDetails;
    }


    private Collection<? extends GrantedAuthority> getGrantedAuthorities(User user) {
        List<UserRole> userRoles = new ArrayList<UserRole>(UserRole.values().length);

        //consider, that every user has USER role
        userRoles.add(UserRole.USER);

        if (UserRole.ADMIN.equals(user.getRole())){
            userRoles.add(UserRole.ADMIN);
        }

        List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>();

        for (UserRole authority: userRoles){
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.name()));
        }

        return grantedAuthorities;
    }
}
