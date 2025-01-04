package com.groupproject_azada_lumanta_umali.groupproject_azada_lumanta_umali;

import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendController {
    

    // Sample data for users and requests (Replace with database in real apps)
    private static List<User> users = new ArrayList<>(Arrays.asList(
        new User("Olivia", "1995-05-15", "Loves coding and playing video games.", "New York"),
        new User("Mary", "1992-10-20", "Food enthusiast and photographer.", "Los Angeles"),
        new User("Alex", "1990-07-12", "Book lover and nature explorer.", "Chicago"),
        new User("Lisa", "1989-03-25", "Sports fanatic and traveler.", "Miami"),
        new User("Charlie", "1993-02-10", "Tech geek and music lover.", "San Francisco"),
        new User("David", "1988-09-30", "Photography and art enthusiast.", "Seattle"),
        new User("Eva", "1996-01-14", "Fashionista and social media influencer.", "Austin"),
        new User("Frank", "1994-12-05", "Nature lover and environmental activist.", "Portland"),
        new User("Grace", "1991-06-22", "Fitness trainer and health advocate.", "Denver")
    ));

    private static List<String> friends = new ArrayList<>(Arrays.asList("Olivia", "Liza", "Alex"));
    private static List<String> sentRequests = new ArrayList<>(Arrays.asList("Mary"));
    private static List<String> receivedRequests = new ArrayList<>(Arrays.asList("Alice"));

    // Retrieve the lists (Friend Requests, Sent Requests, Friends)
    @GetMapping("/list")
    @ResponseBody
    public FriendLists getLists() {
        return new FriendLists(friends, sentRequests, receivedRequests);
    }

    // Send a Friend Request
    @PostMapping("/sendRequest")
    @ResponseBody
    public String sendFriendRequest(@RequestParam String user) {
        if (!sentRequests.contains(user) && !friends.contains(user)) {
            sentRequests.add(user);
            return "Request sent to " + user;
        } else {
            return "Request already sent or already friends.";
        }
    }

    // Cancel a Friend Request
    @PostMapping("/cancelRequest")
    @ResponseBody
    public String cancelRequest(@RequestParam String user) {
        if (sentRequests.remove(user)) {
            return "Request to " + user + " has been canceled.";
        } else {
            return "Request not found.";
        }
    }

    // Confirm a Friend Request
    @PostMapping("/confirmRequest")
    @ResponseBody
    public String confirmFriendRequest(@RequestParam String user) {
        if (!friends.contains(user)) {
            friends.add(user);
            receivedRequests.remove(user);
            return "You are now friends with " + user;
        } else {
            return "Already friends with " + user;
        }
    }

    // Unfriend a user
    @PostMapping("/unfriend")
    @ResponseBody
    public String unfriend(@RequestParam String user) {
        if (friends.remove(user)) {
            return "You are no longer friends with " + user;
        } else {
            return "User not found in friends list.";
        }
    }

    // View profile of a user
    @GetMapping("/viewProfile")
    @ResponseBody
    public User viewProfile(@RequestParam String user) {
        return users.stream()
                .filter(u -> u.getName().equals(user))
                .findFirst()
                .orElse(null);
    }

    // User class to hold user profile info
    static class User {
        private String name;
        private String birthdate;
        private String bio;
        private String location;

        public User(String name, String birthdate, String bio, String location) {
            this.name = name;
            this.birthdate = birthdate;
            this.bio = bio;
            this.location = location;
        }

        public String getName() {
            return name;
        }

        public String getBirthdate() {
            return birthdate;
        }

        public String getBio() {
            return bio;
        }

        public String getLocation() {
            return location;
        }
    }

    // Class to represent the lists of friends, requests, etc.
    static class FriendLists {
        private List<String> friends;
        private List<String> sentRequests;
        private List<String> receivedRequests;

        public FriendLists(List<String> friends, List<String> sentRequests, List<String> receivedRequests) {
            this.friends = friends;
            this.sentRequests = sentRequests;
            this.receivedRequests = receivedRequests;
        }

        public List<String> getFriends() {
            return friends;
        }

        public List<String> getSentRequests() {
            return sentRequests;
        }

        public List<String> getReceivedRequests() {
            return receivedRequests;
        }
    }
}
