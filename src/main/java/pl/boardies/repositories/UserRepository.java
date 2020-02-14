package pl.boardies.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pl.boardies.models.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
	User findByUsernameIgnoreCase(String username);
	User findByEmailIgnoreCase(String email);
	User findByPhone(String phone);
}
