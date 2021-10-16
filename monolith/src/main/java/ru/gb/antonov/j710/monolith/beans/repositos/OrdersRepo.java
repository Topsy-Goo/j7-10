package ru.gb.antonov.j710.monolith.beans.repositos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.monolith.entities.Order;
import ru.gb.antonov.j710.monolith.entities.OurUser;

import java.util.List;

@Repository
public interface OrdersRepo extends JpaRepository<Order, Long>
{
    List<Order> findAllByOuruser (OurUser ouruser);
}
