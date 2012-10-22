package com.aimprosoft.glossary.common.security;

import com.aimprosoft.glossary.common.model.impl.User;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class GlossaryUserDetails extends org.springframework.security.core.userdetails.User {

    private User user;

    public GlossaryUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public GlossaryUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
