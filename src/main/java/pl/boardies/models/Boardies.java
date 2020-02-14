package pl.boardies.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Proxy;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "Boardies")
public class Boardies {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;
    
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

    @Column
    private double price;

    @Column
    private int status; // 0 - unavailable; 1 - available; 2 - waiting for action

    //getters
    public User getUser() {
    	return user;
    }
    
    public Game getGame() {
    	return game;
    }    
    
    public double getPrice() {
    	return price;
    }
    
    public int getId() {
    	return id;
    }

    public int getStatus() {
        return status;
    }


    //setters 
    public void setUser(User user) {
    	this.user = user;
    }
    
    public void setGame(Game game) {
    	this.game = game;
    }
    
    public void setPrice(double price) {
    	this.price = price;
    }
    
    public void setId(int id) {
    	this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public Boardies(User user, Game game, double price, int status) {
    	this.user = user;
    	this.game = game;
    	this.price = price;
    	this.status = status;
    }
    
    @Override
    public String toString() {
    	return "BoardiesID: "+ this.id + " UserID: " + this.user.getId() + " GameID: " + this.game.getId() + " Price: " + this.price + " Status: " + this.status;
    }
    
    public Boardies() {}
    
 
}


