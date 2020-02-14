import React, { Component }  from 'react';
import { Card, Container, Icon } from 'semantic-ui-react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { API_URL } from './App.jsx';
import { cookies, returnCodes } from './App.jsx';

function get(name){
  const parts = window.location.href.split('?');
  if (parts.length > 1) {
    name = encodeURIComponent(name);
    const params = parts[1].split('&');
    const found = params.filter(el => (el.split('=')[0] === name) && el);
    if (found.length) return decodeURIComponent(found[0].split('=')[1]);
  }
}

function showErrorMessage(target, message) {
	document.getElementById(target).innerHTML = message;
}

var pEdit;

class ProfileEdit extends React.Component {

  constructor(props) {
    super(props);
	pEdit = this;
    this.state = {
      password: "",
      passwordConfirmation: ""
    };
	  var data = new FormData();
	  data.append('sessionId', cookies.get('sessionId'));
	 axios.post(`${API_URL}/getUser`, data).then(function (response) {
		if(response.data != null) {
			document.getElementsByName("username")[0].value = response.data.username;
			document.getElementsByName("email")[0].value = response.data.email;
			document.getElementsByName("surname")[0].value = response.data.surname;
			document.getElementsByName("name")[0].value = response.data.name;
			document.getElementsByName("postalCode")[0].value = response.data.postalCode;
			document.getElementsByName("city")[0].value = response.data.city;
			document.getElementsByName("street")[0].value = response.data.street;
			document.getElementsByName("phone")[0].value = response.data.phone;
			pEdit.setState({phone: response.data.phone});
			pEdit.setState({street: response.data.street});
			pEdit.setState({postalCode: response.data.postalCode});
			pEdit.setState({city: response.data.city});
			pEdit.setState({surname: response.data.surname});
			}
		else {
			window.location.replace("/?fp=997");
		}
	})
	.catch(function (error) {
		console.log(error);
	});
  }

  onEmailChange(e) {
    this.setState({email: e.target.value});
  }

  onPasswordChange(e) {
    this.setState({password: e.target.value});
  }
  
  onPhoneChange(e) {
    this.setState({phone: e.target.value});
  }

  onPasswordConfirmationChange(e) {
    this.setState({passwordConfirmation: e.target.value});
  }

  onStreetChange(e) {
    this.setState({street: e.target.value});
  }

  onOldPasswordChange(e) {
    this.setState({oldPassword: e.target.value});
  }

  onPostalCodeChange(e) {
    this.setState({postalCode: e.target.value});
  }
  
  onCityChange(e) {
    this.setState({city: e.target.value});
  }

  submitProfileEdit(e) {
	var data= new FormData();
	data.append('password', this.state.password);
	data.append('oldPassword', this.state.oldPassword);
	data.append('street', this.state.street);
	data.append('postalCode', this.state.postalCode);
	data.append('phone', this.state.phone);
	data.append('email', this.state.email);
	data.append('passwordConfirmation', this.state.passwordConfirmation);
	data.append('city', this.state.city);
	data.append('sessionId', cookies.get('sessionId'));
	axios.post(`${API_URL}/editProfile`, data)
	.then(function (response) {
		console.log("response:   "+response.data);
		if(response.data == "SUCCESS") {
			window.location.replace("/?fp=4");
		}
		else {
			showErrorMessage("errorMessage", returnCodes[response.data]);
		}
	})
	.catch(function (error) {
		console.log(error);
	});
  }

  render() {

    return (
      <div className="inner-container editProfileInner">
        <div className="header">
          Zmiana danych
        </div>
        <div className="box">

          <div className="input-group">
            <label htmlFor="username">Nazwa użytkownika: </label>
            <input
			  disabled
              type="text"
              name="username"
              className="login-input"
              placeholder="Nazwa użytkownika"/>
          </div>
		  
          <div className="input-group">
            <label htmlFor="name">Imię: </label>
            <input
			  disabled
              type="text"
              name="name"
              className="login-input"
              placeholder="Imię"/>
          </div>
          <div className="input-group">
            <label htmlFor="surname">Nazwisko: </label>
            <input
			  disabled
              type="text"
              name="surname"
              className="login-input"
              placeholder="Nazwisko"/>
          </div>
          <div className="input-group">
            <label htmlFor="email">Adres e-mail: </label>
            <input
			  
              type="text"
              name="email"
              className="login-input"
              placeholder="Adres e-mail"
              onChange={this
              .onEmailChange
              .bind(this)}/>
          </div>          
		  <div className="input-group">
            <label htmlFor="oldPassword">Stare hasło: </label>
            <input
              type="password"
              name="oldPassword"
              className="login-input"
              placeholder="Stare hasło"
              onChange={this
              .onOldPasswordChange
              .bind(this)}/>
          </div>
          <div className="input-group">
            <label htmlFor="password">Hasło: </label>
            <input
              type="password"
              name="password"
              className="login-input"
              placeholder="Hasło"
              onChange={this
              .onPasswordChange
              .bind(this)}/>
          </div>
          <div className="input-group">
            <label htmlFor="passwordConfirmation">Powtórz hasło: </label>
            <input
              type="password"
              name="passwordConfirmation"
              className="login-input"
              placeholder="Powtórz hasło"
              onChange={this
              .onPasswordConfirmationChange
              .bind(this)}/>
          </div>
		  
          <div className="input-group">
            <label htmlFor="phone">Numer telefonu: </label>
            <input
              type="text"
              name="phone"
              className="login-input"
              placeholder="Numer telefonu"
              onChange={this
              .onPhoneChange
              .bind(this)}/>
          </div>
          <div className="input-group">
            <label htmlFor="street">Ulica: </label>
            <input
              type="text"
              name="street"
              className="login-input"
              placeholder="Ulica"
              onChange={this
              .onStreetChange
              .bind(this)}/>
          </div>
		  
          <div className="input-group">
            <label htmlFor="postalCode">Kod pocztowy: </label>
            <input
              type="text"
              name="postalCode"
              className="login-input"
              placeholder="Kod pocztowy"
              onChange={this
              .onPostalCodeChange
              .bind(this)}/>
          </div>
          <div className="input-group">
            <label htmlFor="city">Miejscowość: </label>
            <input
              type="text"
              name="city"
              className="login-input"
              placeholder="Miejscowość"
              onChange={this
              .onCityChange
              .bind(this)}/>
          </div>
		  

          </div>

          <button
            type="button"
            className="login-btn"
            onClick={this
            .submitProfileEdit
            .bind(this)}>Zmień dane</button>
		<p id="errorMessage" className="errorMessage"></p>

        </div>

    );

  }

}


export default ProfileEdit;