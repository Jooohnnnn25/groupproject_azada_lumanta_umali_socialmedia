package com.groupproject_azada_lumanta_umali.groupproject_azada_lumanta_umali;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class HomeController {
    
    public static void main(String[] args) {
        SpringApplication.run(HomeController.class, args);
    }

    // Post model class
    public static class Post {
        private int id;
        private String description;
        private String image;
        private String privacy;

        // Getters and setters
        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getPrivacy() {
            return privacy;
        }

        public void setPrivacy(String privacy) {
            this.privacy = privacy;
        }
    }

    @RestController
    @RequestMapping("/api/posts")
    public static class PostController {

        private List<Post> posts = new ArrayList<>();

     
        @GetMapping
        public List<Post> getAllPosts() {
            return posts;
        }

    
        @GetMapping("/{id}")
        public Post getPostById(@PathVariable int id) {
            return posts.stream().filter(post -> post.getId() == id).findFirst().orElse(null);
        }

        // Create a new post
        @PostMapping
        public Post createPost(@RequestBody Post newPost) {
            newPost.setId(posts.size() + 1); // Set a new ID
            posts.add(newPost);
            return newPost;
        }

    
        @DeleteMapping("/{id}")
        public String deletePost(@PathVariable int id) {
            Post post = posts.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
            if (post != null) {
                posts.remove(post);
                return "Post deleted successfully.";
            } else {
                return "Post not found.";
            }
        }

       
        @PutMapping("/{id}")
        public Post updatePost(@PathVariable int id, @RequestBody Post updatedPost) {
            Post post = posts.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
            if (post != null) {
                post.setDescription(updatedPost.getDescription());
                post.setImage(updatedPost.getImage());
                post.setPrivacy(updatedPost.getPrivacy());
                return post;
            } else {
                return null;
            }
        }
    }
}
