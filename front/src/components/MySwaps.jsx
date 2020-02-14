import React, { Component }  from 'react';
import { Card, Container, Icon, Button, Image } from 'semantic-ui-react';
import axios from 'axios';
import { BrowserRouter as Router, Route, Link } from "react-router-dom";
import { API_URL } from './App.jsx';
import { cookies, returnCodes, userId } from './App.jsx';
import NewGameList, { ngl} from './Games';
import defaultAvatar from '../assets/images/defaultAvatar.jpg';

import TransitionGroup from "react-transition-group";

import FadeTransition from "../fade/fadeTransition";

function showErrorMessage(target, message) {
	document.getElementById(target).innerHTML = message;
}

class MySwaps extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isMyBoardiesOpen: true,
      isAwaitingSwapsOpen: false,
	  isCurrentSwapsOpen: false,
	  isFinishedSwapsOpen: false
    };
  }

  showMyBoardies() {
    this.setState({      
	  isMyBoardiesOpen: true,
      isAwaitingSwapsOpen: false,
	  isCurrentSwapsOpen: false,
	  isFinishedSwapsOpen: false
	  });
  }

  showAwaitingSwaps() {
    this.setState({      
	  isMyBoardiesOpen: false,
      isAwaitingSwapsOpen: true,
	  isCurrentSwapsOpen: false,
	  isFinishedSwapsOpen: false
	  });
  }
  
  showCurrentSwaps() {
    this.setState({      
	  isMyBoardiesOpen: false,
      isAwaitingSwapsOpen: false,
	  isCurrentSwapsOpen: true,
	  isFinishedSwapsOpen: false
	  });
  }
  
  showFinishedSwaps() {
    this.setState({      
	  isMyBoardiesOpen: false,
      isAwaitingSwapsOpen: false,
	  isCurrentSwapsOpen: false,
	  isFinishedSwapsOpen: true
	  });
  }

  render() {
    return (
      <div className="root-container login-register-container myswaps-container">

        <div className="box-controller">
          <div
            className={"controller " + ((this.state.isMyBoardiesOpen)
            ? "selected-controller"
            : "")}
            onClick={this
            .showMyBoardies
            .bind(this)}>
            <span>Moje boardies</span>
          </div>
          <div
            className={"controller " + (this.state.isAwaitingSwapsOpen
            ? "selected-controller"
            : "")}
            onClick={this
            .showAwaitingSwaps
            .bind(this)}>
            Oczekujące wymiany
          </div>
          <div
            className={"controller " + ((this.state.isCurrentSwapsOpen)
            ? "selected-controller"
            : "")}
            onClick={this
            .showCurrentSwaps
            .bind(this)}>
            Trwające wymiany
          </div>
          <div
            className={"controller " + (this.state.isFinishedSwapsOpen
            ? "selected-controller"
            : "")}
            onClick={this
            .showFinishedSwaps
            .bind(this)}>
            Zakończone wymiany
          </div>
        </div>
 
        <FadeTransition isOpen={this.state.isMyBoardiesOpen}  duration={5}>
          
            <MyBoardies app={this.props.app} />
          
        </FadeTransition>
        <FadeTransition isOpen={this.state.isAwaitingSwapsOpen} duration={5}>
        
            <AwaitingSwaps app={this.props.app} />
      
        </FadeTransition>
        <FadeTransition isOpen={this.state.isCurrentSwapsOpen} duration={5}>

            <CurrentSwaps app={this.props.app} />

        </FadeTransition>
        <FadeTransition isOpen={this.state.isFinishedSwapsOpen} duration={5}>
     
            <FinishedSwaps app={this.props.app} />
    
        </FadeTransition>

      </div>
    );

  }

}
var mb;
class MyBoardies extends Component {
	constructor(props) {
		super(props);
		mb = this;
	}
	render() {
		return (<Container><NewGameList caller={this} userId={this.props.app.state.userId} callerName={"MySwaps"}/></Container>
		)
	}
}
var as;
class AwaitingSwaps extends Component {
  constructor(props) {
    super(props);
	as = this;
    this.state = {
      isAwaitingFromOpen: false,
      isAwaitingToOpen: true,
	  awaitingTo: null,
	  awaitingFrom: null
    };
	this.loadMyAwaitingSwaps();
  }
  
