package com.dh.userservice.service;

import com.dh.userservice.Exceptions.BadRequestException;
import com.dh.userservice.entities.AppUser;
import com.dh.userservice.repository.IUserRepository;

import com.dh.userservice.repository.KeyCloakUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    KeyCloakUserRepository keycloakRepository;

    //search user x email
    public AppUser searchUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public Optional<AppUser> searchUserById(Long id) {
        return userRepository.findById(id);
    }

    public AppUser createUser(AppUser appuser) throws BadRequestException ,IOException{

     AppUser searchedUser = userRepository.findByEmail(appuser.getEmail());
     if(searchedUser!=null){
       throw new BadRequestException("This email is already associated with an account created");
     }else {
         //TODO go and verify the email
         //TODO assign role
         keycloakRepository.createUser(appuser);
         String password = encryptPassword(appuser.getPassword());
         appuser.setPassword(password);
         String alias = createAlias();
         appuser.setAlias(alias);
         userRepository.save(appuser);
         return appuser;
     }

    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }

    private String createAlias() throws IOException {

        List<String> words = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get("user-service/src/main/resources/words.txt"))) {
            words = stream.collect(Collectors.toList());
        }catch (IOException e){
            e.printStackTrace();
        }
        Random random = new Random();
        return words.get(random.nextInt(words.size())) + "." +
                words.get(random.nextInt(words.size())) + "." +
                words.get(random.nextInt(words.size()));
    }
}
