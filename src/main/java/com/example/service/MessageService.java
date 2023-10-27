package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    public Message CreateMessage(Message message){
        return messageRepository.save(message);
    }

    public Iterable<Message> findAllByPostedBy(Account account){
        return messageRepository.findAllByPosted_By(account.getAccount_id());
    }
}
