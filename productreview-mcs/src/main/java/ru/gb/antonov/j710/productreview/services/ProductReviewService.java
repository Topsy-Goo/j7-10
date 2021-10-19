package ru.gb.antonov.j710.productreview.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j710.cart.integration.CartToOurUserCallService;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.beans.services.ProductService;
import ru.gb.antonov.j710.monolith.entities.OurUser;
import ru.gb.antonov.j710.monolith.entities.Product;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductReviewDto;
import ru.gb.antonov.j710.order.entities.OrderItem;
import ru.gb.antonov.j710.order.services.OrderService;
import ru.gb.antonov.j710.order.services.OrderStatesService;
import ru.gb.antonov.j710.productreview.entities.ProductReview;
import ru.gb.antonov.j710.productreview.repositos.ProductReviewsRepo;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReviewService
{
    private final ProductReviewsRepo       productReviewsRepo;
    private final ProductService           productService;
    //private final OurUserService     ourUserService;
    private final CartToOurUserCallService cartToOurUserCallService;
    private final OrderStatesService       orderStatesService;
    private final OrderService             orderService;

    @Transactional
    public List<ProductReviewDto> getReviewListById (Long pid)
    {
        //Product product = productService.findById (pid);
        List<ProductReviewDto> reviews = productReviewsRepo.findAllByProductId (pid)
                                                           .stream()
                                                           .map(ProductReview::toProductReviewDto)
                                                           .collect (Collectors.toList ());
        return reviews;
    }

    @Transactional
    public void newProductReview (Long pid, String text, Principal principal)
    {
        if (pid == null || principal == null || text == null || text.isBlank())
            throw new BadCreationParameterException ("Не могу выполнить запрошенное действие.");

        Long ouruserId = cartToOurUserCallService.userIdByLogin (principal.getName());

        ProductReview review = new ProductReview();
        review.setText (text);
        review.setOuruserId (ouruserId);
        review.setProductId (pid);
        productReviewsRepo.save (review);
    }

/** Юзер может оставить один отзыв к товару, если он этот товар купил хотя бы один раз. */
    @Transactional
    public Boolean canReview (Principal principal, Long pid)
    {
        boolean ok = false;
        if (principal != null && pid != null)
        {
            //OurUser ourUser = cartToOurUserCallService.userByPrincipal (principal);
            Long uid = cartToOurUserCallService.userIdByLogin (principal.getName())/*ourUser.getId()*/;
        //проверяем отсутствие отзывов юзера на товар:
            if (productReviewsRepo.findByProductIdAndOuruserId (pid, uid).isEmpty())
            {
        //товар должен числиться в оплаченном заказе:
                Integer stateId = orderStatesService.getOrderStatePayed().getId(); //PAYED
                Collection<OrderItem> orderItems = orderService.userOrderItemsByProductId (uid, pid, stateId);
                ok = !orderItems.isEmpty();
            }
        }
        return ok;
    }
}
