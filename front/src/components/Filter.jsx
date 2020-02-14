import React from 'react';
import '../filter.css';
import { Input, Menu, Icon } from 'semantic-ui-react';
import axios from 'axios';
import { ngl } from './Games.jsx';
import { API_URL} from './App.jsx';

var activeFilter = 'all';

function search(val) {
	if(val == "") {
		ngl.getAllBoardies();
	}
	else {
		ngl.getSearchedBoardies(val);
	}
}

function sort(ascending) {
	if(ngl === undefined){}
	else {
		switch(ascending) {
			case 1:
				ngl.sort(true);
				document.getElementById(activeFilter).classList.remove('active');
				activeFilter = 'price_low';
				document.getElementById(activeFilter).classList.add('active');
				break;
			case 2:
				ngl.sort(false);
				document.getElementById(activeFilter).classList.remove('active');
				activeFilter = 'price_high';
				document.getElementById(activeFilter).classList.add('active');
				break;
			default:
				document.getElementById(activeFilter).classList.remove('active');
				activeFilter = 'all';
				document.getElementById(activeFilter).classList.add('active');
				search("");
		}
	}
}

const Filter = ({ setFilter, filterBy, searchQuery, setSearchQuery }) => (
  <Menu secondary className="leftFloating">
    <Menu.Item
	 id="all"
      active={activeFilter == 'all'}
      onClick={e => sort(0)}>
      Wszystkie
    </Menu.Item>
    <Menu.Item
	 id="price_low"
      active={activeFilter == 'price_low'}
      onClick={e => sort(1)}>
      Cena rosnąco
    </Menu.Item>
    <Menu.Item
	 id="price_high"
      active={activeFilter == 'price_high'}
      onClick={e => sort(2)}>
      Cena malejąco
    </Menu.Item>
    
    <Menu.Item>
      <Input
        icon="search"
        onChange={e => search(e.target.value)}
        placeholder="Wyszukiwanie..."
      />
    </Menu.Item>
  </Menu>
);
/*
class Filter extends Component {
	render() {
		return (
		
		)
	}
}
*/
export default Filter;
//onChange={e => setSearchQuery(e.target.value)}