package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.BadRequestException;
import com.example.exception.RequestConflictException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;
import com.example.service.AccountService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */


@RestController
@RequestMapping
public class SocialMediaController {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private AccountService accountService;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.OK)
    public Account registerUser (@RequestBody Account account) throws BadRequestException, RequestConflictException{

        if(accountService.findByUsername(account.getUsername()) != null){
            throw new RequestConflictException();
        }
        if(account.getUsername().equals("") ||
            account.getPassword().length() < 4)
            {
                throw new BadRequestException();
            }
        return this.accountService.saveAccount(account);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public Account login(@RequestBody Account account) throws UnauthorizedException{
        Account foundAccount = accountService.findByUsername(account.getUsername());
        if(foundAccount == null){
            throw new UnauthorizedException();
        }
        if(!account.getPassword().equals(foundAccount.getPassword())){
            throw new UnauthorizedException();
        }
        return foundAccount;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public Message CreateNewMessage(@RequestBody Message message){
        
    }
}
