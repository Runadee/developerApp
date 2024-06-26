package com.developer.services;


import com.developer.entities.Post;
import com.developer.entities.User;
import com.developer.repositories.PostRepository;
import com.developer.requests.PostCreateRequest;
import com.developer.requests.PostUpdateRequest;
import com.developer.responses.PostResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PostService {

    private PostRepository postRepository;
    private UserService userService;

    public PostService(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }


    public List<PostResponse> getAllPosts(Optional<Long> userId) {
        List<Post> list;
        if (userId.isPresent()){
           list = postRepository.findByUserId(userId.get());
        } else {
            list = postRepository.findAll();
        }
        return list.stream().map(PostResponse::new).collect(Collectors.toList());
    }

    public Post getOnePostById(Long postId){
        return postRepository.findById(postId).orElse(null );
    }

    public Post createOnePost(PostCreateRequest newPostRequest) {
       User user = userService.getOneUserById(newPostRequest.getUserId());
       if (user == null) {
           return null;
       }
       Post toSave = new Post();
       toSave.setId(newPostRequest.getId());
       toSave.setText(newPostRequest.getText());
       toSave.setTitle(newPostRequest.getTitle());
       toSave.setUser(user);
       return postRepository.save(toSave);
    }


    public Post updateOnePostById(Long postId, PostUpdateRequest updatePost) {
        Optional<Post> post = postRepository.findById(postId);
        if (post.isPresent()){
            Post toUpdate = post.get();
            toUpdate.setText(updatePost.getText());
            toUpdate.setTitle(updatePost.getTitle());
            postRepository.save(toUpdate);
            return toUpdate;
        }
        return null;
    }

    public void deleteOnePostById(Long postId) {
        postRepository.deleteById(postId);
    }
}
