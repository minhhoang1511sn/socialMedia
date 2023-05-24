package com.social.socialnetwork.Service;

import com.social.socialnetwork.dto.MessageDTO;
import com.social.socialnetwork.model.Message;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface MessageService {
    Collection<MessageDTO> findAllRecentMessages(Long id);

    List<MessageDTO> findConversation(Long userId, Long companionId);

    MessageDTO getRecentMessage(Long id);

    Message postMessage(MessageDTO messageDTO);
}
