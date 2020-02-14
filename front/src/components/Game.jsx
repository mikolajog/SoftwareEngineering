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

var userBalance = 0.0;
var gameInstance;
var game = null;
var gameId = get("g");
var re = "/<p>.*?<\/p>/g";
class Game extends Component {
  constructor(props) {
    super(props);
	gameInstance = this;
	this.state = {
		lowestPrice: 0.0,
		title: "",
		description: "",
		img: "",
		modalOpen: false,
		game: null,
		caller: this,
		callerName: "Game",
		boardiesList: null,
		callingUser: null,
		exchangeText: "",
		iconExchange: "",
		affordable: true
	};
	this.getGameData();
  }

  getGameData() {
	  
	  
	  axios.get(`${API_URL}/games/game/${gameId}`).then(({data}) => {
		  let g = data;
		  game = data;
		  axios.get(`${API_URL}/boardies/getCheapestPrice/${gameId}`).then(({ data }) => {
			  let pri = data;
			  let exc = "WYMIEŃ SIĘ";
			  let excIcon = "exchange";
			  let affor = true;
			  if(gameInstance.props.isLogged) {
				var data= new FormData();
				data.append('sessionId', cookies.get('sessionId'));
				axios.post(`${API_URL}/getBalance`, data)
				.then(function (response) {
					let userBalance = response.data;
					if(pri > userBalance) {
						  affor = false;
						  exc = "NIE STAĆ CIĘ";
						  excIcon = "warning";
					}
						console.log(affor);
					  gameInstance.setState({
						  lowestPrice: pri,
						  title: g.title,
						  img: g.img,
						  description: g.description,
						  exchangeText: exc,
						  iconExchange: excIcon,
						  affordable: affor
					  });
				})
				.catch(function (error) {
					console.log(error);
				});  
			  }
			  else {
				  exc = "ZALOGUJ SIĘ";
				  pri = '?';
				  excIcon = "lock";
				  gameInstance.setState({
					  lowestPrice: pri,
					  title: g.title,
					  img: g.img,
					  description: g.description,
					  exchangeText: exc,
					  iconExchange: excIcon,
					  affordable: affor
				  });
			  }
			  
			  
		  });
	  });
  }

  
  
  
  
  handleTrade() {
	  if(this.props.isLogged) {
		  if(this.state.affordable) {
				var data= new FormData();
				let callName = `${API_URL}/boardies/showAllAvailableButMine`;
				data.append('sessionId', cookies.get('sessionId'));
				axios.post(`${API_URL}/getUser`, data)
				.then(function(response) {
					var data2= new FormData();
					let us = response.data
					data2.append('id_user', us.id);
				    data2.append('id_game', game.id);
					axios.post(callName, data2)
					.then(function (response) {
						var bl = response.data;
						  gameInstance.setState({
							  modalOpen: true,
							  boardiesList: bl,
							  callingUser: us
						  });
					})
					.catch(function (error) {
						console.log(error);
					});
				})
				.catch(function(error) {
				console.log(error);});

			  

		  }
	  }
	  else {
		  window.location.replace("/login");
	  }
  }
  
	report() {
		window.open("mailto:boardieswebservice@gmail.com?Subject=BOARDIES:Zgłoszenie gry "+this.state.title);
	}
  
	render() {
		let descriptionArray = this.state.description.split(/\r?\n/);
		let colorOfActions = "nowPrice";
		if(!this.state.affordable)
			colorOfActions = colorOfActions + " notAffordable";
		return(
		<Container className="profile-container"><div className="heading"><h1>{this.state.title}</h1></div>
		<Container className="cards-wrapper gameWrapper">
		  <Card>
			<Image src={this.state.img} wrapped ui={false} />
		  </Card>
		  
		  <Card.Group className="info-cards game-cards">
			<Card>
			  <Card.Content className="actionsWrapper">
				<Card.Header content='WYMIANA' />
				<Card.Description>
				<p className={colorOfActions}><strong>Aktualna najniższa cena:</strong> {this.state.lowestPrice}&nbsp;<Icon name="universal access" /></p>
					<Button primary className={colorOfActions} onClick={this.handleTrade.bind(this)}>{this.state.exchangeText} <Icon name={this.state.iconExchange} /></Button>
					<Button secondary onClick={this.report.bind(this)}>ZGŁOŚ GRĘ <Icon name="exclamation" /></Button>
				</Card.Description>
			  </Card.Content>
			</Card>
			<Card>
			  <Card.Content className="descriptionWrapper">
				<Card.Header content='OPIS GRY' />
				<Card.Description className="gameDescription" id="description">{descriptionArray.map(item => <p key={Math.random()}>{item}</p>)}</Card.Description>
			  </Card.Content>
			</Card>
		  </Card.Group>
		  </Container>
		  
		<TradeModal modalOpen={this.state.modalOpen} game={game} caller={this} callerName={"Game"} boardiesList={this.state.boardiesList} callingUser={this.state.callingUser}/>
		</Container>
		)
	}
}

export default Game;