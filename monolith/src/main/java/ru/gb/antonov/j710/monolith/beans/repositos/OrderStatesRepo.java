package ru.gb.antonov.j710.monolith.beans.repositos;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.gb.antonov.j710.monolith.entities.OrderState;

@Repository
public interface OrderStatesRepo extends CrudRepository<OrderState, Integer>
{
    OrderState findByShortName (String shortName);
}
