import React from 'react';
import { Button, Header, Image, Modal, Icon  } from 'semantic-ui-react';
import SwapList from './SwapList';
import { API_URL, cookies } from './App';
import axios from 'axios';

function goToGame(e) {
	window.location.replace("/game?g="+e);
}

function makeSwap(user) {
	let elem = document.getElementsByClassName('activeBoardie');
	var data = new FormData();
	data.append('userId', user.id);
	data.append('sessionId', cookies.get('sessionId'));
	data.append('boardieId', elem[0].id);
	axios.post(`${API_URL}/swap/prepare`, data)
	.then(function (response) {
		console.log(response.data);
		window.location.replace('/?fp=5');
	})
	.catch(function (error) {
		console.log(error);
	});
	
}

const TradeModal = ({modalOpen, game, caller, callerName, boardiesList, callingUser}) => (
  <Modal className="addModalWrapper swapModal" open={modalOpen} closeIcon onClose={(e) => caller.setState({modalOpen: false})}>
    <Modal.Header>{game== null ? '$title' : game.title}
		{(callerName == "Game") || (caller == null) ? ("") : (<Button className="goToGameModalButton" secondary onClick={(e) => goToGame(game.id)}>ZOBACZ STRONĘ GRY</Button>)}</Modal.Header>
    <Modal.Content image className="whole-modal-wrapper">
      <Image wrapped size='medium' src={game== null ? '$img' : game.img}></Image>
      <Modal.Description className="swapListDescription">
			{boardiesList == null ? ("?boardiesList") : (<SwapList caller={callingUser} boardiesList={boardiesList}/>)}
      </Modal.Description>
    </Modal.Content><Button id="makeSwapButton" className="modal-add" primary onClick={() => makeSwap(callingUser)}>WYMIEŃ SIĘ</Button>
  </Modal>
)

export default TradeModal;