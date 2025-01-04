package com.groupproject_azada_lumanta_umali.groupproject_azada_lumanta_umali;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private List<Message> messages = new ArrayList<>();

    // Endpoint to get all messages for a specific friend with pagination and sorting
    @GetMapping("/{friendName}")
    public List<Message> getMessages(@PathVariable String friendName,
                                     @RequestParam int page, @RequestParam int size,
                                     @RequestParam(required = false, defaultValue = "asc") String sortOrder) {
        return messages.stream()
                .filter(message -> message.getFriend().equals(friendName))
                .sorted((m1, m2) -> sortOrder.equals("desc") ? m2.getTime().compareTo(m1.getTime()) : m1.getTime().compareTo(m2.getTime()))
                .skip(page * size)
                .limit(size)
                .collect(Collectors.toList());
    }

    // Endpoint to search messages by keyword and date range
    @GetMapping("/search")
    public List<Message> searchMessages(@RequestParam String keyword,
                                        @RequestParam(required = false) String startDate,
                                        @RequestParam(required = false) String endDate) {
        Date start = startDate != null ? parseDate(startDate) : null;
        Date end = endDate != null ? parseDate(endDate) : null;
        return messages.stream()
                .filter(message -> message.getText().contains(keyword)
                        && (start == null || message.getTime().compareTo(start.toString()) >= 0)
                        && (end == null || message.getTime().compareTo(end.toString()) <= 0))
                .collect(Collectors.toList());
    }

    // Endpoint to send a new message with validation
    @PostMapping("/send")
    public ResponseEntity<Message> sendMessage(@RequestBody Message newMessage) {
        if (newMessage.getText().isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        newMessage.setId(UUID.randomUUID().toString());
        newMessage.setTime(new Date().toString());
        newMessage.setStatus("unread");
        messages.add(newMessage);
        return ResponseEntity.status(HttpStatus.CREATED).body(newMessage);
    }

    // Endpoint to edit a message
    @PutMapping("/edit/{messageId}")
    public ResponseEntity<Message> editMessage(@PathVariable String messageId, @RequestBody Message updatedMessage) {
        Message message = findMessageById(messageId);
        if (message != null) {
            message.setText(updatedMessage.getText());
            message.setTime(new Date().toString());
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
    }

    // Endpoint to delete a message
    @DeleteMapping("/delete/{messageId}")
    public ResponseEntity<String> deleteMessage(@PathVariable String messageId) {
        Message message = findMessageById(messageId);
        if (message != null) {
            messages.remove(message);
            return ResponseEntity.ok("Message deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found");
    }

    // Endpoint to delete multiple messages
    @DeleteMapping("/delete/batch")
    public ResponseEntity<String> deleteMessages(@RequestBody List<String> messageIds) {
        List<String> deletedMessages = new ArrayList<>();
        for (String messageId : messageIds) {
            Message message = findMessageById(messageId);
            if (message != null) {
                messages.remove(message);
                deletedMessages.add(messageId);
            }
        }
        if (deletedMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No messages found for deletion");
        }
        return ResponseEntity.ok("Deleted messages: " + deletedMessages);
    }

    // Endpoint to update message status (read/unread)
    @PutMapping("/status/{messageId}")
    public ResponseEntity<Message> updateStatus(@PathVariable String messageId, @RequestParam String status) {
        Message message = findMessageById(messageId);
        if (message != null && (status.equals("read") || status.equals("unread"))) {
            message.setStatus(status);
            return ResponseEntity.ok(message);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
    }

    // Endpoint to upload an attachment (file)
    @PostMapping("/upload/{messageId}")
    public ResponseEntity<String> uploadFile(@PathVariable String messageId, @RequestParam("file") MultipartFile file) throws IOException {
        Message message = findMessageById(messageId);
        if (message != null && !file.isEmpty()) {
            message.setFile(file.getOriginalFilename());
            // You can save the file to your server or cloud storage here.
            return ResponseEntity.ok("File uploaded successfully!");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Message not found or file is empty");
    }

    // Endpoint to get unread messages
    @GetMapping("/unread/{friendName}")
    public List<Message> getUnreadMessages(@PathVariable String friendName) {
        return messages.stream()
                .filter(message -> message.getFriend().equals(friendName) && message.getStatus().equals("unread"))
                .collect(Collectors.toList());
    }

    // Endpoint to mark all messages as read for a specific friend
    @PutMapping("/markAllAsRead/{friendName}")
    public ResponseEntity<String> markAllAsRead(@PathVariable String friendName) {
        messages.stream()
                .filter(message -> message.getFriend().equals(friendName))
                .forEach(message -> message.setStatus("read"));
        return ResponseEntity.ok("All messages marked as read for " + friendName);
    }

    // Endpoint to get messages in a specific time range
    @GetMapping("/time-range")
    public List<Message> getMessagesInTimeRange(@RequestParam String startTime, @RequestParam String endTime) {
        Date start = parseDate(startTime);
        Date end = parseDate(endTime);
        return messages.stream()
                .filter(message -> {
                    Date messageTime = parseDate(message.getTime());
                    return messageTime != null && messageTime.compareTo(start) >= 0 && messageTime.compareTo(end) <= 0;
                })
                .collect(Collectors.toList());
    }

    // Helper method to find a message by ID
    private Message findMessageById(String messageId) {
        return messages.stream()
                .filter(message -> message.getId().equals(messageId))
                .findFirst()
                .orElse(null);
    }

    // Helper method to parse date strings to Date objects
    private Date parseDate(String dateStr) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(dateStr);
        } catch (ParseException e) {
            return null;
        }
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

    // Custom exception handler for invalid message operations
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    // Helper method to get all messages (for testing purposes)
    @GetMapping("/all")
    public List<Message> getAllMessages() {
        return messages;
    }

    // Endpoint to retrieve messages by specific status
    @GetMapping("/status/{status}")
    public List<Message> getMessagesByStatus(@PathVariable String status) {
        return messages.stream()
                .filter(message -> message.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    // Additional helper methods for handling large batch operations or complex logic
    @PutMapping("/batch/updateStatus")
    public ResponseEntity<String> batchUpdateStatus(@RequestBody List<String> messageIds, @RequestParam String status) {
        List<String> updatedMessages = new ArrayList<>();
        for (String messageId : messageIds) {
            Message message = findMessageById(messageId);
            if (message != null) {
                message.setStatus(status);
                updatedMessages.add(messageId);
            }
        }
        if (updatedMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No messages found for batch update");
        }
        return ResponseEntity.ok("Batch update successful: " + updatedMessages);
    }

    @DeleteMapping("/batch/delete")
    public ResponseEntity<String> batchDeleteMessages(@RequestBody List<String> messageIds) {
        List<String> deletedMessages = new ArrayList<>();
        for (String messageId : messageIds) {
            Message message = findMessageById(messageId);
            if (message != null) {
                messages.remove(message);
                deletedMessages.add(messageId);
            }
        }
        if (deletedMessages.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No messages found for batch deletion");
        }
        return ResponseEntity.ok("Batch deletion successful: " + deletedMessages);
    }
}
