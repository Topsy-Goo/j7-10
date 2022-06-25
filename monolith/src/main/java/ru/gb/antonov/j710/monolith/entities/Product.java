package ru.gb.antonov.j710.monolith.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

import static ru.gb.antonov.j710.monolith.Factory.*;

@Entity     @Table (name="products")
public class Product implements Buildable<Product> {

    @Id  @Getter
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column(name="title", nullable=false, length=255)             @Getter
    private String title;

    @Column(name="price", nullable=false, precision=10, scale=2)             @Getter
    private BigDecimal price = BigDecimal.ZERO;

    @Column(name="rest", nullable=false)              @Getter
    private Integer rest;

    @ManyToOne
    @JoinColumn(name="measure_id", nullable=false)    @Getter
    private Measure measure;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)   @Getter
    private ProductsCategory category;

    @CreationTimestamp    @Column(name="created_at")  @Getter @Setter
    private LocalDateTime createdAt;

    @UpdateTimestamp     @Column(name="updated_at")  @Getter @Setter
    private LocalDateTime updatedAt;
//----------------------------------------------------------------------
    private Product () {}
/** При создании товара некоторые характеристики должны обязательно заполняться. Остальные могут
быть заполнены позже и/или при необходимости.
@param newTitle наименование товара. Уникальность наименования не проверяется в этом конструкторе.
@param newMeasure единица измерения.
@param newProductCategory категория товара. */
    private Product (String newTitle, Measure newMeasure, ProductsCategory newProductCategory) {
        if (!setTitle (newTitle))
            throw new BadCreationParameterException ("\rнекорректное название продукта : "+ newTitle);
        if (!setMeasure (newMeasure))
            throw new BadCreationParameterException ("\rнекорректная еденица измерения : "+ newMeasure);
        if (!setCategory (newProductCategory))
            throw new BadCreationParameterException ("\rнекорректная категория продукта : "+ newProductCategory);
    }
//---------------- создание и обновление объектов ----------------------
/** Создаёт пустой объект Product и начинает цепочку методов, каждый из которых проверяет валидность
изменяемого параметра. Параметрами являются обязательные характеристики товара.
@return ссылка на объект Product */
    public static Product create (String newTitle, Measure newMeasure, ProductsCategory newProductCategory)
    {
        //Делая measure и category обязательными, мы избавляем себя от их проверок на null
        //в таких методах как Product.toProductDto().
        return new Product (newTitle, newMeasure, newProductCategory);
    }

/**
@return this
@throws BadCreationParameterException */
    public Product withPrice (BigDecimal newPrice) {
        if (!setPrice (newPrice))
            throw new BadCreationParameterException ("\rнекорректная цена продукта : " + newPrice);
        return this;
    }

/**
@return this
@throws BadCreationParameterException */
    public Product withRest (Integer newRest) {
        if (!setRest (newRest))
            throw new BadCreationParameterException ("\rнекорректный остаток продукта : " + newRest);
        return this;
    }

/**
@return this */
    @Override public Product build () {
        if (title == null || category == null || price == null || measure == null || rest == null)
            throw new BadCreationParameterException ("\rнедостаточная инициализация");
        return this;
    }

/** Любой из параметров может быть {@code null}. Равенство параметра {@code null} расценивается как
нежелание изменять соответствующее ему свойство товара..
@throws  BadCreationParameterException*/
    public void update (String newTitle, BigDecimal newPrice, Integer newRest, Measure newMeasure,
                        ProductsCategory newProductCategory)
    {
        if (newTitle != null && !setTitle (newTitle))
            throw new BadCreationParameterException ("\rнекорректное название продукта : " + newTitle);

        if (newPrice != null && !setPrice (newPrice))
            throw new BadCreationParameterException ("\rнекорректная цена продукта : " + newPrice);

        if (newRest != null && !setRest (newRest))
            throw new BadCreationParameterException ("\rнекорректный остаток продукта : " + newRest);

        if (newMeasure != null && !setMeasure (newMeasure))
            throw new BadCreationParameterException ("\rнекорректная еденица измерения продукта : "+ newMeasure);

        if (newProductCategory != null && !setCategory (newProductCategory))
            throw new BadCreationParameterException ("\rнекорректная категория продукта : " + newProductCategory);
    }
//----------------- Геттеры и сеттеры -----------------------------------

    private void setId (Long newvalue)   {   this.id = newvalue;   }

    public boolean setTitle (String newvalue) {
        boolean ok = isTitleValid (newvalue);
        if (ok)
            this.title = newvalue.trim();
        return ok;
    }

    public boolean setPrice (BigDecimal newvalue) {
        boolean ok = isPriceValid (newvalue);
        if (ok)
            this.price = newvalue;
        return ok;
    }

    private boolean setMeasure (Measure newvalue) {
        boolean ok = Measure.isMeasureValid (newvalue);
        if (ok)
            measure = newvalue;
        return ok;
    }

    private boolean setCategory (ProductsCategory newvalue) {
        boolean ok = newvalue != null;
        if (ok)
            category = newvalue;
        return ok;
    }

    public boolean setRest (Integer newvalue) {
        boolean ok = newvalue != null && newvalue >= 0;
        if (ok)
            rest = newvalue;
        return ok;
    }

    private void setUpdatedAt (LocalDateTime newvalue) { updatedAt = newvalue; }
    private void setCreatedAt (LocalDateTime newvalue) { createdAt = newvalue; }
//-----------------------------------------------------------------------

    public static boolean isTitleValid (String value)  {
        return sayNoToEmptyStrings (value);
    }

    public static boolean isPriceValid (BigDecimal value) {
        return value.compareTo (MIN_PRICE) >= 0  &&  value.compareTo(MAX_PRICE) <= 0;
    }

    public ProductDto toProductDto ()  {
        return new ProductDto (id, title, price, rest, measure.getName(), category.getName());
    }
//-----------------------------------------------------------------------
    @Override public boolean equals (Object o) {
        if (o == this)  return true;
        if (o == null || getClass() != o.getClass())  return false;
        Product p = (Product) o;
        return this.id.equals(p.getId());
    }

    @Override public int hashCode()    {   return Objects.hash (id);   }

    @Override public String toString() {
        return String.format ("Product:[id:%d, «%s», %.2f, rt:%d, msr:«%s», cat:«%s»]",
                        id, title, price, rest, measure.getName(), category.getName());
    }
}
