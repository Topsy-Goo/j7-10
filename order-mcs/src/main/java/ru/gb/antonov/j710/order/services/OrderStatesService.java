package ru.gb.antonov.j710.order.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.gb.antonov.j710.order.repositos.OrderStatesRepo;
import ru.gb.antonov.j710.order.entities.OrderState;

import static ru.gb.antonov.j710.monolith.Factory.*;

@Service
@RequiredArgsConstructor
public class OrderStatesService {

    private final OrderStatesRepo orderStatesRepo;

    public OrderState getOrderStateNone ()     { return orderStatesRepo.findByShortName (ORDERSTATE_NONE); }
    public OrderState getOrderStatePending ()  { return orderStatesRepo.findByShortName (ORDERSTATE_PENDING); }
    public OrderState getOrderStateServing ()  { return orderStatesRepo.findByShortName (ORDERSTATE_SERVING); }
    public OrderState getOrderStatePayed ()    { return orderStatesRepo.findByShortName (ORDERSTATE_PAYED); }
    public OrderState getOrderStateCanceled () { return orderStatesRepo.findByShortName (ORDERSTATE_CANCELED); }
}
