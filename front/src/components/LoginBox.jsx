import React, { Component }  from 'react';
import { Card, Container, Icon } from 'semantic-ui-react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { API_URL } from './App.jsx';
import { cookies, returnCodes } from './App.jsx';

import TransitionGroup from "react-transition-group";

import FadeTransition from "../fade/fadeTransition";


import Filter from '../containers/Filter';
import Menu from '../containers/Menu';

function showErrorMessage(target, message) {
	document.getElementById(target).innerHTML = message;
}

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isLoginOpen: true,
      isRegisterOpen: false,
	  isForgottenOpen: false
    };
  }

  showLoginBox() {
    this.setState({isLoginOpen: true, isRegisterOpen: false, isForgottenOpen: false});
	console.log(this.state);
  }

  showRegisterBox() {
    this.setState({isRegisterOpen: true, isLoginOpen: false, isForgottenOpen: false});
	console.log(this.state);
  }
  showForgottenBox() {
    this.setState({isRegisterOpen: false, isLoginOpen: false, isForgottenOpen: true});
  }

  render() {
    return (
      <div className="root-container login-register-container">

        <div className="box-controller">
          <div
            className={"controller " + ((this.state.isLoginOpen || this.state.isForgottenOpen)
            ? "selected-controller"
            : "")}
            onClick={this
            .showLoginBox
            .bind(this)}>
            Logowanie
          </div>
          <div
            className={"controller " + (this.state.isRegisterOpen
            ? "selected-controller"
            : "")}
            onClick={this
            .showRegisterBox
            .bind(this)}>
            Rejestracja
          </div>
        </div>
 
        <FadeTransition isOpen={this.state.isLoginOpen} duration={500}>
          <div className="box-container">
            <LoginBox parental={this} />
          </div>
        </FadeTransition>
        <FadeTransition isOpen={this.state.isRegisterOpen} duration={500}>
          <div className="box-container">
            <RegisterBox/>
          </div>
        </FadeTransition>
        <FadeTransition isOpen={this.state.isForgottenOpen} duration={500}>
          <div className="box-container">
            <ForgottenBox parental={this} />
          </div>
        </FadeTransition>

      </div>
    );

  }

}

class LoginBox extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  submitLogin(e) {
	  var data= new FormData();
	  data.append('username', this.state.username);
data.append('password', this.state.password);
		axios.post(`${API_URL}/login`, data)
		  .then(function (response) {
			  if (response.data == "0") {
				  showErrorMessage("loginErrorMessage", returnCodes["LOGIN_ERROR"]);
			  }
			else {
				cookies.set('sessionId', response.data, { path: '/' });
				window.location.replace("/");
			}
		  })
		  .catch(function (error) {
			console.log(error);
		  });
  }
    onUsernameChange(e) {
    this.setState({username: e.target.value});
  }
  
    onPasswordChange(e) {
    this.setState({password: e.target.value});
  }
  render() {
    return (
      <div className="inner-container">
        <div className="header">
          Logowanie
        </div>
        <div className="box">

          <div className="input-group">
            <label htmlFor="username">Nazwa użytkownika: </label>
			  <input
              type="text"
              name="username"
              className="login-input"
              placeholder="Nazwa użytkownika"
              onChange={this
              .onUsernameChange
              .bind(this)} />
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
              .bind(this)}
			  />
          </div>

          <button
            type="button"
            className="login-btn"
            onClick={this
            .submitLogin
            .bind(this)}>Zaloguj się</button>
		<a className="forgotten-password" onClick={this.props.parental.showForgottenBox.bind(this.props.parental)}>Zapomniałeś hasła?</a>
		<p id="loginErrorMessage" className="errorMessage"></p>
        </div>
      </div>
    );
  }

}

class ForgottenBox extends React.Component {

  constructor(props) {
    super(props);
    this.state = {};
  }

