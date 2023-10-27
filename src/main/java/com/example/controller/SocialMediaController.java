package com.example.controller;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
import com.example.repository.MessageRepository;
import com.example.service.AccountService;
import com.example.service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private MessageService messageService;

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

    @PostMapping("/messages")
    @ResponseStatus(HttpStatus.OK)
    public Message CreateNewMessage(@RequestBody Message message) throws BadRequestException{
        if(accountService.findByPosted_By(message.getPosted_by()) == null){
            throw new BadRequestException();
        }
        if (message.getMessage_text().equals("") ||
                message.getMessage_text().length() > 255) {
            throw new BadRequestException();
        }
        return messageRepository.save(message);
    }
    @GetMapping("/messages")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Message> getAllMessages(){
        return messageRepository.findAll();
    }
    @GetMapping("/messages/{message_id}")
    @ResponseStatus(HttpStatus.OK)
    public Message getMessageById(@PathVariable("message_id") int id){
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(!messageOptional.isPresent()){
            return null;
        }
        return messageOptional.get();
    }

    @DeleteMapping("/messages/{message_id}")
    @ResponseStatus(HttpStatus.OK)
    public Integer deleteMessageById(@PathVariable("message_id") int id){
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(!messageOptional.isPresent()){
            return null;
        }
        messageRepository.delete(messageOptional.get());
        return 1;
    }

    @PatchMapping("/messages/{message_id}")
    @ResponseStatus(HttpStatus.OK)
    public Integer updateMessageById(@PathVariable("message_id") int id, @RequestBody String messageText)
        throws BadRequestException, JsonProcessingException{
        Optional<Message> messageOptional = messageRepository.findById(id);
        if(!messageOptional.isPresent()){
            throw new BadRequestException();
        }
        ObjectMapper objectMapper = new ObjectMapper();
        Message message = objectMapper.readValue(messageText, Message.class);
        String text = message.getMessage_text();
        if(text.length()==0 || text.length()>255){
            throw new BadRequestException();
        }
        return 1;
    }

    @GetMapping("/accounts/{account_id}/messages")
    @ResponseStatus(HttpStatus.OK)
    public Iterable<Message> getAllMessagesByAccountId(@PathVariable("account_id") int id){
        Optional<Account> accountOptional = accountRepository.findById(id);
        if(!accountOptional.isPresent()){
            return new ArrayList<Message>();
        }
        //Account account = accountOptional.get();
        return messageService.findAllByPostedBy(accountOptional.get());
    }
}
