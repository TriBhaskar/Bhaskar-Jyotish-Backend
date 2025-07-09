package com.anterka.bjyotish.service;

import com.anterka.bjyotish.dao.BjyotishUserJdbcRepository;
import com.anterka.bjyotish.dao.BjyotishUserRepository;
import com.anterka.bjyotish.entities.BjyotishUserRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class BjyotishUserService implements UserDetailsService {

    private final BjyotishUserJdbcRepository userRepository;
    /**
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BjyotishUserRecord user = userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User " + username + " does not exist"));

        return new User(user.email(), user.passwordHash(),
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.role().name())));
    }
}
