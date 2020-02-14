import React, { Component }  from 'react';
import { Card, Container, Icon, Image, Button } from 'semantic-ui-react';
export { NewGameList, NewGame};
import { API_URL, ALL_DISTINCT_GAMES_API_URL, app, isLogged, coins } from './App.jsx';
import { cookies, returnCodes } from './App.jsx';
import axios from 'axios';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
export {ngl}

function get(name){
  const parts = window.location.href.split('?');
  if (parts.length > 1) {
    name = encodeURIComponent(name);
    const params = parts[1].split('&');
    const found = params.filter(el => (el.split('=')[0] === name) && el);
    if (found.length) return decodeURIComponent(found[0].split('=')[1]);
  }
}


var ngl;
var games = null;

class NewGameList extends Component {
	
	constructor(props) {
		super(props);
		this.state = {
		  games: null,
		  boardieMode: true
		};
		ngl = this;
		switch(this.props.callerName) {
			case "Profile":
				this.getBoardies();
				break;
			case "App":
				this.getAllBoardies();
				break;
			case "MySwaps":
				this.getMyBoardies();
				break;
			case "AddBoardie":
				this.getAllGames();
				break;
			default:
				break;
		}
	}
	getAllGames() {		
		axios.get(`${API_URL}/games/showAll/`).then(({ data }) => {
			ngl.setState({games: data, boardieMode: false});
			if (data === undefined || data.length == 0) {
				ngl.props.caller.setState({noGames: true});
			}
			else {
				ngl.props.caller.setState({noGames: false});
			}
		});
	}
	
	getSearchedGames(searchValue) {		
		axios.get(`${API_URL}/games/search/`+searchValue).then(({ data }) => {
			ngl.setState({games: data, boardieMode: false});
			if (data === undefined || data.length == 0) {
				ngl.props.caller.setState({noGames: true});
			}
			else {
				ngl.props.caller.setState({noGames: false});
			}
		});
	}
	
	getMyBoardies() {	
		var data= new FormData();
		data.append('sessionId', cookies.get('sessionId'));
		axios.post(`${API_URL}/getUser`, data)
		.then(function (response) {
			let us = response.data.id;
			var data2= new FormData();
			data2.append('userId', us);
			axios.post(`${API_URL}/showAvailableUsersBoardies`, data2)
			.then(function (response) {
				
				ngl.setState({games: response.data, boardieMode: true});
			})
			.catch(function (error) {
				console.log(error);
			});
		})
		.catch(function (error) {
			console.log(error);
		});
	
	

	}
	
	getAllBoardies() {		
		axios.get(`${ALL_DISTINCT_GAMES_API_URL}`).then(({ data }) => {
			if(ngl.props.userId != null) {
				let tmp = data;
				axios.get(`${API_URL}/boardies/getIdsOfUsersBoardies/`+ngl.props.userId).then(({data}) => {
				ngl.setState({games: tmp.filter(function(elem) {
						return !data.includes(elem.game.id);
					}
					), boardieMode: true});
				});
			}
			else {
				ngl.setState({games: data, boardieMode: true});
			}
		});
	}
	
	getSearchedBoardies(searchValue) {
		axios.get(`${API_URL}/boardies/search/`+searchValue).then(({ data }) => {
			if(ngl.props.userId != null) {
				let tmp = data;
				axios.get(`${API_URL}/boardies/getIdsOfUsersBoardies/`+ngl.props.userId).then(({data}) => {
				ngl.setState({games: tmp.filter(function(elem) {
						return !data.includes(elem.game.id);
					}
					), boardieMode: true});
				});
			}
			else {
				ngl.setState({games: data, boardieMode: true});
			}
		});
	}
	
	getBoardies() {
		var data= new FormData();
		data.append('userId', this.props.userId);
		axios.post(`${API_URL}/showAvailableUsersBoardies`, data)
		.then(function (response) {
			ngl.setState({games: response.data, boardieMode: true});
			if(ngl.props.callerName == "Profile") {
				ngl.props.caller.setState({gamesCount: response.data.length});
				document.getElementById("gamesCount").innerHTML = response.data.length;
			}
			
		})
		.catch(function (error) {
			console.log(error);
		});
	}
	
	sort(ascending) {
		if(this.state.games !== undefined && this.state.games != null && this.state.games.length > 0) {
			if(ascending)
				this.state.games.sort((a, b) => (a.price > b.price) ? 1 : -1);
			else
				this.state.games.sort((a, b) => (a.price > b.price) ? -1 : 1);
			this.forceUpdate();
		}
	}
	
	render() {
		return (
		<Card.Group itemsPerRow={4} className='notFloating'>
			{this.state.games == null
				? ''
				: this.state.games.map(item => <NewGame boardieMode={this.state.boardieMode} key={Math.random()} theGame={item} />)}
		</Card.Group>
		)
	}
}

class NewGame extends Component {
	constructor(props) {
		super(props);
	}
	
