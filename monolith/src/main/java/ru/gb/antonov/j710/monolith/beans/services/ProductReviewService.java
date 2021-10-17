package ru.gb.antonov.j710.monolith.beans.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.beans.repositos.OrderItemRepo;
import ru.gb.antonov.j710.monolith.beans.repositos.ProductReviewsRepo;
import ru.gb.antonov.j710.monolith.entities.OrderItem;
import ru.gb.antonov.j710.monolith.entities.OurUser;
import ru.gb.antonov.j710.monolith.entities.Product;
import ru.gb.antonov.j710.monolith.entities.ProductReview;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductReviewDto;

import java.security.Principal;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReviewService
{
    private final ProductReviewsRepo productReviewsRepo;
    private final ProductService     productService;
    private final OurUserService     ourUserService;
    private final OrderItemRepo      orderItemRepo;
    private final OrderStatesService orderStatesService;

    @Transactional
    public List<ProductReviewDto> getReviewListById (long pid)
    {
        Product product = productService.findById (pid);
        List<ProductReviewDto> reviews = productReviewsRepo.findAllByProductId (pid)
                                                           .stream()
                                                           .map (ProductReviewDto::new)
                                                           .collect (Collectors.toList ());
        return reviews;
    }

    @Transactional
    public void newProductReview (Long pid, String text, Principal principal)
    {
        if (pid == null || principal == null || text == null || text.isBlank())
            throw new BadCreationParameterException ("Не могу выполнить запрошенное действие.");

        OurUser ourUser = ourUserService.userByPrincipal (principal);

        ProductReview review = new ProductReview();
        review.setText (text);
        review.setOurUser (ourUser);
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
            OurUser ourUser = ourUserService.userByPrincipal (principal);
            Long uid = ourUser.getId();
        //проверяем отсутствие отзывов юзера на товар:
            if (productReviewsRepo.findByProductIdAndOurUser (pid, ourUser).isEmpty())
            {
        //товар должен числиться в оплаченном заказе:
                Integer stateId = orderStatesService.getOrderStatePayed().getId(); //PAYED
                Collection<OrderItem> orderItems = orderItemRepo.userOrderItemsByProductId (uid, pid, stateId);
                ok = !orderItems.isEmpty();
            }
        }
        return ok;
    }
}
