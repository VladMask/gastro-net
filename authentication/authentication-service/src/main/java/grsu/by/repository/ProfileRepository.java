package grsu.by.repository;

import grsu.by.entity.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {

    Optional<Profile> findByEmail(String email);

    @Query("SELECT p FROM Profile p JOIN FETCH p.roles WHERE p.email = :email")
    Optional<Profile> findByEmailWithRoles(@Param("email") String email);
}
