package com.dh.userservice.data;


import com.dh.userservice.entities.AppUser;
import com.dh.userservice.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
@RequiredArgsConstructor
public class UsuarioLoader implements ApplicationRunner {

    private final UserService usuarioService;


    @Override
    public void run(ApplicationArguments args) throws Exception {
//        var usuario1 = new AppUser();
//        usuario1.setName("Edi");
//        usuario1.setLast_name("Carv");
//        usuario1.setDni(1457456);
//        usuario1.setEmail("edi@mail.com");
//        usuario1.setPhone(32565655L);
//        usuario1.setPassword("$2a$10$wEM0auiCyLmSLuuNsJGpXemKm2uD5wHWwUixJqUooHEkGXmxJ9jAi");
//        usuario1.setCvu(2316546854L);
//        usuario1.setAlias("edicar");
//        var uDB1 = usuarioService.createUser(usuario1);
//        log.info(uDB1.toString());
//        log.info("Password: edi123");

    }
}
