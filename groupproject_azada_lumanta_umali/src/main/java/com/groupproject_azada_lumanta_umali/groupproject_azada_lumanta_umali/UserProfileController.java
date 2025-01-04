package com.groupproject_azada_lumanta_umali.groupproject_azada_lumanta_umali;


import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

@Controller
public class UserProfileController {
    
    private List<UserProfile> profiles = new ArrayList<>();

    public UserProfileController() {
        // Adding a test user manually for testing purposes
        UserProfile testUser = new UserProfile();
        testUser.setFullName("John Timothy S. Umali");
        testUser.setEmail("johntimothyumali@email.com");
        testUser.setPassword("password");
        testUser.setGender("Male");
        testUser.setAddress("Libmanan");
        testUser.setBirthDate("2003-04-25");
        profiles.add(testUser);
    }

    // Redirect to the static index.html
    @RequestMapping("/")
    public String index() {
        return "redirect:/index.htm"; // Redirect to the static HTML file
    }
    @GetMapping("/Home")
    public String HomePage() {
        return "Home";  // example.html (located in src/main/resources/templates)
    }

    @GetMapping("/friends")
    public String friendsPage() {
        return "friends";  // example.html (located in src/main/resources/templates)
    }

    @GetMapping("/messages")
    public String messagesPage() {
        return "messages";  // example.html (located in src/main/resources/templates)
    }

    // Get all profiles
    @GetMapping("/api/users")
    @ResponseBody
    public List<UserProfile> getAllProfiles() {
        return profiles;
    }

    // Create a new user profile
    @PostMapping("/api/users")
    @ResponseBody
    public UserProfile createUserProfile(@RequestBody UserProfile userProfile) {
        profiles.add(userProfile);
        return userProfile;
    }

    // Update a user profile
    @PutMapping("/api/users/{email}")
    @ResponseBody
    public ResponseEntity<UserProfile> updateProfile(@PathVariable String email, @RequestBody UserProfile userDetails) {
        // Find the user profile by email
        for (UserProfile userProfile : profiles) {
            if (userProfile.getEmail().equals(email)) {
                // Update the profile fields
                userProfile.setFullName(userDetails.getFullName());
                userProfile.setPassword(userDetails.getPassword());
                userProfile.setGender(userDetails.getGender());
                userProfile.setAddress(userDetails.getAddress());
                userProfile.setBirthDate(userDetails.getBirthDate());
                userProfile.setProfileImage(userDetails.getProfileImage());
    
                // Return the updated user profile
                return ResponseEntity.ok(userProfile);
            }
        }
    
        // If the profile is not found, return a not found status
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                             .body(null);
    }

    // Delete user profile
    @DeleteMapping("/api/users/{id}")
    @ResponseBody
    public String deleteUserProfile(@PathVariable int id) {
        if (id < 0 || id >= profiles.size()) {
            throw new RuntimeException("User not found");
        }
        profiles.remove(id);
        return "User deleted successfully";
    }

    // Login API - Verifies email and password
    @PostMapping("/api/login")
@ResponseBody
public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
    // Loop through profiles to find a matching email and password
    for (UserProfile userProfile : profiles) {
        if (userProfile.getEmail().equals(loginRequest.getEmail())) {
            // Check if passwords are not null and match
            if (userProfile.getPassword() != null && userProfile.getPassword().equals(loginRequest.getPassword())) {
                // Return the user profile if login is successful
                return ResponseEntity.ok(userProfile);
            } else {
                // Password is invalid
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                                     .body(new ErrorResponse("Invalid password"));
            }
        }
    }

    // Return an error message if the email is not found
    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                         .body(new ErrorResponse("Invalid email or password"));
}

}
class UserProfile {
    private String fullName;
    private String email;
    private String password;
    private String gender;
    private String address;
    private String birthDate;
    private String profileImage;

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }
}

class LoginRequest {
    private String email;
    private String password;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

class ErrorResponse {
    private String message;

    public ErrorResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