  loadMyAwaitingSwaps() {
	var data= new FormData();
	data.append('userId', mb.props.app.state.userId);
	data.append('sessionId', cookies.get('sessionId'));
	axios.post(`${API_URL}/swap/getMyAwaitingTo`, data)
	.then(function (response) {
		let awTo = response.data;
		axios.post(`${API_URL}/swap/getMyAwaitingFrom`, data)
		.then(function (response) {
			as.setState({awaitingTo: awTo.filter((e) => (e.boardie.status == 2)), awaitingFrom: response.data.filter((e) => (e.boardie.status == 2))});
		})
		.catch(function (error) {
			console.log(error);
		}); 
	})
	.catch(function (error) {
		console.log(error);
	}); 
  }

  showAwaitingFrom() {
    this.setState({      
      isAwaitingFromOpen: true,
      isAwaitingToOpen: false
	  });
  }

  showAwaitingTo() {
    this.setState({      
      isAwaitingFromOpen: false,
      isAwaitingToOpen: true
	  });
  }
	render() {
		return (
      <div className="root-container login-register-container myswaps-container awaiting-container">

        <div className="box-controller">
          <div
            className={"controller " + ((this.state.isAwaitingToOpen)
            ? "selected-controller"
            : "")}
            onClick={this
            .showAwaitingTo
            .bind(this)}>
            Do zatwierdzenia
          </div>
          <div
            className={"controller " + (this.state.isAwaitingFromOpen
            ? "selected-controller"
            : "")}
            onClick={this
            .showAwaitingFrom
            .bind(this)}>
            Wysłane
          </div>
        </div>
 
        <FadeTransition isOpen={this.state.isAwaitingToOpen}  duration={5}>
          
            <AwaitingTo app={this.props.app} caller={this} />
          
        </FadeTransition>
        <FadeTransition isOpen={this.state.isAwaitingFromOpen} duration={5}>
        
            <AwaitingFrom app={this.props.app} caller={this} />
      
        </FadeTransition>

      </div>
		)
	}
}

class AwaitingSwap extends Component {
	
	cancelSwap() {
		var data= new FormData();
		data.append('swapId', this.props.swap.id);
		data.append('userId', this.props.swap.boardie.user.id);
		data.append('sessionId', cookies.get('sessionId'));
		axios.post(`${API_URL}/swap/cancelByLender`, data)
		.then(function (response) {
			if(response.data)
				location.reload();
		})
		.catch(function (error) {
			console.log(error);
		}); 
	}
	cancelSwapByBorrower() {
		var data= new FormData();
		data.append('swapId', this.props.swap.id);
		data.append('userId', this.props.swap.borrower.id);
		data.append('sessionId', cookies.get('sessionId'));
		axios.post(`${API_URL}/swap/cancel`, data)
		.then(function (response) {
			if(response.data)
				location.reload();
		})
		.catch(function (error) {
			console.log(error);
		}); 
	}
	
	confirmSwap() {
		var data= new FormData();
		data.append('boardieId', this.props.swap.boardie.id);
		data.append('userId', this.props.swap.boardie.user.id);
		data.append('sessionId', cookies.get('sessionId'));
		axios.post(`${API_URL}/swap/confirm`, data)
		.then(function (response) {
			if(response.data)
				location.reload();
		})
		.catch(function (error) {
			console.log(error);
		}); 
	}
	
	visitGame() {
		window.location.replace('/game?g='+this.props.swap.boardie.game.id);
	}
	
