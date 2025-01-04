package com.groupproject_azada_lumanta_umali.groupproject_azada_lumanta_umali;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {
 
    private List<Message> messages = new ArrayList<>();

    // Endpoint to get all messages for a specific friend
    @GetMapping("/{friendName}")
    public List<Message> getMessages(@PathVariable String friendName) {
        return messages.stream()
                .filter(message -> message.getFriend().equals(friendName))
                .collect(Collectors.toList());
    }

    // Endpoint to send a new message
    @PostMapping("/send")
    public Message sendMessage(@RequestBody Message newMessage) {
        newMessage.setId(UUID.randomUUID().toString());
        newMessage.setTime(new Date().toString());
        messages.add(newMessage);
        return newMessage;
    }

    // Endpoint to edit a message
    @PutMapping("/edit/{messageId}")
    public Message editMessage(@PathVariable String messageId, @RequestBody Message updatedMessage) {
        Message message = findMessageById(messageId);
        if (message != null) {
            message.setText(updatedMessage.getText());
            message.setTime(new Date().toString());
        }
        return message;
    }

    // Endpoint to delete a message
    @DeleteMapping("/delete/{messageId}")
    public String deleteMessage(@PathVariable String messageId) {
        Message message = findMessageById(messageId);
        if (message != null) {
            messages.remove(message);
            return "Message deleted successfully";
        }
        return "Message not found";
    }

    // Helper method to find a message by ID
    private Message findMessageById(String messageId) {
        return messages.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .orElse(null);
    }

    // Message class to represent a message object
    public static class Message {
        private String id;
        private String text;
        private String friend;
        private String status;
        private String time;
        private String file;

        // Getters and setters
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public String getFriend() {
            return friend;
        }

        public void setFriend(String friend) {
            this.friend = friend;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getFile() {
            return file;
        }

        public void setFile(String file) {
            this.file = file;
        }
    }
}