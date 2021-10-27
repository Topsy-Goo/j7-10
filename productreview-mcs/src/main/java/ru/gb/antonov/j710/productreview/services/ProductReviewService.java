package ru.gb.antonov.j710.productreview.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductReviewDto;
import ru.gb.antonov.j710.order.entities.OrderItem;
import ru.gb.antonov.j710.productreview.entities.ProductReview;
import ru.gb.antonov.j710.productreview.integration.ProductreviewToOrderCallService;
import ru.gb.antonov.j710.productreview.integration.ProductreviewToOurUserCallService;
import ru.gb.antonov.j710.productreview.repositos.ProductReviewsRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductReviewService
{
    private final ProductReviewsRepo                productReviewsRepo;
    private final ProductreviewToOurUserCallService productreviewToOurUserCallService;
    private final ProductreviewToOrderCallService   productreviewToOrderCallService;

    @Transactional
    public List<ProductReviewDto> getReviewListById (Long pid)
    {
        return productReviewsRepo.findAllByProductId (pid)
                                 .stream()
                                 .map(ProductReview::toProductReviewDto)
                                 .collect (Collectors.toList ());
    }

    @Transactional
    public void newProductReview (Long pid, String text, String username)
    {
        if (pid == null || username == null || text == null || text.isBlank())
            throw new BadCreationParameterException ("Не могу выполнить запрошенное действие.");

        Long ouruserId = productreviewToOurUserCallService.userIdByLogin (username);

        ProductReview review = new ProductReview();
        review.setText (text);
        review.setOuruserId (ouruserId);
        review.setProductId (pid);
        productReviewsRepo.save (review);
    }

/** Юзер может оставить один отзыв к товару, если он этот товар купил хотя бы один раз. */
    @Transactional
    public Boolean canReview (String username, Long pid)
    {
        boolean ok = false;
        if (username != null && pid != null)
        {
        //Убеждаемся, что юзер ещё не написал отзыв на товар pid:
            Long uid = productreviewToOurUserCallService.userIdByLogin (username);
            if (productReviewsRepo.findByProductIdAndOuruserId (pid, uid).isEmpty())
            {
        //Убеждаемся, что юзер купил этот товар:
                ok = 0 < productreviewToOrderCallService.payedOrderItemsCountByUserIdAndProductId(uid, pid);
            }
        }
        return ok;
    }
}