	render() {
		let tradeUser;
		let confirmButton;
		let cancelButton;
		if(this.props.isAwaitingTo) {
			tradeUser = this.props.swap.borrower;
			confirmButton = <Button onClick={this.confirmSwap.bind(this)} primary><Icon name="check" />&nbsp;Potwierdź</Button>;
			cancelButton = <Button onClick={this.cancelSwap.bind(this)} secondary><Icon name="cancel" />&nbsp;Anuluj</Button>;
		}
		else {
			tradeUser = this.props.swap.boardie.user;
			confirmButton = "";
			cancelButton = <Button onClick={this.cancelSwapByBorrower.bind(this)} secondary><Icon name="cancel" />&nbsp;Anuluj</Button>;
		}
		return (
		<Container className="cards-wrapper">
		  <Card className="swapCard">
			<Image src={this.props.swap.boardie.game.img} wrapped ui={false} />
			<Card.Content>
			  <Card.Header id="name">{this.props.swap.boardie.game.title}</Card.Header>
			  <Card.Meta className="date-wrapper">
				<span className='date' id="joined">&nbsp;</span>
			  </Card.Meta>
			  <Card.Description id="buttonWrapper" className="profile-button-wrapper">
			  <p>&nbsp;</p>
			  <p>&nbsp;</p>
				<Button primary id="messageButton" onClick={this.visitGame.bind(this)} >Zobacz grę</Button>
			  </Card.Description>
			</Card.Content>
		  </Card>
		  
		  
		  <Card.Group className="info-cards">
			<Card>
			  <Card.Content className="city">
				<Card.Header content='OPERACJE' />
	<Card.Description id="city">{confirmButton}{cancelButton}</Card.Description>
			  </Card.Content>
			</Card>
			<Card>
			  <Card.Content className="city">
				<Card.Header content='CENA' />
				<Card.Description id="gamesCount" >{this.props.swap.boardie.price}&nbsp;<Icon name="universal access" /></Card.Description>
			  </Card.Content>
			</Card>
		  </Card.Group>
		  <Card className="swapCard">
			<Image src={defaultAvatar} wrapped ui={false} />
			<Card.Content>
			  <Card.Header id="name">{tradeUser.name} {tradeUser.surname}</Card.Header>
			  <Card.Meta className="date-wrapper">
				<span className='date' id="joined">{tradeUser.username}</span>
			  </Card.Meta>
			  <Card.Description id="buttonWrapper" className="profile-button-wrapper">
			  <p>{tradeUser.city}</p>
			  <p><Icon name="phone" />&nbsp; {tradeUser.phone}</p>
				<Button primary id="messageButton" ><Icon name="envelope" />Wiadomość</Button>
			  </Card.Description>
			</Card.Content>
		  </Card>
		  </Container>
		)
	}
}

class AwaitingTo extends Component {
	render() {
		let at = this.props.caller.state.awaitingTo;
		if(at == null || this.props.caller.state.awaitingTo.length == 0) {
			return (<p>Brak gier do zatwierdzenia</p>)
		}
		else
		return (
<Container>
{at.map(item => <AwaitingSwap swap={item} isAwaitingTo={true} key={Math.random()} />)}</Container>
		)
	}
}

class AwaitingFrom extends Component {
	render() {
		let at = this.props.caller.state.awaitingFrom;
		if(at == null || this.props.caller.state.awaitingFrom.length == 0) {
			return (<p>Brak gier oczekujących</p>)
		}
		else
		return (
<Container>
{at.map(item => <AwaitingSwap swap={item} isAwaitingTo={false} key={Math.random()} />)}</Container>
		)
	}
}
var cs;
class CurrentSwaps extends Component {
	constructor(props) {
		super(props);
		cs = this;
		this.state = {
			toMeGames: null,
			fromMeGames: null
		};
		this.loadCurrentGames();
	}
	
	loadCurrentGames() {
		var data= new FormData();
		data.append('userId', mb.props.app.state.userId);
		data.append('sessionId', cookies.get('sessionId'));
		axios.post(`${API_URL}/swap/getMyCurrentTo`, data)
		.then(function (response) {
			let cTo = response.data;
			axios.post(`${API_URL}/swap/getMyCurrentFrom`, data)
			.then(function (response) {
				cs.setState({toMeGames: cTo.filter((e) => (e.boardie.status == 0)), fromMeGames: response.data.filter((e) => (e.boardie.status == 0))});
			})
			.catch(function (error) {
				console.log(error);
			}); 
		})
		.catch(function (error) {
			console.log(error);
		}); 
  }
	
	render() {
		let tm = this.state.toMeGames;
		let fm = this.state.fromMeGames;
		return (<Container>
			{(tm == null)
				? ''
				: tm.map(item => <DoneSwap swap={item} key={Math.random()} isTo={true} />)}
			{(fm == null)
				? ''
				: fm.map(item => <DoneSwap swap={item} key={Math.random()} isTo={false} />)}</Container>
		)
	}
}

var fs;
class FinishedSwaps extends Component {
	constructor(props) {
		super(props);
		cs = this;
		this.state = {
			toMeGames: null,
			fromMeGames: null
		};
		this.loadPastGames();
	}
	
