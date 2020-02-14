package pl.boardies.models;
import java.time.LocalDate;


import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "User")
public class User {
	@Id
    @NotNull
	@GeneratedValue(strategy=GenerationType.AUTO)
    private int id; 
	
    @Column(unique = true, nullable = false)
    @NotNull
    private String username;

    @Column(nullable = false)
    @NotNull
    private String password;
    
    @Column(nullable = false)
    @NotNull
    private String name;

    @Column(nullable = false)
    @NotNull
    private String surname;

    @Column(nullable = false)
    @NotNull
    private String street;

    @Column(nullable = false)
    @NotNull
    private String postalCode;
    
    @Column(nullable = false)
    @NotNull
    private String city;
    
    @Column(nullable = false)
    @NotNull
    private int swapsMade;
    
    @Column(nullable = false)
    @NotNull
    private LocalDate joined;
    
    @Column(unique = true, nullable = false)
    @NotNull
    private String email;
    
    @Column(unique = true, nullable = false)
    @NotNull
    private String phone;

    @Column(nullable = false)
    @NotNull
    private double balance;
    
    //getters

    public int getId() {
        return id;
    }

    public String getUsername() {
    	return username;
    }

    public String getName() {
    	return name;
    }
    
    public String getSurname() {
    	return surname;
    }
    
    public String getPassword() {
    	return password;
    }
    
    public LocalDate getJoinDate() {
    	return joined;
    }

    public String getStreet() {
    	return street;
    }

    public int getSwapsMade() {
    	return swapsMade;
    }
    
    public String getPostalCode() {
    	return postalCode;
    }
    
    public String getCity() {
    	return city;
    }
    
    public String getEmail() {
    	return email;
    }
    
    public String getPhone() {
    	return phone;
    }

    public double getBalance() {
        return balance;
    }



    //setters 
    public void setUsername(String username) {
    	this.username = username;
    }
    
    public void setName(String name) {
    	this.name = name;
    }
    
    public void setSwapsMade(int swapsMade) {
    	this.swapsMade = swapsMade;
    }
    
    public void setSurname(String surname) {
    	this.surname = surname;
    }
    
    public void setStreet(String street) {
    	this.street = street;
    }
    
    public void setJoinDate(LocalDate joined) {
    	this.joined = joined;
    }
    
    public void setCity(String city) {
    	this.city = city;
    }
    
    public void setPostalCode(String postalCode) {
    	this.postalCode = postalCode;
    }
    
    public void setPhone(String phone) {
    	this.phone = phone;
    }
    
    public void setPassword(String password) {
    	this.password = password;
    }
    
    public void setEmail(String email) {
    	this.email = email;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void changeBalance(double amount){
        this.balance += amount;
    }

    public User(String username, String password, String name, String surname, String street, String postalCode, String city, String email, String phone, double balance) {
    	this.username = username;
    	this.password = password;
    	this.email = email;
    	this.name = name;
    	this.surname = surname;
    	this.street = street;
    	this.postalCode = postalCode;
    	this.city = city;
    	this.phone = phone;
        this.balance = balance;
        this.swapsMade = 0;
        this.joined = LocalDate.now();
        
    }
    
    @Override
    public String toString() {
    	return "UserID: " + this.id + " Username: "+ this.username + " Password: " + this.password + " E-mail: " + this.email + " Name: " + this.name + " Surname: " + this.surname + " Street: " + this.street + " PostalCode: " + this.postalCode + " City: " + this.city + " Phone: " + this.phone + " Balance: " + this.balance+ " Swaps made: " + this.swapsMade;
    }
    
    public User() {}
    
 
}


