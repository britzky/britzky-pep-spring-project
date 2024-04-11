package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.AuthenticationException;
import com.example.exception.ClientErrorException;
import com.example.exception.DuplicateUsernameException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    
    @Autowired
    private AccountRepository accountRepository;

    public Account register(Account account){
        if (account.getUsername() == null){
            throw new ClientErrorException("Username cannot be empty");
        }
        if (accountRepository.findByUsername(account.getUsername()).isPresent()){
            throw new DuplicateUsernameException();
        }
        if (account.getPassword().length() < 4){
            throw new ClientErrorException("Password must be at least 4 characters long");
        }
        return accountRepository.save(account);
    }

    public Account login(Account account){
        System.out.println(account);
        return accountRepository.findByUsernameAndPassword(account.getUsername(), account.getPassword())
            .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
    }
}
