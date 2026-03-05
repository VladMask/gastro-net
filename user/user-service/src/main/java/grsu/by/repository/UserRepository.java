package grsu.by.repository;

import grsu.by.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u.email from User u where u.id = :id")
    Optional<String> findUserEmailByUserId(Long id);
}
