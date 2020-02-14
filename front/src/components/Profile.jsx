import React, { Component }  from 'react';
import { Card, Container, Icon, Image, Button } from 'semantic-ui-react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { API_URL, app, isLogged, coins } from './App.jsx';
import { cookies, returnCodes } from './App.jsx';
import defaultAvatar from '../assets/images/defaultAvatar.jpg';
import NewGameList from './Games';
import TradeModal from './TradeModal';

function get(name){
  const parts = window.location.href.split('?');
  if (parts.length > 1) {
    name = encodeURIComponent(name);
    const params = parts[1].split('&');
    const found = params.filter(el => (el.split('=')[0] === name) && el);
    if (found.length) return decodeURIComponent(found[0].split('=')[1]);
  }
}

var prof;


class Profile extends Component {
  constructor(props) {
    super(props);
	prof = this;
    this.state = {
      userId: get("u"),
	  uesrname: null,
	  me: false,
	  isProfile: true,
	  modalOpen: false,
	  chosenGame: null,
	  boardiesList: null,
	  callingUser: null
    };
	this.loadUserData();
	this.checkIfMe();
	//this.showUsersGames();
  }
	checkIfMe() {
		var data= new FormData();
		data.append('userId', this.state.userId);
		data.append('sessionId', cookies.get('sessionId'));
		axios.post(`${API_URL}/getMatchForSession`, data)
		.then(function (response) {
			if(response.data) {
				document.getElementById("messageButton").remove();
				document.getElementById("reportButton").remove();
				document.getElementById("root").classList.add("myProfile");
				//document.getElementyById
				prof.setState({me: true});
			}
			else {
				document.getElementById("myBalance").remove();
				document.getElementById("myBalanceText").remove();
			    prof.setState({me: false});
			}
		})
		.catch(function (error) {
			console.log(error);
		});
	}
  
	loadUserData() {
		var data= new FormData();
		data.append('userId', this.state.userId);
		axios.post(`${API_URL}/getUserById`, data)
		.then(function (response) {
			document.getElementById("username").innerHTML = response.data.username;
			document.getElementById("name").innerHTML = response.data.name + " " + response.data.surname;
			document.getElementById("city").innerHTML = response.data.city;
			document.getElementById("joined").innerHTML = "Dołączył "+response.data.joinDate;
			document.getElementById("swapsMade").innerHTML = response.data.swapsMade;
			prof.setState({username: response.data.username, city: response.data.city, joined: response.data.joinDate, surname: response.data.surname, name: response.data.name, swapsMade: response.data.swapsMade});
		})
		.catch(function (error) {
			console.log(error);
		});
	}
	
	report() {
		window.open("mailto:boardieswebservice@gmail.com?Subject=BOARDIES:Zgłoszenie użytkownika "+prof.state.username);
	}

	render() {
		return(
		<Container className="profile-container"><div className="heading"><h1>PROFIL UŻYTKOWNIKA</h1><h2 id="username">NICK</h2></div>
		<Container className="cards-wrapper">
		  <Card>
			<Image src={defaultAvatar} wrapped ui={false} />
			<Card.Content>
			  <Card.Header id="name">Imię Nazwisko</Card.Header>
			  <Card.Meta className="date-wrapper">
				<span className='date' id="joined">Dołączył</span>
			  </Card.Meta>
			  <Card.Description id="buttonWrapper" className="profile-button-wrapper">
				<Button primary id="messageButton" ><Icon name="envelope" />Wiadomość</Button>
				<Button secondary id="reportButton" onClick={(e) => this.report()}><Icon name="exclamation" />Zgłoś</Button>
				<Icon id="myBalance" name="universal access" /> <span id="myBalanceText">{coins}</span>
			  </Card.Description>
			</Card.Content>
		  </Card>
		  
		  
		  <Card.Group className="info-cards">
			<Card>
			  <Card.Content className="city">
				<Card.Header content='SKĄD' />
				<Card.Description id="city" content='Miasto' />
			  </Card.Content>
			</Card>
			<Card>
			  <Card.Content className="city">
				<Card.Header content='DOKONANE WYMIANY' />
				<Card.Description id="swapsMade" content='0' />
			  </Card.Content>
			</Card>
			<Card>
			  <Card.Content className="city">
				<Card.Header content='POSIADANE GRY' />
				<Card.Description id="gamesCount" content='0' />
			  </Card.Content>
			</Card>
		  </Card.Group>
		  </Container>
		  
		  <h2 className="centered">CHCE WYMIENIĆ</h2>
		  <NewGameList userId={this.state.userId} me={this.state.me} caller={this} callerName={"Profile"} />
		</Container>
		)
	}
}

export default Profile;