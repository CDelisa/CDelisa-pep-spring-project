package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException.BadRequest;

import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account saveAccount(Account account){
        return accountRepository.save(account);
    }
    public Account findByUsername(String username){
        var accountOptional = accountRepository.findByUsername(username);
        if(!accountOptional.isPresent()){
            return null;
        }
        return accountOptional.get();
    }
    public Account findByPosted_By(int accountId){
        var accountOptional = accountRepository.findById(accountId);
        if(!accountOptional.isPresent()){
            return null;
        }
        return accountOptional.get();
    }
}
