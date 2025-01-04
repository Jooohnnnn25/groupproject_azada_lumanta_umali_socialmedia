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

        // Get all posts
        @GetMapping
        public List<Post> getAllPosts() {
            return posts;
        }

        // Get post by ID
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

        // Delete post by ID
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

        // Update post by ID
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

        // Get all posts with a specific privacy filter (new feature)
        @GetMapping("/filter")
        public List<Post> getPostsByPrivacy(@RequestParam String privacy) {
            List<Post> filteredPosts = new ArrayList<>();
            for (Post post : posts) {
                if (post.getPrivacy().equalsIgnoreCase(privacy)) {
                    filteredPosts.add(post);
                }
            }
            return filteredPosts;
        }

        // Get all posts sorted by their description length (new feature)
        @GetMapping("/sortByDescription")
        public List<Post> getPostsSortedByDescriptionLength() {
            posts.sort((post1, post2) -> Integer.compare(post1.getDescription().length(), post2.getDescription().length()));
            return posts;
        }

        // Add a comment to a post (new feature)
        @PostMapping("/{id}/comment")
        public String addCommentToPost(@PathVariable int id, @RequestParam String comment) {
            Post post = posts.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
            if (post != null) {
                // Simulate adding comment (You can store comments in a List if needed)
                return "Comment added to post ID " + id + ": " + comment;
            } else {
                return "Post not found.";
            }
        }

        // Search posts by description keyword (new feature)
        @GetMapping("/search")
        public List<Post> searchPostsByDescription(@RequestParam String keyword) {
            List<Post> searchResults = new ArrayList<>();
            for (Post post : posts) {
                if (post.getDescription().contains(keyword)) {
                    searchResults.add(post);
                }
            }
            return searchResults;
        }

        // Like a post (new feature)
        @PostMapping("/{id}/like")
        public String likePost(@PathVariable int id) {
            Post post = posts.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
            if (post != null) {
                // Simulate like functionality (You could use a counter for likes if needed)
                return "Post ID " + id + " liked.";
            } else {
                return "Post not found.";
            }
        }

        // Unlike a post (new feature)
        @PostMapping("/{id}/unlike")
        public String unlikePost(@PathVariable int id) {
            Post post = posts.stream().filter(p -> p.getId() == id).findFirst().orElse(null);
            if (post != null) {
                // Simulate unlike functionality
                return "Post ID " + id + " unliked.";
            } else {
                return "Post not found.";
            }
        }

        // View posts by privacy and sorted by description length (new feature)
        @GetMapping("/filterAndSort")
        public List<Post> getPostsByPrivacyAndSortedByDescription(@RequestParam String privacy) {
            List<Post> filteredPosts = getPostsByPrivacy(privacy);
            filteredPosts.sort((post1, post2) -> Integer.compare(post1.getDescription().length(), post2.getDescription().length()));
            return filteredPosts;
        }
    }
}
