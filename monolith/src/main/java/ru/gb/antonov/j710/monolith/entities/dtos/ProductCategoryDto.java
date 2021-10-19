package ru.gb.antonov.j710.monolith.entities.dtos;

import org.hibernate.validator.constraints.Length;
import ru.gb.antonov.j710.monolith.entities.ProductsCategory;

import javax.validation.constraints.NotNull;

import static ru.gb.antonov.j710.monolith.Factory.PRODCAT_NAMELEN_MAX;
import static ru.gb.antonov.j710.monolith.Factory.PRODCAT_NAMELEN_MIN;

public class ProductCategoryDto
{
    private Long             id;

    @NotNull (message="Не указано название категории товара!")
    @Length (min= PRODCAT_NAMELEN_MIN,
             max= PRODCAT_NAMELEN_MAX,
             message="Длина названия категории товара: 1…255 символов!")
    private String           name;
//-----------------------------------------------------------------------
    public ProductCategoryDto(){}
    public ProductCategoryDto (ProductsCategory category)
    {
        this.id = category.getId();
        this.name = category.getName();
    }

    public Long getId ()    {    return id;    }

    public void setId (Long id)    {    this.id = id;    }

    public String getName ()    {    return name;    }

    public void setName (String name)    {    this.name = name;    }

}
