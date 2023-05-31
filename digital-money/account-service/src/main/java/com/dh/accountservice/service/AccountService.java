package com.dh.accountservice.service;

import com.dh.accountservice.entities.*;
import com.dh.accountservice.exceptions.BadRequestException;
import com.dh.accountservice.exceptions.ResourceNotFountException;
import com.dh.accountservice.repository.IAccountRepository;
import com.dh.accountservice.repository.feing.ICardFeignRepository;
import com.dh.accountservice.repository.feing.ITransactionFeignRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.module.ResolutionException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class AccountService {
    @Autowired
    IAccountRepository accountRepository;
    @Autowired
    ITransactionFeignRepository transactionFeignRepository;

    @Autowired
    ICardFeignRepository cardFeignRepository;

    public AccountTransactionsDTO findLastTransactionsByAccountId (Long id ) {
        Optional<List<Transaction>> response = transactionFeignRepository.findAllByAccountId(id);
        Account account = accountRepository.findById(id).orElse(null);
        return new AccountTransactionsDTO(account.getId(), account.getAlias(), account.getCvu(), account.getBalance(), response.get());
    }

    public AccountCardsDTO findAllCardsByAccountId (Long id ) {
        Optional<List<Card>> response = cardFeignRepository.findAllByAccountId(id);
        Account account = accountRepository.findById(id).orElse(null);
        return new AccountCardsDTO(account.getId(), account.getAlias(), account.getCvu(), account.getBalance(), response.get());
    }

    public AccountDTO findAccountById (Long id) throws ResourceNotFountException{
        Account account = accountRepository.findById(id).orElse(null);
        if (account == null){
            throw  new ResourceNotFountException( "we don´t found any account with the id :" + id);
        }
        return  new AccountDTO(account.getId(), account.getAlias(), account.getCvu(), account.getBalance(),account.getUser_id());
    }
    public AccountDTO findAccountByUserId (Long userId) throws ResourceNotFountException {
       Optional<Account> account= accountRepository.findByUserId(userId);
       if (account.isPresent()){
           Account extractedAccount = account.get();
           return new AccountDTO(extractedAccount.getId(),extractedAccount.getAlias(),extractedAccount.getCvu(), extractedAccount.getBalance(), extractedAccount.getUser_id());
       }
        throw  new ResourceNotFountException( "we don´t found any account with the user_id :" + userId);

    }
    public AccountDTO createAccount(AccountCreateRequestDTO account) throws IOException, BadRequestException {
        String alias = createAlias();
        Account account1 = new Account(account.getId(),alias,account.getCvu(),0.0, account.getUser_id(),null,null);
        if (accountRepository.findByAlias(alias).isPresent()){
            throw new BadRequestException("We can´t create the user account because the alias it´s already in use");
        } else {
            accountRepository.save(account1);
            return new AccountDTO(account1.getId(), account1.getAlias(), account.getCvu(), account1.getBalance(), account1.getUser_id());
        }

    }
     public AccountDTO patchAccount (Long id ) throws BadRequestException,IOException{
         Optional<Account> accountSearched = accountRepository.findById(id);
         if (accountSearched.isPresent()){
             Account existingAccount = accountSearched.get();
             String alias = createAlias();
           if(findAccountByAlias(alias).isPresent()){
               throw new BadRequestException("We can´t update the user account because the alias it´s already in use");
           }else {
               existingAccount.setAlias(alias);
               accountRepository.save(existingAccount);
               return new AccountDTO(existingAccount.getId(), existingAccount.getAlias(), existingAccount.getCvu(), existingAccount.getBalance(), existingAccount.getUser_id());
           }
         }else{
             throw new BadRequestException("We can´t update the user account because the user don´t exist");
         }
     }
    public Optional<Account> findAccountByAlias (String alias) {
        return accountRepository.findByAlias(alias);
    }

    private String createAlias() throws IOException {

        List<String> words = new ArrayList<>();
        try (Stream<String> stream = Files.lines(Paths.get("account-service/src/main/resources/words.txt"))) {
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