				  handleCoinClick() {
					  if(isLogged) {
						  this.goToGame();
					  }
					  else
						  window.location.replace("/login");
				  }
				  
				  handleProfileTrade() {
					    var data= new FormData();
					    data.append('boardieId', this.props.theGame.id);
					    data.append('sessionId', cookies.get('sessionId'));
						axios.post(`${API_URL}/swap/prepareForSession`, data)
						.then(function (response) {
							console.log(response.data);
							window.location.replace('/?fp=5');
						})
						.catch(function (error) {
							console.log(error);
						});
				  }
				  handleDeleteGame() {
					axios.delete(`${API_URL}/boardies/delete/${this.props.theGame.id}/${ngl.props.userId}/`+cookies.get('sessionId')).then(response => {
					  console.log(response.data);
					  if(response.data) {
						  location.reload();
					  }
					});
				  }
				  handleFastTrade(e) {
					  
					  if(isLogged) {
						  if(e != "expensive") {
							  if(ngl.props.callerName == "Profile") {
								  this.handleProfileTrade();
							  }
							  else if (ngl.props.callerName == "MySwaps") {
								  this.handleDeleteGame();
							  }
							  else {
							  
						var data= new FormData();
						let callName = `${API_URL}/boardies/showAllAvailableButMine`;
						data.append('id_user', ngl.props.userId);
						data.append('id_game', this.props.theGame.game.id);
						let dis = this;
						axios.post(callName, data)
						.then(function (response) {
							var bl = response.data;
							var data2 = new FormData();
							data.append('userId', ngl.props.userId);
							axios.post(`${API_URL}/getUserById`, data).then(function (response) {
								ngl.props.caller.setState({modalOpen: true, chosenGame: dis.props.theGame.game,  callingUser: response.data, boardiesList: bl});
							}).catch(function(error){console.log(error);});
						})
						.catch(function (error) {
							console.log(error);
						});
						  }
						  }
					  }
					  else {
						  window.location.replace("/login");
					  }
				  }
				  
				  handleGameAdd() {
					axios.get(`${API_URL}/boardies/getCheapestPrice/`+this.props.theGame.id).then(({ data }) => {
						ngl.props.caller.setState({modalOpen: true, chosenGame: this.props.theGame, lowestPrice: data});
					});
				  }
				  
    goToGame() {
		this.props.boardieMode ? window.location.replace("/game?g="+this.props.theGame.game.id) : window.location.replace("/game?g="+this.props.theGame.id);
	}
	
	render() {
		let priceToDisplay, cardClass, exchangeText;
				  if(isLogged) {
					  if(ngl.props.callerName=="MySwaps") {
						  exchangeText = "USUŃ GRĘ";
						  cardClass = "myToDelete";
					  priceToDisplay = this.props.theGame.price;
					  } else {
					  priceToDisplay = this.props.theGame.price;
					  if(this.props.theGame.price > coins) {
						  cardClass = "expensive";
						  exchangeText = "NIE STAĆ CIĘ NA WYMIANĘ";
					  }
					  else {
						  cardClass= "";
						  if(ngl.props.callerName == "Profile" && ngl.props.caller.state.username != null)
							  exchangeText="WYMIEŃ Z "+ngl.props.caller.state.username.toUpperCase();
						  else
							  exchangeText = "WYMIEŃ SIĘ";
						  
					  }
				  }
				  }				  else {
					  priceToDisplay = "Zaloguj się, aby zobaczyć cenę";
					  cardClass="";
				  }
	    if(this.props.boardieMode)
			return (
				<Card className={cardClass}>
					<div className="main-wrapper" onClick={this.goToGame.bind(this)}>
					<div className="card-image">
					<Image src={this.props.theGame.game.img} />
					</div>
					<Card.Content>
					<Card.Header>{this.props.theGame.game.title}</Card.Header>

					</Card.Content>
					</div>
					<Card.Content extra>
					<a onClick={this.handleCoinClick.bind(this)}>
					<Icon name="universal access" />
					{priceToDisplay}
					</a>
					</Card.Content>
					<Button className="exchange-button" name="fastTradeButton" onClick={() => this.handleFastTrade(cardClass)}>
					{exchangeText} <Icon name="exchange" />
					</Button>
				</Card>
			) 
		else
			return (
				<Card className={cardClass}>
					<div className="main-wrapper" onClick={this.goToGame.bind(this)}>
					<div className="card-image">
					<Image src={this.props.theGame.img} />
					</div>
					<Card.Content>
					<Card.Header>{this.props.theGame.title}</Card.Header>

					</Card.Content>
					</div>
					<Button className="gameInAdd" name="gameInAddButton" onClick={this.handleGameAdd.bind(this)} >
					WYMIEŃ <Icon name="exchange" className="gameAddIcon" />
					</Button>
				</Card>
		)
	}
}
export default NewGameList;