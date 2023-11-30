package br.com.livelo.br.com.livelo.repositories;

import br.com.livelo.br.com.livelo.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository repository;

    @Test
    void saveUserShouldReturnSavedUser() {
        User userSaved = repository.save(getUser());
        assertNotNull(userSaved);

    }

    @Test
    void findUserByIdShouldReturnAUser() {
        User userSaved = repository.save(getUser());
        Optional<User> result = repository.findById(userSaved.getId());

        assert  result.isPresent();
        assertEquals(userSaved.getId(), result.get().getId());
        assertEquals(userSaved.getEmail(), result.get().getEmail());
        assertEquals(userSaved.getPassword(), result.get().getPassword());
        assertEquals(userSaved.getUsername(), result.get().getUsername());
    }

    @Test
    void findByIdPassingIdZeroShouldReturnOptionalEmpty() {
        assertEquals(repository.findById(0), Optional.empty());
    }


    @Test
    void findAllShouldReturnSavedUsers() {

        User user1 = new User();
        user1.setUsername("test1");
        user1.setEmail("test1@test1.com");
        user1.setPassword("password1");
        user1.setCreationDate(new Date());

        User user2 = new User();
        user2.setUsername("test2");
        user2.setEmail("test2@test2.com");
        user2.setPassword("password2");
        user2.setCreationDate(new Date());

        this.repository.save(user1);
        this.repository.save(user2);

        assertEquals(2, this.repository.findAll(Pageable.unpaged()).getTotalElements());

    }


    private User getUser() {
        User user = new User();
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("password");
        user.setCreationDate(new Date());
        return user;
    }
}
