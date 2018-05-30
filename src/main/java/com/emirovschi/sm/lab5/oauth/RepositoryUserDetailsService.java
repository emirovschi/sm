package com.emirovschi.sm.lab5.oauth;

import com.emirovschi.sm.lab5.users.models.RoleModel;
import com.emirovschi.sm.lab5.users.UserRepository;
import com.emirovschi.sm.lab5.users.models.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Service
public class RepositoryUserDetailsService implements UserDetailsService
{
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String DEFAULT_ROLE = "USER";

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(final String email) throws UsernameNotFoundException
    {
        return ofNullable(userRepository.findByEmail(email)).map(this::getUser)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found"));
    }

    private UserDetails getUser(final UserModel user)
    {
        return new User(user.getEmail(), user.getPassword(), getAuthorities(user));
    }

    private List<? extends GrantedAuthority> getAuthorities(final UserModel user)
    {
        final List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(ROLE_PREFIX + DEFAULT_ROLE));
        authorities.addAll(getUserAuthorities(user));
        return authorities;
    }

    private List<? extends GrantedAuthority> getUserAuthorities(final UserModel user)
    {
        return user.getRoles().stream()
                .map(RoleModel::getName)
                .map(String::toUpperCase)
                .map(s -> ROLE_PREFIX + s)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }
}
