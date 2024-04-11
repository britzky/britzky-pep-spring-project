package com.example.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.exception.AuthenticationException;
import com.example.exception.ClientErrorException;
import com.example.exception.DuplicateUsernameException;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */

@RestController
@RequestMapping
public class SocialMediaController {

    private final AccountService accountService;
    private final MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService){
        this.accountService = accountService;
        this.messageService = messageService;
    }

    @ExceptionHandler(ClientErrorException.class)
    public ResponseEntity<String> handleClientErrorException(ClientErrorException ex){
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(ex.getMessage());
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex){
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            .body(ex.getMessage());
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<String> handleDuplicateUsernameException(DuplicateUsernameException ex){
        System.out.println(ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(ex.getMessage());
    }

    @PostMapping("register")
    public ResponseEntity<String> register(@RequestBody Account account){
        System.out.println("User attempting to register " + account);
        accountService.register(account);
        return ResponseEntity.status(HttpStatus.OK)
            .body("User successfully registered. " + account);
    }

    @PostMapping("login")
    public ResponseEntity<Account> login(@RequestBody Account account){
        System.out.println("User attempting to log in " + account);
        Account existingAccount = accountService.login(account);
        return ResponseEntity.status(HttpStatus.OK)
            .body(existingAccount);
    }

    @PostMapping("messages")
    public ResponseEntity<Message> createMessage(@RequestBody Message message){
        System.out.println("User is attemting to create this message: " + message);
        Message createdMessage = messageService.createMessage(message);
        return ResponseEntity.status(HttpStatus.OK)
            .body(createdMessage);
    }

    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessages(){
        List<Message> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages); 
    }

    @GetMapping("/messages/{message_id}")
    public ResponseEntity<Message> getMessageById(@PathVariable int message_id){
        Message message = messageService.getMessageById(message_id);
        return ResponseEntity.ok(message);
    }

    @DeleteMapping("/messages/{message_id}")
    public ResponseEntity<Object> deleteMessageById(@PathVariable int message_id){
        Object result = messageService.deleteMessageById(message_id);
        return ResponseEntity.ok(result);
    }

    @PatchMapping("/messages/{message_id}")
    public ResponseEntity<Integer> patchMessageById(@PathVariable int message_id, @RequestBody Message message){
        System.out.println(message);
        int rowsAffected = messageService.patchMessageById(message_id, message.getMessage_text()); 
        return ResponseEntity.ok(rowsAffected);
    }

    @GetMapping("/accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getMessagesByUserId(@PathVariable int account_id){
        List<Message> messages = messageService.getMessagesByUserId(account_id);
        return ResponseEntity.ok(messages);
    }

    

}
