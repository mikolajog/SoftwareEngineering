package pl.boardies.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pl.boardies.models.Boardies;
import pl.boardies.models.Swap;


@Repository
public interface SwapRepository extends CrudRepository<Swap, Integer> { 
	Swap findByBoardieId(int boardieId);
	
	@Query(value = 	"SELECT swap.id, swap.expiration_date, swap.swap_date, swap.boardie_id, swap.borrower_id " + 
			"FROM swap " + 
			"INNER JOIN boardies ON swap.boardie_id=boardies.id WHERE boardies.user_id = :givenId", nativeQuery = true)
	List<Swap> findByLender(@Param("givenId") int lenderId);
	
	@Query(value = 	"SELECT swap.id, swap.expiration_date, swap.swap_date, swap.boardie_id, swap.borrower_id " + 
			"FROM swap " + 
			"INNER JOIN boardies ON swap.boardie_id=boardies.id WHERE swap.borrower_id = :givenId", nativeQuery = true)
	List<Swap> findByBorrower(@Param("givenId") int borrowerId);
}
