import React, { Component }  from 'react';
import { Card, Container, Icon } from 'semantic-ui-react';
import axios from 'axios';
import { BrowserRouter, Route, Switch, Link } from "react-router-dom";
import { Dropdown } from "semantic-ui-react";
import logo from '../assets/images/logo_full_white.png'
import Cookies from 'universal-cookie';
import Login from './LoginBox'
import NewPassword from './NewPassword'
import ProfileEdit from './ProfileEdit'
import Profile from './Profile'
import AddBoardie from './AddBoardie'
import NewGameList, { ngl} from './Games'
import Game from './Game'
import TradeModal from './TradeModal'
import MySwaps from './MySwaps'



export const returnCodes = {
	  "DATABASE_ERROR": "Wystąpił błąd bazy danych",
	  "SUCCESS": "Operacja zakończona pomyślnie",
	  "DUPLICATE_USERNAME": "Użytkownik z taką nazwą już istnieje!",
	  "DUPLICATE_EMAIL": "Użytkownik z takim adresem e-mail już istnieje!",
	  "DUPLICATE_PHONE": "Użytkownik z takim numerem telefonu już istnieje!",
	  "MISMATCHING_PASSWORDS": "Podane hasła nie są takie same!",
	  "EMPTY_FIELDS": "Niektóre pola są puste!",
	  "LOGIN_ERROR": "Niepoprawne dane logowania!",
	  "SESSION_ERROR": "Twoja sesja wygasła",
	  "WRONG_EMAIL": "Nie znaleziono użytkownika z takim adresem e-mail!",
	  "EMAIL_SENT": "Sprawdź swoją skrzynkę mailową. Na podany przez Ciebie adres wysłaliśmy link do zresetowania hasła.",
	  "PASSWORD_CHANGED": "Zmiana hasła przebiegła pomyślnie! Teraz możesz się zalogować.",
	  "REGISTRATION_SUCCESS": "Rejestracja przebiegła pomyślnie! Teraz możesz się zalogować.",
	  "PROFILE_EDIT_SUCCESS": "Poprawnie zmieniono dane.",
	  "BAD_PASSWORD": "Wprowadzono błędne stare hasło.",
	  "SWAP_PREPARED": "Sukces! Teraz umówcie się na dokonanie wymiany i zatwierdźcie ją!"
}

function showErrorMessage(target, message) {
	document.getElementById(target).innerHTML = message;
}

const cookies = new Cookies();
export { cookies };

function get(name){
  const parts = window.location.href.split('?');
  if (parts.length > 1) {
    name = encodeURIComponent(name);
    const params = parts[1].split('&');
    const found = params.filter(el => (el.split('=')[0] === name) && el);
    if (found.length) return decodeURIComponent(found[0].split('=')[1]);
  }
}

const API_URL = 'http://localhost:8080'
const ALL_DISTINCT_GAMES_API_URL = `${API_URL}/boardies/showAllDistinctAndAvailable`
const ALLGAMES_API_URL = `${API_URL}/boardies/showAll`
export { API_URL, ALL_DISTINCT_GAMES_API_URL };

import BookCard from '../containers/BookCard';
import Filter from '../containers/Filter';
import Menu from '../containers/Menu';

var isLogged = false;
var userId = 0;
var username = "";
var coins = 0;
var infoMessage = "";
export { isLogged, coins };
var app;

var locations = ["/login", "/newPassword", "/profileEdit","/profile","/addBoardie","/game","/mySwaps"];

function checkLocations() {
	for (let loc of locations) {
		if(window.location.href.includes(window.location.origin+loc))
			return false;
	}
	return true;
}

class App extends Component {
	  constructor(props){
    super(props);
		  app = this;
  this.checkCookie();
  switch(parseInt(get("fp"))) {
  case 1:
	  infoMessage = returnCodes["EMAIL_SENT"];
    break;
  case 2:
	  infoMessage = returnCodes["PASSWORD_CHANGED"];
    break;
  case 3:
	  infoMessage = returnCodes["REGISTRATION_SUCCESS"];
	  break;
  case 4:
	  infoMessage = returnCodes["PROFILE_EDIT_SUCCESS"];
	  break;
  case 5:
	  infoMessage = returnCodes["SWAP_PREPARED"];
	  break;
  case 997:
	  infoMessage = returnCodes["DATABASE_ERROR"];
	  break;
  default:
	infoMessage="";
}
  this.state = {modalOpen: false, chosenGame: null, boardiesList: null, callingUser: null, userId: 0}
  }
	checkCookie() {
	if(cookies.get('sessionId') == "0" || !cookies.get('sessionId')) {
		isLogged = false;
	}
	else {
		isLogged = true;
	}
	
}
  onclick () {
    window.location.assign('/login');
  }
  goTo(value) {
	  window.location.assign(value);
  }
  logout() {
	  cookies.remove('sessionId');
	  isLogged = false;
	  window.location.replace("/");
  }
  
