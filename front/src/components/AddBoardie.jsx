import React, { Component }  from 'react';
import { Card, Container, Icon, Image, Button, Input, Modal } from 'semantic-ui-react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Link, Header } from "react-router-dom";
import { API_URL, app, isLogged, coins } from './App.jsx';
import { cookies, returnCodes } from './App.jsx';
import NewGameList, { ngl} from './Games'
import AddBoardieModal from './AddBoardieModal'

import TransitionGroup from "react-transition-group";

import FadeTransition from "../fade/fadeTransition";

var addb;
var ngc;
var currentName = "";

function search(val) {
	if(val == "") {
		ngl.getAllGames();
		currentName = "";
	}
	else {
		ngl.getSearchedGames(val);
		currentName = val;
	}
}

class NewGameComponent extends Component {
	constructor(props) {
		super(props);
		ngc = this;
		this.state = {
			formOpened: false,
			description: "",
			price: "",
			img: ""
		};
	}
	
	
	submitNewGame() {
		var data= new FormData();
		data.append('title', currentName);
		data.append('description', this.state.description);
		data.append('img', this.state.img);
		axios.post(`${API_URL}/games/add`, data)
		.then(function (response) {
			ngc.submitNewBoardie(response.data);
		})
		.catch(function (error) {
			console.log(error);
		});
	}
	
	submitNewBoardie(e) {
		var data= new FormData();
		data.append('id_game', e);
		data.append('id_user', addb.props.userId);
		data.append('price', this.state.price);
		axios.post(`${API_URL}/boardies/add`, data)
		.then(function (response) {
			if(response.data)
				window.location.replace("/profile?u="+addb.props.userId);
			else
				console.log("ERROR");
		})
		.catch(function (error) {
			console.log(error);
		});
	}
	
	displayForm() {
		let willBeHidden = document.getElementsByClassName("toHide");
		for( let elem of willBeHidden) {
			elem.classList.add("hidden");
		}
		this.setState({formOpened: true});
	}
	
	displaySearch() {
		let willBeShown = document.getElementsByClassName("toHide");
		for( let elem of willBeShown) {
			elem.classList.remove("hidden");
		}
		this.setState({formOpened: false});
	}
	
	enableFilePic() {
		document.getElementById("urlPic").classList.add("hidden");
		document.getElementById("filePic").classList.remove("hidden");
	}
	
	enableUrlPic() {
		document.getElementById("urlPic").classList.remove("hidden");
		document.getElementById("filePic").classList.add("hidden");
	}
	
    onPriceChange(e) {
		this.setState({price: parseFloat(e.target.value)});
	}
		
    onImageChange(e) {
		this.setState({img: e.target.value});
	}
		
    onDescriptionChange(e) {
		this.setState({description: e.target.value});
	}
		
    onTitleChange(e) {
		this.setState({title: e.target.value});
	}
	
	render() {		
		return (
			<Container className="centered">
			<Button primary id="addGameButtonFinal" className="toHide" onClick={e => this.displayForm()}><Icon name="plus" className="new_game" />&nbsp;Dodaj grę '{this.props.gameName}'</Button>
			
        <FadeTransition isOpen={this.state.formOpened} duration={500}>
			<Container>
			<Button className="previousGameAdd" secondary onClick={e => this.displaySearch()}><Icon name="angle left" className="new_game" /> Wstecz</Button>
		<Container className="inner-container-game">
          <div className="input-group">
            <label htmlFor="title">Tytuł gry: </label>
			  <input
              type="text"
			  value={currentName}
              name="title"
              className="login-input"
              placeholder="Tytuł gry"
              onChange={this
              .onTitleChange
              .bind(this)}/>
          </div>
          <div className="input-group" id="urlPic">
            <label htmlFor="img">Obrazek: </label>
			<Container className="buttonsContainer">
		  <Button primary className="active" disabled={true}>Link</Button>
		  <Button primary className="inactive" onClick={e => this.enableFilePic()}>Plik</Button></Container>
            <input
              type="text"
              name="img"
              className="login-input"
              placeholder="Obrazek (URL)" 
              onChange={this
              .onImageChange
              .bind(this)}
			  />
          </div>
          <div className="input-group hidden" id="filePic">
            <label htmlFor="img">Obrazek: </label>
			<Container className="buttonsContainer">
		  <Button primary className="inactive" onClick={e => this.enableUrlPic()}>Link</Button>
		  <Button primary className="active" disabled={true}>Plik</Button></Container>
            <input
              type="file"
              name="img"
              placeholder="Obrazek (PLIK)" 
			  />
          </div>
          <div className="input-group descriptionArea">
            <label htmlFor="description">Opis: </label>
			  <textarea
              name="description"
              className="login-input"
              placeholder="Opis"
              onChange={this
              .onDescriptionChange
              .bind(this)}/>
          </div>
          <div className="input-group priceWrapper">
            <label htmlFor="price"><Icon name="universal access" /> Twoja cena: </label>
            <input
              type="text"
              name="price"
              className="login-input"
              placeholder="Cena" 
              onChange={this
              .onPriceChange
              .bind(this)}
			  />
          </div>
		            <button
            type="button"
            className="login-btn"
            onClick={this
            .submitNewGame
            .bind(this)}>Dodaj</button>
		<p id="errorMessage" className="errorMessage"></p>
			</Container>
			</Container>
        </FadeTransition>
			
			

			
			</Container>
		)
	}
}

class AddBoardie extends Component {
  constructor(props) {
    super(props);
	addb = this;
	this.state = {
		noGames: false,
		modalOpen: false,
		chosenGame: null,
		lowestPrice: 0.0
	};
  }
  
	submitNewExistingBoardie(e) {
		var data= new FormData();
		data.append('id_game', e);
		data.append('id_user', this.props.userId);
		data.append('price', document.getElementById("addPriceFinal").value);
		axios.post(`${API_URL}/boardies/add`, data)
		.then(function (response) {
			if(response.data)
				window.location.replace("/profile?u="+addb.props.userId);
			else
				console.log("ERROR");
		})
		.catch(function (error) {
			console.log(error);
		});
	}
  
  addExistingBoardie() {
	  console.log(addb);
	  addb.submitNewExistingBoardie(addb.state.chosenGame.id);
  }

	render() {
		return(
		<Container className="gameAddContainer"><div className="heading"><h1>DODAWANIE GRY DO WYMIANY</h1></div>
		      <div className="game_add_search toHide"><Input 
        icon="search"
        onChange={e => search(e.target.value)}
        placeholder="Wpisz tytuł gry, którą chcesz dodać..."
      /></div>
	   {this.state.noGames ? (<NewGameComponent gameName={currentName} />) : (<NewGameList userId={this.props.userId} caller={this} callerName={"AddBoardie"} />)}
	   <AddBoardieModal modalOpen={this.state.modalOpen} game={this.state.chosenGame} lowestPrice={this.state.lowestPrice} caller={this}/>
		</Container>

		)
	}
}

export default AddBoardie;