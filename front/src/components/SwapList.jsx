import React, { Component } from 'react'
import { Card, Container, Icon  } from 'semantic-ui-react'


class Swap extends Component {
	
	changeActive(e) {
		let elem = document.getElementById(e);
		if(!elem.classList.contains('expensive')) {
			document.querySelector('.activeBoardie').classList.remove('activeBoardie');
			document.getElementById(e).classList.add('activeBoardie');
		}
	}
	
	goToProfile(e) {
		window.location.replace('/profile?u='+e);
	}
	
	render() {
		let cardClass="modalGameListElement";
		if(this.props.caller.city == this.props.boardie.user.city) cardClass = cardClass + " myCity";
		if(this.props.caller.postalCode == this.props.boardie.user.postalCode) cardClass = cardClass + " myPostalCode";
		if(this.props.caller.balance < this.props.boardie.price) {
			cardClass = cardClass + " expensive";
		}
		if(this.props.idx == 0) cardClass = cardClass + " activeBoardie"
		return (
			<Card id={this.props.boardie.id} className={cardClass} onClick={e => this.changeActive(this.props.boardie.id)}>
			<div className="flexer name-flexer" onClick={e => this.goToProfile(this.props.boardie.user.id)}><Card.Header onClick={e => this.goToProfile(this.props.boardie.user.id)}>{this.props.boardie.user.name} {this.props.boardie.user.surname}</Card.Header>
			<Card.Meta onClick={e => this.goToProfile(this.props.boardie.user.id)}>{this.props.boardie.user.username}</Card.Meta></div>
			<Card.Description className="flexer" >{this.props.boardie.user.city}</Card.Description>
			<Card.Description className="flexer coins">{this.props.boardie.price}&nbsp;<Icon name="access universal" /></Card.Description>
			</Card>
		)
	}
}

class SwapList extends Component {
	
	render() {
		return (
		<Container className="modalCardsWrapper">
			<Card.Group itemsPerRow={1} className='notFloating'>
				{this.props.boardiesList.map((item, index) => <Swap boardie={item} idx={index} caller={this.props.caller} key={Math.random()} />)}
			</Card.Group>
		</Container>
		)
	}
}

export default SwapList