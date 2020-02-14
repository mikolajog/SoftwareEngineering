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

class NewPassword extends Component {
	
  constructor(props) {
    super(props);
    this.state = {};
	console.log(returnCodes["SUCCESS"]);
  }
    onPasswordOneChange(e) {
    this.setState({password: e.target.value});
  }
  
    onPasswordTwoChange(e) {
    this.setState({password2: e.target.value});
  }

  submitChange(e) {
	console.log(get("i"));
	console.log(get("s"));
	var data= new FormData();
	data.append('newPassword', this.state.password);
    data.append('confirmedNewPassword', this.state.password2);
	data.append('userId', get("i"));
	data.append('sessionId', get("s"));
		axios.post(`${API_URL}/setNewPassword`, data)
		  .then(function (response) {
			  if(response.data == "SUCCESS") {
				window.location.replace("/?fp=2");
			  }
			  else {
				  document.getElementById('errorMessage').innerHTML = returnCodes[response.data];
			  }
			  console.log(response.data);
		  })
		  .catch(function (error) {
			console.log(error);
		  });
  }
  render() {
    return (
      <div className="root-container">
	       <div className="box-container"> <div className="inner-container">
        <div className="header">
          Zmiana hasła
        </div>
        <div className="box">

          <div className="input-group">
            <label htmlFor="password">Nowe hasło: </label>
			  <input
              type="password"
              name="password"
              className="login-input"
              placeholder="Nowe hasło"
              onChange={this
              .onPasswordOneChange
              .bind(this)} />
          </div>

          <div className="input-group">
            <label htmlFor="password2">Powtórz nowe hasło: </label>
            <input
              type="password"
              name="password2"
              className="login-input"
              placeholder="Nowe hasło"
              onChange={this
              .onPasswordTwoChange
              .bind(this)}
			  />
          </div>

          <button
            type="button"
            className="login-btn"
            onClick={this
            .submitChange
            .bind(this)}>Zapisz</button>
			<p id="errorMessage" className="errorMessage"></p>
        </div>
      </div>
      </div></div>
    );

  }

}

export default NewPassword;