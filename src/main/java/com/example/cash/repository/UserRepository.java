package com.example.cash.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.cash.domain.User;
import com.example.cash.dto.UserJoinSettingDTO;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    public final String SELECT_FROM_USER_JOIN_SETTING = "SELECT u.id, u.email, u.password, u.role, u.username, u.image, s.dark FROM users u JOIN settings s ON s.user_id = u.id WHERE u.email = :email";

    User findByEmail(String email);

    User findByUsername(String username);

    @Query(value = SELECT_FROM_USER_JOIN_SETTING, nativeQuery = true)
    UserJoinSettingDTO findUserJoinSetting(String email);

}
