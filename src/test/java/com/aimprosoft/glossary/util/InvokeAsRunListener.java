package com.aimprosoft.glossary.util;

import com.aimprosoft.glossary.common.model.UserRole;
import org.junit.runner.Description;
import org.junit.runner.notification.RunListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;

public class InvokeAsRunListener extends RunListener{

    private final Logger _logger = LoggerFactory.getLogger(getClass());

    private final static Authentication ANONYMOUS =
            new AnonymousAuthenticationToken("key", "anonymousUser", AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));

    private final static Authentication USER = createUser(UserRole.USER);

    private final static Authentication ADMIN = createUser(UserRole.ADMIN);

    private static Authentication createUser(UserRole userRole) {
        return new UsernamePasswordAuthenticationToken(
                userRole.name().toLowerCase(),
                null,
                AuthorityUtils.createAuthorityList(userRole.name()));
    }

    private void authenticateAnonymously(){
        authenticate(ANONYMOUS);
    }

    private void authenticateUser(UserRole role) {
        switch (role) {
            case USER :
                authenticate(USER);
                return;
            case ADMIN :
                authenticate(ADMIN);
        }
    }

    private void authenticate(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }


    @Override
    public void testStarted(Description description) throws Exception {
        _logger.debug("Start working on {} method", description.getMethodName());

        InvokeAs annotation = description.getAnnotation(InvokeAs.class);

        if (annotation != null){
            authenticateUser(annotation.value());
        } else {
            authenticateAnonymously();
        }

        _logger.debug("End working on {} method", description.getMethodName());
    }
}