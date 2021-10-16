package ru.gb.antonov.j710.monolith.entities;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.jetbrains.annotations.NotNull;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.BadCreationParameterException;
import ru.gb.antonov.j710.monolith.beans.soap.products.ProductSoap;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.util.Objects;

import static ru.gb.antonov.j710.monolith.Factory.MAX_PRICE;
import static ru.gb.antonov.j710.monolith.Factory.MIN_PRICE;

@Entity
@Table (name="products")
public class Product
{
    @Id  @Getter
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    @Column (name="id")
    private Long id;

    @Column(name="title", nullable=false)  @Getter
    private String title;

    @Column(name="price")  @Getter
    private double price;

    @Column(name="rest")  @Getter
    private int rest;

    @ManyToOne
    @JoinColumn(name="category_id", nullable=false)  @Getter
    private ProductsCategory category;

    @CreationTimestamp
    @Column(name="created_at", nullable=false)  @Getter @Setter
    private LocalDateTime createdAt;

    @CreationTimestamp
    @Column(name="updated_at", nullable=false)  @Getter @Setter
    private LocalDateTime updatedAt;
//----------------------------------------------------------------------
    public Product(){}

/** Любой из параметров может быть {@code null}. Равенство параметра {@code null} расценивается как
нежелание изменять соответствующее ему свойство товара..
@throws  BadCreationParameterException*/
    public Product update (String ttl, Double prc, Integer rst, ProductsCategory cat)
    {
        String newTitle = (ttl == null) ? title : ttl;
        Double newPrice = (prc == null) ? price : prc;
        Integer newRest = (rst == null) ? rest : rst;
        ProductsCategory newCat = (cat == null) ? category : cat;

        if (!setTitle (newTitle) || !setPrice (newPrice) || !setRest (newRest) || !setCategory (newCat))
        {
            String newCategoryName = (newCat != null) ? newCat.getName() : "null";
            String sb = "Недопустимый набор значений:\r" +
                        "• название продукта = " + newTitle + ",\r" +
                        "• цена = " + newPrice + ",\r" +
                        "• остаток = " + newRest + ",\r" +
                        "• категория = " + newCategoryName + '.';
            throw new BadCreationParameterException (sb);
        }
        return this;
    }
//(метод используется в тестах, где корректность аргументов зависит от целей тестирования)
    public static Product dummyProduct (Long id, String title, double price, int rest,
                                        ProductsCategory category,
                                        LocalDateTime createdAt, LocalDateTime updatedAt)
    {   Product p = new Product();
        p.id = id;
        p.title = title;
        p.price = price;
        p.rest = rest;
        p.category = category;
        p.createdAt = createdAt;
        p.updatedAt = updatedAt;
        return p;
    }
//----------------- Геттеры и сеттеры -----------------------------------

    private void setId (Long id)   {   this.id = id;   }

    public boolean setTitle (String title)
    {
        boolean ok = isTitleValid (title);
        if (ok)
            this.title = title.trim();
        return ok;
    }

    public boolean setPrice (Double newvalue)
    {
        boolean ok = isPriceValid (newvalue);
        if (ok)
            this.price = newvalue;
        return ok;
    }

    private boolean setCategory (ProductsCategory newcategory)
    {
        boolean ok = newcategory != null;
        if (ok)
            category = newcategory;
        return ok;
    }

    public boolean setRest (Integer newvalue)
    {
        boolean ok = newvalue != null && newvalue >= 0;
        if (ok)
            rest = newvalue;
        return ok;
    }

    public static String getPriceFieldName ()  {   return "price";   }
    public static String getTitleFieldName ()  {   return "title";   }
//-----------------------------------------------------------------------

    public static boolean isTitleValid (String title)
    {
        return title != null  &&  !title.trim().isEmpty();
    }

    public static boolean isPriceValid (double value)
    {
        return value >= MIN_PRICE  &&  value <= MAX_PRICE;
    }

    @Override public boolean equals (Object o)
    {
        if (o == this)  return true;
        if (o == null || getClass() != o.getClass())  return false;
        Product p = (Product) o;
        return this.id.equals(p.getId());
    }

    @Override public int hashCode()    {   return Objects.hash (id);   }

    @Override public String toString()
    {   return String.format ("[id:%d, «%s», %.2f, rt:%d]", id, title, price, rest);
    }

    @NotNull
    public static ProductSoap toProductSoap (Product p)
    {
        if (p != null && p.id != null)
        return new ProductSoap (p.id,
                                p.title,
                                p.price,
                                p.rest,
                                p.getCategory().getName(),
                                p.createdAt.getLong (ChronoField.MILLI_OF_SECOND),
                                p.updatedAt.getLong (ChronoField.MILLI_OF_SECOND));
        return new ProductSoap();
    }
}