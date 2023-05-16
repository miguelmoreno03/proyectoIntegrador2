package com.dh.userservice.service;

import com.dh.userservice.entities.AppUser;
import com.dh.userservice.repository.UserRepository;
import com.dh.userservice.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
       AppUser usuario =  usuarioRepository
               .findOneByEmail(email);
//               .orElseThrow(() -> new UsernameNotFoundException("El usuario con email "+ email + "no existe."));
        return new UserDetailsImpl(usuario);
    }
}