	loadPastGames() {
		var data= new FormData();
		data.append('userId', mb.props.app.state.userId);
		data.append('sessionId', cookies.get('sessionId'));
		axios.post(`${API_URL}/swap/getMyPastTo`, data)
		.then(function (response) {
			let pTo = response.data;
			axios.post(`${API_URL}/swap/getMyPastFrom`, data)
			.then(function (response) {
				cs.setState({toMeGames: pTo.filter((e) => (e.boardie.status == 0)), fromMeGames: response.data.filter((e) => (e.boardie.status == 0))});
			})
			.catch(function (error) {
				console.log(error);
			}); 
		})
		.catch(function (error) {
			console.log(error);
		}); 
  }
	
	render() {
		let tm = this.state.toMeGames;
		let fm = this.state.fromMeGames;
		return (<Container>
			{(tm == null)
				? ''
				: tm.map(item => <DoneSwap swap={item} key={Math.random()} isTo={true} />)}
			{(fm == null)
				? ''
				: fm.map(item => <DoneSwap swap={item} key={Math.random()} isTo={false} />)}</Container>
		)
	}
}

class DoneSwap extends Component {
	
	visitGame() {
		window.location.replace('/game?g='+this.props.swap.boardie.game.id);
	}
	
	render() {
		let tradeUser;
		let headerText;
		let expDat = new Date(this.props.swap.expirationDate * 1000);
		let swapDat = new Date(this.props.swap.swapDate * 1000);
		if(this.props.isTo) {
			tradeUser = this.props.swap.boardie.user;
			headerText= "WYPOŻYCZONA TOBIE";
		}
		else {
			tradeUser = this.props.swap.borrower;
			headerText = "WYPOŻYCZONA PRZEZ CIEBIE";
		}
		return (
		<Container className="cards-wrapper">
		  <Card className="swapCard">
			<Image src={this.props.swap.boardie.game.img} wrapped ui={false} />
			<Card.Content>
			  <Card.Header id="name">{this.props.swap.boardie.game.title}</Card.Header>
			  <Card.Meta className="date-wrapper">
				<span className='date' id="joined">&nbsp;</span>
			  </Card.Meta>
			  <Card.Description id="buttonWrapper" className="profile-button-wrapper">
			  <p>&nbsp;</p>
			  <p>&nbsp;</p>
				<Button primary id="messageButton" onClick={this.visitGame.bind(this)} >Zobacz grę</Button>
			  </Card.Description>
			</Card.Content>
		  </Card>
		  
		  
		  <Card.Group className="info-cards">
			<Card>
			  <Card.Content className="city">
				<Card.Header><strong>{headerText}</strong></Card.Header>
			  </Card.Content>
			</Card>
			<Card>
			  <Card.Content className="city">
				<Card.Header content='TERMINY' />
	<Card.Description id="city" className="dateDescription"><strong>OD: </strong><span>{this.props.swap.swapDate}</span></Card.Description>
	<Card.Description id="city" className="dateDescription"><strong>DO: </strong><span>{this.props.swap.expirationDate}</span></Card.Description>
			  </Card.Content>
			</Card>
			<Card>
			  <Card.Content className="city">
				<Card.Header content='CENA' />
				<Card.Description id="gamesCount" >{this.props.swap.boardie.price}&nbsp;<Icon name="universal access" /></Card.Description>
			  </Card.Content>
			</Card>
		  </Card.Group>
		  <Card className="swapCard">
			<Image src={defaultAvatar} wrapped ui={false} />
			<Card.Content>
			  <Card.Header id="name">{tradeUser.name} {tradeUser.surname}</Card.Header>
			  <Card.Meta className="date-wrapper">
				<span className='date' id="joined">{tradeUser.username}</span>
			  </Card.Meta>
			  <Card.Description id="buttonWrapper" className="profile-button-wrapper">
			  <p>{tradeUser.city}</p>
			  <p><Icon name="phone" />&nbsp; {tradeUser.phone}</p>
				<Button primary id="messageButton" ><Icon name="envelope" />Wiadomość</Button>
			  </Card.Description>
			</Card.Content>
		  </Card>
		  </Container>
		)
	}
}

export default MySwaps;