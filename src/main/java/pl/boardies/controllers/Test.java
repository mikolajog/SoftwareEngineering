package pl.boardies.controllers;

import org.springframework.web.bind.annotation.RestController;

import pl.boardies.models.Boardies;
import pl.boardies.models.Game;
import pl.boardies.models.Swap;
import pl.boardies.models.User;
import pl.boardies.models.Session;
import pl.boardies.repositories.BoardiesRepository;
import pl.boardies.repositories.GameRepository;
import pl.boardies.repositories.SwapRepository;
import pl.boardies.repositories.UserRepository;
import pl.boardies.repositories.SessionRepository;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.NoSuchAlgorithmException;

@RestController
public class Test {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public SwapRepository swapRepository;

    @Autowired
    public GameRepository gameRepository;

    @Autowired
    public BoardiesRepository boardiesRepository;

    @Autowired
    public SessionRepository sessionRepository;


    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @RequestMapping("/test")
    @ResponseBody
    public String test() throws NoSuchAlgorithmException {
        StringBuilder response = new StringBuilder();

        for(User i: userRepository.findAll()) {
            response.append(i).append("<br>");
        }
        response.append("---------------------------------------").append("<br>");

        for(Game i: gameRepository.findAll()) {
            response.append(i).append("<br>");
        }
        response.append("---------------------------------------").append("<br>");

        for(Swap i: swapRepository.findAll()) {
            response.append(i).append("<br>");
        }
        response.append("---------------------------------------").append("<br>");

        for(Boardies i: boardiesRepository.findAll()) {
            response.append(i).append("<br>");
        }
        response.append("---------------------------------------").append("<br>");

        for(Session i: sessionRepository.findAll()) {
            response.append(i).append("<br>");
        }

        return response.toString();
    }

}