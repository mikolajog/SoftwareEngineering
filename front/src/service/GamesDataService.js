import axios from 'axios'

const API_URL = 'http://localhost:8080'
const SEARCH_API_URL = `${API_URL}/games/search/${INSTRUCTOR}`

class GamesDataService {
    retrieveAllGames(name) {
        return axios.get(`${SEARCHR_API_URL}/name`);
    }
}
export default new GamesDataService()