package ru.gb.antonov.j710.productreview.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.gb.antonov.j710.productreview.dtos.ProductReviewDto;

import javax.persistence.*;
import java.time.LocalDateTime;

import static ru.gb.antonov.j710.monolith.Factory.NO_STRING;

@Data
@NoArgsConstructor
@Entity
@Table (name="productreviews")
public class ProductReview {

    @Id    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column(name="text", nullable=false, length=2048)
    private String text;

    @Column(name="ouruser_id", nullable=false)
    private Long ouruserId;

    @Column(name="product_id", nullable=false)
    private Long productId;

    @CreationTimestamp    @Column(name="created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp    @Column(name="updated_at")
    private LocalDateTime updatedAt;
//-----------------------------------------------------------
    private void setId (Long value) { id = value; }
    private void setUpdatedAt (LocalDateTime value) { updatedAt = value; }
    private void setCreatedAt (LocalDateTime value) { createdAt = value; }
//-----------------------------------------------------------

    public ProductReviewDto toProductReviewDto () {
        return new ProductReviewDto (ouruserId, NO_STRING, text, createdAt, productId);
    }

    @Override public String toString() {
        return String.format ("review:[%d, u:%s, p:%s, «%s»]", id, ouruserId, productId, text);
    }
}
