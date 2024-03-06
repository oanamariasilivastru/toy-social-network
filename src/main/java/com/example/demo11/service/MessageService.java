package com.example.demo11.service;

import com.example.demo11.domain.Message;
import com.example.demo11.domain.Utilizator;
import com.example.demo11.repository.MessageDBRepository;

import java.time.LocalDateTime;
import java.util.List;

public class MessageService {
    private MessageDBRepository messageDBRepository;

    public MessageService(MessageDBRepository messageDBRepository) {
        this.messageDBRepository = messageDBRepository;
    }

}
