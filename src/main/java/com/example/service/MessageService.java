package com.example.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.exception.ClientErrorException;
import com.example.repository.AccountRepository;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private AccountRepository accountRepository;
    
    public Message createMessage(Message message) throws ClientErrorException {
        if (message.getMessage_text().length() < 1){
            throw new ClientErrorException("Message cannot be empty.");
        } 
        if (message.getMessage_text().length() > 255) {
            throw new ClientErrorException("Message cannot be longer than 255 characters.");
        }
        if (accountRepository.findById(message.getPosted_by()).isEmpty()){
            throw new ClientErrorException("User does not exist.");
        }
        return messageRepository.save(message);
    }

    public List<Message> getAllMessages(){
        return messageRepository.findAll();
    }

    public Message getMessageById(int id){
        Message message = messageRepository.findById(id).orElse(null);
        return message;
    }

    public List<Message> getMessagesByUserId(int id){
        return messageRepository.getMessagesByUserId(id);
    }

    @Transactional
    public Object deleteMessageById(int id){
        int rowsAffected = messageRepository.deleteMessageById(id);
        System.out.println("Rows affected in service: " + rowsAffected);
        if (rowsAffected > 0){
            return rowsAffected;
        }
        return "";
    }

    @Transactional
    public int patchMessageById(int id, String messageText) throws ClientErrorException {
        Message message = messageRepository.findById(id).orElse(null);
        if (message == null) {
            throw new ClientErrorException("Message does not exist");
        }
        if (messageText.length() < 1 || messageText.length() > 255){
            throw new ClientErrorException("Message must be between 1 and 255 characters");
        }
        return messageRepository.updateMessageById(id, messageText);
    }
}
