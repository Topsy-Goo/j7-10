package ru.gb.antonov.j710.order.repositos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.order.entities.OrderState;

@Repository
public interface OrderStatesRepo extends CrudRepository<OrderState, Integer> {

    OrderState findByShortName (String shortName);
}
