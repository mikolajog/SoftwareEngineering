package pl.boardies.models;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Game")
public class Game {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String img;

    //getters
    public int getId() {
    	return id;
    }
    
    public String getTitle() {
    	return title;
    }
    
    public String getDescription() {
    	return description;
    }
    
    public String getImg() {
    	return img;
    }



    //setters 
    public void setId(int id) {
    	this.id = id;
    }
    
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public void setDescription(String description) {
    	this.description = description;
    }
    
    public void setImg(String img) {
    	this.img = img;
    }

    
    public Game(String title, String description, String img) {
    	this.title = title;
    	this.description = description;
    	this.img = img;
    }
    
    @Override
    public String toString() {
    	return "GameID: "+ this.id + " Title: " + this.title + " Description: " + this.description + " Img: " + this.img;
    }
    
    public Game() {}
    
 
}



