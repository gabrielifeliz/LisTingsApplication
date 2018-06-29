package com.example.listings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Controller
public class HomeController {
    @Autowired
    PostRepository postRepository;

    @RequestMapping("/")
    public String displayHome(Model model) {
        model.addAttribute("posts", postRepository.findAllByOrderByPublicationDateDesc());
        return "index";
    }

    @GetMapping("/addpost")
    public String addPost(Model model) {
        model.addAttribute("post", new Post());
        return "addpost";
    }

    @PostMapping("/process")
    public String processForm(@Valid @ModelAttribute("post") Post post, BindingResult result) {
        if (result.hasErrors()) {
            return "addpost";
        }

        post.setPublicationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm:ss")));
        postRepository.save(post);
        return "redirect:/";
    }

    @RequestMapping("/update/{id}")
    public String updatePost( @PathVariable("id") long id, Model model){
        model.addAttribute("post", postRepository.findById(id).get());
        return "addpost";
    }

    @RequestMapping("/delete/{id}")
    public  String deletePost(@PathVariable("id") long id){
        postRepository.deleteById(id);
        return "redirect:/";
    }

    @RequestMapping("/search")
    public String showSearchResults(HttpServletRequest request, Model model)
    {
        //Get the search string from the result form
        String searchString = request.getParameter("search");
        model.addAttribute("search", searchString);
        model.addAttribute("posts",
                postRepository.findAllByTitleContainingIgnoreCase(searchString));
        return "index";
    }
}