  submitForgotten(e) {
	  document.getElementsByTagName("BODY")[0].classList.add("waiting");
	  var data= new FormData();
	  data.append('email', this.state.email);
	  data.append('password', this.state.password);
		axios.post(`${API_URL}/forgottenPassword`, data)
		  .then(function (response) {
	  document.getElementsByTagName("BODY")[0].classList.remove("waiting");
			  if(response.data) {
				window.location.replace("/?fp=1");
			  }
			  else {
				  showErrorMessage("forgottenErrorMessage", returnCodes["WRONG_EMAIL"]);
			  }
			  console.log(response.data);
		  })
		  .catch(function (error) {
			console.log(error);
		  });
		  
  }
    onEmailChange(e) {
    this.setState({email: e.target.value});
  }
  
  render() {
    return (
      <div className="inner-container">
		<a className="forgotten-password" onClick={this.props.parental.showLoginBox.bind(this.props.parental)}><Icon name="angle left" /> Wstecz</a>
        <div className="header">
          Zapomniane hasło
        </div>
        <div className="box">

          <div className="input-group">
            <label htmlFor="email">E-mail: </label>
			  <input
              type="text"
              name="email"
              className="login-input"
              placeholder="Adres e-mail"
              onChange={this
              .onEmailChange
              .bind(this)} />
          </div>

          <button
            type="button"
            className="login-btn forgotten-password-button"
            onClick={this
            .submitForgotten
            .bind(this)}>Przypomnij hasło</button>
		<p id="forgottenErrorMessage" className="errorMessage"></p>
        </div>
      </div>
    );
  }

}

function pop(props) {
  return
}

class RegisterBox extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
      username: "",
      email: "",
      password: "",
      errors: [],
      pwdState: null
    };
  }

  onUsernameChange(e) {
    this.setState({username: e.target.value});
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

  onNameChange(e) {
    this.setState({name: e.target.value});
  }

  onPasswordConfirmationChange(e) {
    this.setState({passwordConfirmation: e.target.value});
  }
  
  onSurnameChange(e) {
    this.setState({surname: e.target.value});
  }

  onStreetChange(e) {
    this.setState({street: e.target.value});
  }

  onPostalCodeChange(e) {
    this.setState({postalCode: e.target.value});
  }
  
  onCityChange(e) {
    this.setState({city: e.target.value});
  }

  submitRegister(e) {
	var data= new FormData();
	data.append('username', this.state.username);
	data.append('password', this.state.password);
	data.append('name', this.state.name);
	data.append('surname', this.state.surname);
	data.append('street', this.state.street);
	data.append('postalCode', this.state.postalCode);
	data.append('phone', this.state.phone);
	data.append('email', this.state.email);
	data.append('passwordConfirmation', this.state.passwordConfirmation);
	data.append('city', this.state.city);
	axios.post(`${API_URL}/register`, data)
	.then(function (response) {
		if(response.data == "SUCCESS") {
			window.location.replace("/?fp=3");
		}
		else {
			showErrorMessage("registerErrorMessage", returnCodes[response.data]);
		}
	})
	.catch(function (error) {
		console.log(error);
	});
  }

  render() {

    return (
      <div className="inner-container">
        <div className="header">
          Rejestracja
        </div>
        <div className="box">

          <div className="input-group">
            <label htmlFor="username">Nazwa użytkownika: </label>
            <input
              type="text"
              name="username"
              className="login-input"
              placeholder="Nazwa użytkownika"
              onChange={this
              .onUsernameChange
              .bind(this)}/>
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
            <label htmlFor="name">Imię: </label>
            <input
              type="text"
              name="name"
              className="login-input"
              placeholder="Imię"
              onChange={this
              .onNameChange
              .bind(this)}/>
          </div>
          <div className="input-group">
            <label htmlFor="surname">Nazwisko: </label>
            <input
              type="text"
              name="surname"
              className="login-input"
              placeholder="Nazwisko"
              onChange={this
              .onSurnameChange
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
            .submitRegister
            .bind(this)}>Zarejestruj się</button>
		<p id="registerErrorMessage" className="errorMessage"></p>

        </div>

    );

  }

}

export default Login;
