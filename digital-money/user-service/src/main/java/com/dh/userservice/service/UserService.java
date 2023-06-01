package com.dh.userservice.service;

import com.dh.userservice.entities.*;
import com.dh.userservice.exceptions.BadRequestException;
import com.dh.userservice.exceptions.ResourceNotFountException;
import com.dh.userservice.repository.IUserRepository;

import com.dh.userservice.repository.KeyCloakUserRepository;
import com.dh.userservice.repository.feing.IAccountFeignRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;


@Service
public class UserService {
    @Autowired
    IUserRepository userRepository;
    @Autowired
    IAccountFeignRepository accountFeignRepository;
    @Autowired
    KeyCloakUserRepository keycloakRepository;

    //search user x email
    public AppUser searchUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
    public AppUserResponseDTO searchUserById(Long id) throws ResourceNotFountException {
        Optional<AppUser> user = userRepository.findById(id);
        //todo set account

        if(user.isPresent()){
            AppUser  userResponse = user.get();
            return new AppUserResponseDTO(userResponse.getId(), userResponse.getFirst_name(), userResponse.getLast_name(), userResponse.getDni(), userResponse.getEmail(),userResponse.getPhone());
        }else {
            throw new ResourceNotFountException("We don´t found any user with the id :" + id);
        }
    }
    public AppUserAccountDTO findUserWithAccountInformation (Long userId) throws ResourceNotFountException {
        Optional<AppUser> user = userRepository.findById(userId);
        if (user.isPresent()){
            AppUser extractedUser = user.get();
            Optional<Account> account = accountFeignRepository.findAccountByUserId(userId);
            if (account.isPresent()){
                extractedUser.setAccount(account.get());
            } else {
                extractedUser.setAccount(null);
            }
         return new AppUserAccountDTO(extractedUser.getId(),extractedUser.getFirst_name(),extractedUser.getLast_name(),extractedUser.getDni(),extractedUser.getEmail(), extractedUser.getPhone(),extractedUser.getAccount());
        }else {
            throw new ResourceNotFountException("We don´t found any account with the id :" + userId);
        }
    }



@Transactional
    public AppUserResponseDTO createUser(AppUser appuser) throws BadRequestException ,IOException{

     AppUser searchedUser = userRepository.findByEmail(appuser.getEmail());
     if(searchedUser!=null){
       throw new BadRequestException("This email is already associated with an user created");
     }else {
         //TODO go and verify the email
         //TODO assign role
         keycloakRepository.createUser(appuser);
         String password = encryptPassword(appuser.getPassword());
         appuser.setPassword(password);
         userRepository.save(appuser);
         //TODO SET ACCOUNT OPEN FEIGN
         return new AppUserResponseDTO(appuser.getId(), appuser.getFirst_name(), appuser.getLast_name(), appuser.getDni(), appuser.getEmail(), appuser.getPhone());

     }
    }

    public AppUserResponseDTO patchUser (AppUserDTO user) throws BadRequestException{
       Optional<AppUser> userSearched = userRepository.findById(user.getId());
       if (userSearched.isPresent()){
           AppUser existingUser = userSearched.get();
           keycloakRepository.patchUser(existingUser.getEmail(), user);
           if(user.getDni() != null) {
                  existingUser.setDni(user.getDni());
           }
           if(user.getFirst_name() != null) {
               existingUser.setFirst_name(user.getFirst_name());
           }
           if(user.getLast_name() != null) {
               existingUser.setLast_name(user.getLast_name());
           }
           if(user.getEmail() != null) {
               existingUser.setEmail(user.getEmail());
           }
           if(user.getPhone() != null) {
               existingUser.setPhone(user.getPhone());
           }
           if(user.getPassword() != null) {
               String password = encryptPassword(user.getPassword());
               existingUser.setPassword(password);
           }
           userRepository.save(existingUser);
           return new AppUserResponseDTO(existingUser.getId(), existingUser.getFirst_name(), existingUser.getLast_name(),existingUser.getDni(),existingUser.getEmail(), existingUser.getPhone());
       }else {
           throw new BadRequestException("We can´t update the user because the user don´t exist ");
       }

    }

    private String encryptPassword(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.encode(password);
    }
}
