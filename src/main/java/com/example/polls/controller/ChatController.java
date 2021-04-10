package com.example.polls.controller;



import com.example.polls.model.chat.ChatMessage;
import com.example.polls.model.chat.ChatNotification;
import com.example.polls.payload.requests.chat.ChatMessageRequest;
import com.example.polls.repository.chat.MessageStatusRepository;
import com.example.polls.service.ChatMessageService;
import com.example.polls.service.ChatRoomService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
public class ChatController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private ChatMessageService chatMessageService;
    @Autowired
    private ChatRoomService chatRoomService;
    @Autowired
    private MessageStatusRepository messageStatusRepository;


    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessageRequest chatMessage) {


        ChatMessage saved = chatMessageService.save(chatMessage);
        messagingTemplate.convertAndSendToUser(
                chatMessage.getRecipientId().toString(),"/queue/messages",
                new ChatNotification(
                        saved.getId(),
                        saved.getSenderId(),
                        saved.getSenderName()));
    }

//    @GetMapping("/messages/{senderId}/{recipientId}/count")
//    public ResponseEntity<Long> countNewMessages(
//            @PathVariable Long senderId,
//            @PathVariable Long recipientId) {
//
//        return ResponseEntity
//                .ok(chatMessageService.countNewMessages(senderId, recipientId));
//    }

    @GetMapping("/messages/{chatId}")
    public ResponseEntity<?> findChatMessages ( @PathVariable Long chatId) {
        return ResponseEntity
                .ok(chatMessageService.findChatMessages(chatId));
    }

    @GetMapping("/messages/{id}")
    @ResponseBody
    public ResponseEntity<?> findMessage ( @PathVariable Long id) {
        return ResponseEntity
                .ok(chatMessageService.findById(id));
    }

    @GetMapping("/messages/status/{id}")
    public ResponseEntity<?> getStatus ( @PathVariable Long id) {
        return ResponseEntity
                .ok(chatMessageService.findById(id).getStatus().getName());
    }
}