  componentDidMount() {
  if (isLogged) {
	  var data = new FormData();
	  data.append('sessionId', cookies.get('sessionId'));
		axios.post(`${API_URL}/getUserId`, data)
		  .then(function (response) {
			userId = response.data;
		  }).then(function(){app.setState({userId: userId});})
		  .catch(function (error) {
			console.log(error);
		  });
		axios.post(`${API_URL}/getName`, data)
		  .then(function (response) {
			username = response.data;
		  }).then(function(){app.forceUpdate();})
		  .catch(function (error) {
			console.log(error);
		  });
		axios.post(`${API_URL}/getBalance`, data)
		  .then(function (response) {
			coins = response.data;
		  }).then(function(){app.forceUpdate();})
		  .catch(function (error) {
			console.log(error);
		  });
  }
  }
  
render() {
	let greeting;
	let login_but;
	
	if (isLogged) {
		greeting = <p className='welcome'>Cześć, {username}</p>;
		login_but = <Link onClick={(e) => this.logout()} className="login_btn" >Wyloguj się</Link>;
	}
	else {
		greeting = ``;
		login_but = <Link to="/login" onClick={(e) => this.onclick(e)} className="login_btn" >Zaloguj się</Link>;
		document.getElementsByTagName("BODY")[0].classList.add("not-logged");
	}
    return (
      <BrowserRouter>
      <Container>
        <p id="infoMessage" className="infoMessage">{infoMessage}</p>  
      <header>
        <div className="wrapper">
<a className="boardies_logo" href={window.location.origin}><img src={logo} alt="logo" />
	{greeting}</a>
          <nav>
		  
			<Dropdown className="for-logged profile-menu-element" text='Profil'>
				<Dropdown.Menu>
					<Dropdown.Item text='Wyświetl swój profil' onClick={(e) => this.goTo('/profile?u='+userId)} />
					<Dropdown.Item text='Twoje wymiany' onClick={(e) => this.goTo('/mySwaps')} />
					<Dropdown.Item text='Edytuj profil' onClick={(e) => this.goTo('/profileEdit')} />
				</Dropdown.Menu>
			</Dropdown>
            <ul className="menu-elements">
              <li><a href="">Kontakt</a></li>
              <li><a className="for-logged" href="">Wiadomości</a></li>
              <li><a className="for-logged" href="/addBoardie">Dodaj grę</a></li>
            </ul>
            {login_but}
            
            
          </nav>
		  
        </div>
      </header>
      { /* <Menu />  */ }
		  {isLogged ? ("") : (<Route path="/login" component={Login} />) }
		  {isLogged ? (<Route path="/profileEdit" component={ProfileEdit} />) : ("") }
		  {isLogged ? ("") : (<Route path="/newPassword" component={NewPassword} />) }
		  {isLogged ? (<Route path="/profile" render={(props) => <Profile {...props} isAuthed={true} app={app} />}     /> ) : ("") }
		  {isLogged ? (<Route path="/addBoardie" render={(props) => <AddBoardie {...props} isAuthed={true} app={app} userId={userId} />}     /> ) : ("") }
		  {isLogged ? (<Route path="/mySwaps" render={(props) => <MySwaps {...props} isAuthed={true} app={app} isLogged={isLogged} />}     /> ) : ("") }
		  <Route path="/game" render={(props) => <Game {...props} isAuthed={true} app={app} userId={userId} isLogged={isLogged} />} />
         
		  {checkLocations() ? (<Filter />) : ("")}
		  {checkLocations() ? (<div className='coins-overall rightFloating'>
		<p><strong>Twoje żetony:</strong> {coins} <Icon name='universal access' /></p>
	</div>) : ("")}
		  {checkLocations() ? (<NewGameList caller={this} userId={userId} callerName={"App"}/>) : ("")}
	
	<TradeModal modalOpen={this.state.modalOpen} game={this.state.chosenGame} caller={this} callerName={"App"} boardiesList={this.state.boardiesList} callingUser={this.state.callingUser}/>
    </Container>
    </BrowserRouter>
    );
 

}
}
export default App;
