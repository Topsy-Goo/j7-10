package ru.gb.antonov.j710.order.repositos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.order.entities.Order;

import java.util.List;

@Repository
public interface OrdersRepo extends JpaRepository<Order, Long> {

    List<Order> findAllByOuruserId (Long ouruserId);
}
