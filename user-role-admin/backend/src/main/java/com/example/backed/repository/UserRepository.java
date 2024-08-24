package com.example.backed.repository;

import com.example.backed.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    @Query("SELECT u FROM User u WHERE "
            + "(:name IS NULL OR u.userName LIKE %:name%) "
            + "AND (:minCreateTime IS NULL OR u.createTime >= :minCreateTime) "
            + "AND (:maxCreateTime IS NULL OR u.createTime <= :maxCreateTime)")
    List<User> findUsers(
            @Param("name") String name,
            @Param("minCreateTime") LocalDateTime minCreateTime,
            @Param("maxCreateTime") LocalDateTime maxCreateTime,
            Pageable pageable);

    @Query("SELECT COUNT(u) FROM User u WHERE "
            + "(:name IS NULL OR u.userName LIKE %:name%) "
            + "AND (:minCreateTime IS NULL OR u.createTime >= :minCreateTime) "
            + "AND (:maxCreateTime IS NULL OR u.createTime <= :maxCreateTime)")
    int countUsers(
            @Param("name") String name,
            @Param("minCreateTime") LocalDateTime minCreateTime,
            @Param("maxCreateTime") LocalDateTime maxCreateTime);
}