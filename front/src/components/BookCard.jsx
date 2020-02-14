import React from 'react';
import { Card, Image, Icon, Button } from 'semantic-ui-react';
import { isLogged, coins } from './App.jsx';

const BookCard = book => {
  const { game, title, author, price, img, addToCart, addedCount } = book;
  
  function goToProductPage() {
	  window.location.replace("/productPage?id="+game.id);
  }
  function handleCoinClick() {
	  if(isLogged) {
		  goToProductPage();
	  }
	  else
		  window.location.replace("/login");
  }
  
  function handleFastTrade() {
	  if(isLogged) {
		  console.log("FAST TRADE");
	  }
	  else {
		  window.location.replace("/login");
	  }
  }
  
  let priceToDisplay, cardClass, exchangeText;

  if(isLogged) {
	  priceToDisplay = price;
	  if(price > coins) {
		  cardClass = "expensive";
		  exchangeText = "NIE STAĆ CIĘ NA WYMIANĘ";
	  }
	  else {
		  cardClass= "";
		  exchangeText = "WYMIEŃ SIĘ";
	  }
  }
  else {
	  priceToDisplay = "Zaloguj się, aby zobaczyć cenę";
	  cardClass="";
  }
  return (
    <Card id={price} className={cardClass}>
	<div className="main-wrapper" onClick={goToProductPage}>
      <div className="card-image">
        <Image src={game.img} />
      </div>
      <Card.Content>
        <Card.Header>{game.title}</Card.Header>
        
      </Card.Content>
	  </div>
      <Card.Content extra>
        <a onClick={handleCoinClick}>
          <Icon name="universal access" />
          {priceToDisplay}
        </a>
      </Card.Content>
	  <Button className="exchange-button" onClick={handleFastTrade}>
		  {exchangeText} <Icon name="exchange" />
      </Button>
    </Card>
  );
};

export default BookCard;

      //<Button className="exchange-button" onClick={addToCart.bind(this, book)}>