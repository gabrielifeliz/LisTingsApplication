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
    public String displayHome() {
        return "index";
    }


    @GetMapping("/addpost")
    public String addPost(Model model) {
        model.addAttribute("post", new Post());
        return "addpost";
    }

    @PostMapping("/process")
    public String processForm(@Valid Post post, BindingResult result) {
        if (result.hasErrors()) {
            return "addpost";
        }
        post.setPublicationDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern("MMM DD, YYYY")));
        postRepository.save(post);
        return "redirect:/showposts";
    }

    @RequestMapping("/showposts")
    public String showPost(Model model) {
        model.addAttribute("posts", postRepository.findAllByOrderByPublicationDateDesc());
        return "listposts";
    }

    @RequestMapping("/detail/{id}")
    public String postDetails(@PathVariable("id") long id, Model model) {
        model.addAttribute("post", postRepository.findById(id).get());
        return "showpost";
    }
    @RequestMapping("/update/{id}")
    public String updatePost( @PathVariable("id") long id, Model model){
        model.addAttribute("post", postRepository.findById(id).get());
        return "addpost";
    }

    @RequestMapping("/delete/{id}")
    public  String deletePost(@PathVariable("id") long id){
        postRepository.deleteById(id);
        return "listposts";
    }

    @RequestMapping("/search")
    public String showSearchResults(HttpServletRequest request, Model model)
    {
        //Get the search string from the result form
        String searchString = request.getParameter("search");
        model.addAttribute("search", searchString);
        model.addAttribute("posts",
                postRepository.findAllByTitleContainingIgnoreCase(searchString));
        return "listposts";
    }
}

/*
    Reddit has become the one of the most popular sites on the internet. Your team has been tasked with creating a minireddit or a reddit clone called ListTings for the CodeWork Academy students.

        Your site should have a home page that lists user supplied links that look like:



        ProjectReddit  AddLink

        ----------------------------------------------------------------------

        13 Great Reasons to Learn Java
        submitted by Daylin
        May 31, 2017

        The Best MOBA is NOT Smite
        submitted by Daylin
        May 31, 2017

        etc.

        All links should be saved to the database.

        The links should be listed by most recently added link first on the page.

        Each submission should include a title and a url, and the url should be clickable when displayed as a list. Further the links should open in a new tab when clicked.

        Be careful and plan things out before you start coding!
        
        
        
        
        LocalDateTime.now()
        
        
        
        */
