package com.dh.accountservice.service;

import com.dh.accountservice.entities.*;
import com.dh.accountservice.exceptions.BadRequestException;
import com.dh.accountservice.exceptions.ConflictException;
import com.dh.accountservice.exceptions.ResourceNotFountException;
import com.dh.accountservice.repository.IAccountRepository;
import com.dh.accountservice.repository.feing.ICardFeignRepository;
import com.dh.accountservice.repository.feing.ITransactionFeignRepository;

import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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


    public ResponseEntity<Card> saveCardForAccount(Long accountId, CardCreateDTO cardCreateDTO) throws BadRequestException, ConflictException {
       Optional<Account> account = accountRepository.findById(accountId);
       if (account.isPresent()){
           Card card = new Card();
           card.setType(cardCreateDTO.getType());
           card.setBalance(cardCreateDTO.getBalance());
           card.setAccountId(accountId);
           card.setCardNumber(cardCreateDTO.getCardNumber());
           card.setAccountHolder(cardCreateDTO.getAccountHolder());
           card.setExpireDate(cardCreateDTO.getExpireDate());
           card.setBankEntity(cardCreateDTO.getBankEntity());
           try{
               return cardFeignRepository.saveCard(card);
           }catch (FeignException.Conflict e){
               throw new ConflictException(e.getMessage());
           }catch (FeignException.BadRequest e){
               throw new BadRequestException(e.getMessage());
           }

       }else{
           throw new BadRequestException("you are trying to create a card and associate it from a non-existent account");
       }

    }
    @Transactional
    public Transaction createTransaction(CreateTransactionDTO transactionRequest,Long id) throws BadRequestException {
      Optional<Account> userAccount = accountRepository.findById(id);
      if (userAccount.isEmpty()){
          throw new BadRequestException("You´re trying to create a transaction from a  non-existent account. ");
      }else{
          if (userAccount.get().getBalance() < transactionRequest.getAmount()){
             throw new BadRequestException("You do not have the necessary money to make the transaction try to add funds and try again later.");
          }else {
              Optional<Account> searchedAccount = accountRepository.findByCvu(transactionRequest.getDestination_cvu());
              userAccount.get().setBalance(userAccount.get().getBalance()- transactionRequest.getAmount());

              searchedAccount.ifPresent(account -> {
                  account.setBalance(account.getBalance() + transactionRequest.getAmount());
                  accountRepository.save(account);
              } );
              accountRepository.save(userAccount.get());

          }
          Transaction transaction = new Transaction();
          transaction.setAccount_id(id);
          transaction.setDescription(transactionRequest.getDescription());
          transaction.setAmount(transactionRequest.getAmount());
          transaction.setDestination_cvu(transactionRequest.getDestination_cvu());
          transaction.setOrigin_cvu(userAccount.get().getCvu());
          transaction.setType(transactionRequest.getType());
          try{
             return transactionFeignRepository.createTransaction(transaction);
          } catch (FeignException.BadRequest ex){
              throw new BadRequestException(ex.getMessage());
          }catch (InternalError ex ){
              throw new BadRequestException("We have problems with the transaction-service try later. ");
          }

      }
    }

    public AccountTransactionsDTO findLastTransactionsByAccountId (Long id ) throws ResourceNotFountException {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            Account extractecAccount = account.get();
            try{
                Optional<List<Transaction>> transactions =  transactionFeignRepository.findAllByAccountId(id);
                return new AccountTransactionsDTO(extractecAccount.getId(), extractecAccount.getAlias(),extractecAccount.getCvu(), extractecAccount.getBalance(), transactions.get());
            }catch (FeignException.NotFound | FeignException.InternalServerError e) {
                throw new ResourceNotFountException(e.getMessage());
            }
        } else {
            throw new ResourceNotFountException("We don't found any userAccount with the id: " + id);
        }
    }
    public AccountTransactionsDTO findTransactionsByAccountId (Long id ) throws ResourceNotFountException {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            Account extractecAccount = account.get();
            try{
                Optional<List<Transaction>> transactions = transactionFeignRepository.findTransactionsByAccountId(id);
                return new AccountTransactionsDTO(extractecAccount.getId(), extractecAccount.getAlias(),extractecAccount.getCvu(), extractecAccount.getBalance(), transactions.get());
            }catch (FeignException.NotFound | FeignException.InternalServerError e) {
                throw new ResourceNotFountException(e.getMessage());
            }
        } else {
            throw new ResourceNotFountException("We don't found any userAccount with the id: " + id);
        }
    }
    public AccountTransactionsDTO findTransactionsByAccountIdAndRange (Long id ,Double rangeA,Double rangeB) throws ResourceNotFountException {
        Optional<Account> account = accountRepository.findById(id);
        if(account.isPresent()){
            Account extractecAccount = account.get();
            try{
                Optional<List<Transaction>> transactions = transactionFeignRepository.findTransactionsByAccountIdAndRange(id, rangeA, rangeB);
                return new AccountTransactionsDTO(extractecAccount.getId(), extractecAccount.getAlias(),extractecAccount.getCvu(), extractecAccount.getBalance(), transactions.get());
            }catch (FeignException.NotFound | FeignException.InternalServerError e) {
                throw new ResourceNotFountException(e.getMessage());
            }
        } else {
            throw new ResourceNotFountException("We don't found any userAccount with the id: " + id);
        }
    }

    public AccountCardsDTO findAllCardsByAccountId (Long id ) throws ResourceNotFountException {
       Optional<Account> account = accountRepository.findById(id);
       if(account.isPresent()){
           Account extractecAccount = account.get();
           try{
               Optional<List<Card>> cards = cardFeignRepository.findAllByAccountId(id);
               return new AccountCardsDTO(extractecAccount.getId(), extractecAccount.getAlias(),extractecAccount.getCvu(), extractecAccount.getBalance(), cards.get());
           }catch (FeignException.NotFound | FeignException.InternalServerError e) {
               throw new ResourceNotFountException(e.getMessage());
           }
       } else {
           throw new ResourceNotFountException("We don't found any userAccount with the id: " + id);
       }

    }

    public AccountsCardDTO findAccountWithCardById(Long accountId, Long cardId) throws ResourceNotFountException {
    Optional<Account> account = accountRepository.findById(accountId);
    if (account.isPresent()){
        Account extractedAccount = account.get();
        try{
            Optional<Card> card = cardFeignRepository.findCardById(cardId);
            return new AccountsCardDTO(extractedAccount.getId(), extractedAccount.getAlias(), extractedAccount.getCvu(), extractedAccount.getBalance(), card.get());
        }catch (FeignException.NotFound | FeignException.InternalServerError e) {
            throw new ResourceNotFountException(e.getMessage());
        }
    } else {
        throw new ResourceNotFountException("We don't found any userAccount with the id: " + accountId);
    }
    }

    public AccountDTO findAccountById (Long id) throws ResourceNotFountException{
        Optional<Account> account= accountRepository.findById(id);
        if (account.isPresent()){
            Account extractedAccount = account.get();
            return  new AccountDTO(extractedAccount.getId(),extractedAccount.getAlias(), extractedAccount.getCvu(),extractedAccount.getBalance(),extractedAccount.getUser_id());
        }
       throw new ResourceNotFountException("we don´t found any account with the id : "+ id);
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
        if (!isValidCvuLength(account.getCvu())){
            throw new BadRequestException("We cannot create the user account because the length of the cvu is not correct");
        }
        if(!validNumeric(account.getCvu())){
            throw new BadRequestException("Invalid CVU format. The CVU must contain only numeric characters.");
        }
        String alias = createAlias();
        Account account1 = new Account(account.getId(),alias,account.getCvu(),0.0, account.getUser_id(),null,null);
        if (accountRepository.findByAlias(alias).isPresent()){
            throw new BadRequestException("We can´t create the user account because the alias it´s already in use");
        } else {
           Optional<Account> accountOptional = accountRepository.findByUserId(account.getUser_id());
           if (accountOptional.isPresent()){
               throw new BadRequestException("We can´t create the user account because the user associated in the account its already in use ");

           }
            Optional<Account> accountOptionalByCvu=accountRepository.findByCvu(account.getCvu());
            if (accountOptionalByCvu.isPresent()){
                throw new BadRequestException("We can´t create the user account because the cvu associated in the account its already in use ");
            }
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
   public byte[] generateReceipt (Long transactionId,Long accountId) throws  BadRequestException {
        Optional<Account> searchedAccount = accountRepository.findById(accountId);
        if (searchedAccount.isPresent()){
            try{
                Optional<Transaction> transaction = transactionFeignRepository.findTransactionById(transactionId);
                if(transaction.get().getAccount_id().equals(accountId)){
                    return transactionFeignRepository.generateReceipt(transactionId);
                } else {
                    throw new BadRequestException("you are trying to generate a receipt for a transaction that is not from your account");
                }
            }catch (FeignException.NotFound ex){
                throw new BadRequestException(ex.getMessage());
            }


        }else {
            throw new BadRequestException("You´re trying to get a receipt from a transaction from a non-existence account");
        }
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
    private boolean isValidCvuLength(String cvu){
        return cvu.length() == 22;
    }
    private boolean validNumeric(String cvu){
        return cvu.matches("\\d+");
    }

}
