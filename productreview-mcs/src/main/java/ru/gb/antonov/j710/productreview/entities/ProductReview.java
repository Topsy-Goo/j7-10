package ru.gb.antonov.j710.productreview.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import ru.gb.antonov.j710.monolith.entities.OurUser;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductReviewDto;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table (name="productreviews")
public class ProductReview
{
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column(name="text", nullable=false)
    private String text;

/*    @ManyToOne
    @JoinColumn(name="ouruser_id", nullable=false)
    private OurUser ourUser;*/
    @Column(name="ouruser_id", nullable=false)
    private Long ouruserId;

    @Column(name="product_id", nullable=false)
    private Long productId;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name="updated_at", nullable=false)
    private LocalDateTime updatedAt;
//-----------------------------------------------------------
    public ProductReviewDto toProductReviewDto (/*String nikname*/)
    {
        return new ProductReviewDto (ouruserId/*nikname/*ourUser.getLogin()*/, text, createdAt, productId);
    }
    @Override public String toString()
    {   return String.format ("review:[%d, u:%s, p:%s, «%s»]",
                              id,
                              ouruserId/*(ourUser == null) ? null : ourUser.getLogin()*/,
                              productId,
                              text);
    }
}
