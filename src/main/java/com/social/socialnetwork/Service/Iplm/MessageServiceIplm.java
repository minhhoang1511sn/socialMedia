package com.social.socialnetwork.Service.Iplm;

import com.social.socialnetwork.Service.MessageService;
import com.social.socialnetwork.Service.UserService;
import com.social.socialnetwork.dto.MessageDTO;
import com.social.socialnetwork.model.*;
import com.social.socialnetwork.repository.MessageRepository;
import com.social.socialnetwork.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class MessageServiceIplm implements MessageService {
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    @Override
    public Collection<MessageDTO> findAllRecentMessages(Long id) {
        Iterable<Message> all = messageRepository.findAllRecentMessages(id);
        Map<User, MessageDTO> map = new HashMap<>();
        all.forEach(m -> {
            MessageDTO messageDTO = modelMapper.map(m,MessageDTO.class);
            User user = m.getSender().getId().equals(id) ? m.getReceiver() : m.getSender();
            map.put(user, messageDTO);
        });
        return map.values();
    }

    @Override
    public List<MessageDTO> findConversation(Long userId, Long companionId) {
        List<Message> all = messageRepository.findConversation(userId, companionId);
        List<MessageDTO> messages = new LinkedList<>();
        all.forEach(m -> messages.add(modelMapper.map(m,MessageDTO.class)));
        return messages;
    }

    @Override
    public MessageDTO getRecentMessage(Long id) {
        Message message = messageRepository.findFirstBySenderIdOrReceiverIdOrderByIdDesc(id, id);
        MessageDTO messageDTO = modelMapper.map(message,MessageDTO.class);
        return messageDTO;
    }


    @Override
    public Message postMessage(MessageDTO messageDTO) {
        User sender = userService.getCurrentUser();
        User receiver = userRepository.findUserById(messageDTO.getReceiver().getId());
        Message message = new Message();
        message.setMessage(messageDTO.getMessage());
        message.setReceiver(receiver);
        message.setSender(sender);
        message.setCreateTime(new Date());
        messageRepository.save(message);
        return message;
    }
}
