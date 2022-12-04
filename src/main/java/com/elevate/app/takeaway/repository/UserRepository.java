package com.elevate.app.takeaway.repository;

import com.elevate.app.takeaway.dto.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByNameAndPassword(String name, String password);
    User findByName(String name);
    @Query("SELECT u FROM User u")
    Optional<List<User>> getAllUsers();

    @Query(value = "SELECT u.* FROM users u, user_address ua where ua.city=?1 and ua.user_id=u.id", nativeQuery = true)
    Optional<List<User>> getUserByCity(String city);
}
