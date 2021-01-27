package com.netease.homework.utils.userAuthentication;

import com.netease.homework.domain.User;
import com.netease.homework.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class LoginValidateAuthenticationProvider implements AuthenticationProvider {
    @Resource
    private UserService userService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String rawPassword = (String) authentication.getCredentials();
        User user = (User) userService.loadUserByUsername(username);
        if (!user.isEnabled())
            throw new DisabledException("USER[" + username + "] IS NOT ENABLED");
        else if (!user.isAccountNonLocked())
            throw new LockedException("USER[" + username + "] IS LOCKED");
        else if (!user.isAccountNonExpired())
            throw new AccountExpiredException("USER[" + username + "] IS EXPIRED");
        else if (!user.isCredentialsNonExpired())
            throw new CredentialsExpiredException("USER[" + username + "]'S CREDENTIAL IS NOT ENABLED");
        if (!passwordEncoder.matches(rawPassword, user.getPassword()))
            throw new BadCredentialsException("USER[" + username + "] CREDENTIAL WRONG");
        return new UsernamePasswordAuthenticationToken(user, rawPassword, user.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
