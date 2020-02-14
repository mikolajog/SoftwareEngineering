import React from 'react'
import { Button, Header, Image, Modal, Icon  } from 'semantic-ui-react'

const AddBoardieModal = ({modalOpen, game, lowestPrice, caller}) => (
  <Modal className="addModalWrapper" open={modalOpen} closeIcon onClose={(e) => caller.setState({modalOpen: false})}>
    <Modal.Header>{game== null ? '$title' : game.title}</Modal.Header>
    <Modal.Content image>
      <Image wrapped size='medium' src={game== null ? '$img' : game.img} />
      <Modal.Description>
        <p>{game== null ? '$desc' : game.description}</p>
		<p className="nowPrice"><strong>Aktualna najniższa cena:</strong> {lowestPrice== null ? '$lowestPrice' : lowestPrice}&nbsp;<Icon name="universal access" /></p>
		<p className="yourPrice"><strong>Twoja cena:</strong>
		<input
			  id="addPriceFinal"
              type="text"
              name="yourPrice"
              className="login-input modalPriceInput"
			  /> <Icon name="universal access" /></p>
			  
      </Modal.Description>
    </Modal.Content><Button className="modal-add" primary onClick={caller.addExistingBoardie.bind(this)}>DODAJ</Button>
  </Modal>
)

export default AddBoardieModal