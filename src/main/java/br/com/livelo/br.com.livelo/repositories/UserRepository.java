package br.com.livelo.br.com.livelo.repositories;

import br.com.livelo.br.com.livelo.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
}
