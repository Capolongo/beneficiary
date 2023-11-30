package br.com.livelo.br.com.livelo.services;

import br.com.livelo.br.com.livelo.dto.UserDTO;
import br.com.livelo.br.com.livelo.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {

    Page<User> retrieveUsers(Pageable pageable);

    UserDTO create(UserDTO user);

    User findById(Integer id);
}
