package com.example.universal_shop.Repo;

import com.example.universal_shop.Models.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;

public interface IOrdersRepository extends JpaRepository<Orders, Long> {
    @Query("select o from Orders o where o.user_id = :userId")
    Set<Orders> findByUser_id(long userId);

    Optional<Orders> findById(String id);

    Optional<Orders> findByOrderIdentifier(String orderIdentifier);

    boolean existsById(String id);

    boolean existsByOrderIdentifier(String orderIdentifier);

    @Query("select t.processed from Orders t where t.id = :id")
    boolean existsByProcessedAndId(String id);

    @Query("select t.processed from Orders t where t.orderIdentifier = :orderIdentifier")
    boolean existsByProcessedAndOrderIdentifier(String orderIdentifier);

    @Modifying
    @Transactional
    @Query("delete from Orders r where r.id = :id")
    long deleteById(String id);
}
