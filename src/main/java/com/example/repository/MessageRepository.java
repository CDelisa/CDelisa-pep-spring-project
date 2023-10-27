package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import com.example.entity.Message;


public interface MessageRepository extends CrudRepository<Message, Integer> {
    @Query("SELECT m FROM Message m WHERE m.posted_by = :account")
    List<Message> findAllByPosted_By(@Param("account")int account_id);
}
