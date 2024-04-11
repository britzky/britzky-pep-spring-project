package com.example.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.entity.Message;

@Repository
public interface MessageRepository extends JpaRepository<Message, Integer> {
    @Modifying
    @Query("DELETE FROM Message m WHERE m.message_id = :message_id")
    public int deleteMessageById(@Param("message_id") int id);

    @Modifying
    @Query("UPDATE Message m SET m.message_text = :message_text WHERE m.message_id = :message_id")
    public int updateMessageById(@Param("message_id") int id, @Param("message_text") String messageText);

    @Query("SELECT m FROM Message m WHERE m.posted_by = :accountId")
    public List<Message> getMessagesByUserId(@Param("accountId") int accountId);

}
