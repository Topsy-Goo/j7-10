package ru.gb.antonov.j710.monolith.beans.services;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.ResourceNotFoundException;
import ru.gb.antonov.j710.monolith.beans.repositos.ProductRepo;
import ru.gb.antonov.j710.monolith.beans.repositos.specifications.ProductSpecification;
import ru.gb.antonov.j710.monolith.entities.*;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static ru.gb.antonov.j710.monolith.Factory.NO_FILTERS;

@Service
@RequiredArgsConstructor
public class ProductService
{
    private final ProductRepo productRepo;
    private final ProductCategoryService productCategoryService;
    private final OurUserService ourUserService;
    //private final OrderService orderService;  кольцует

//названия фильтров, использующиеся на фронте:
    private static final String FILTER_MIN_PRICE = "min_price";
    private static final String FILTER_MAX_PRICE = "max_price";
    private static final String FILTER_TITLE = "title";
//-----------------------------------------------------------------------

/** @throws ResourceNotFoundException */
    public Product findById (Long id)
    {
        String errMessage = "Не найден продукт с id = "+ id;
        return productRepo.findById(id)
                          .orElseThrow (()->new ResourceNotFoundException (errMessage));
    }

/** @param from {@code productId} первого элемента интервала.
    @param to {@code productId} последнего элемента интервала (включительно). */
    public List<Product> findAllByIdBetween (Long from, Long to)
    {
        String errMsg = String.format ("Не могу получить все товары из диапазона id: %d…%d.", from, to);
        return productRepo.findAllByIdBetween (from, to)
                          .orElseThrow (()->new ResourceNotFoundException (errMsg));
    }

    public Page<Product> findAll (int pageIndex, int pageSize, @Nullable MultiValueMap<String, String> filters)
    {
        pageIndex = validatePageIndex (pageIndex, pageSize, productRepo.count());
        return (filters == NO_FILTERS)
                    ? productRepo.findAll (PageRequest.of (pageIndex, pageSize))
                    : productRepo.findAll (constructSpecification (filters), PageRequest.of (pageIndex, pageSize));
    }

    private int validatePageIndex (int pageIndex, int pageSize, long productsCount)
    {
        int pagesCount    = (int)(productsCount / pageSize);

        if (productsCount % pageSize > 0)
            pagesCount ++;

        if (pageIndex >= pagesCount)
            pageIndex = pagesCount -1;

        return Math.max(pageIndex, 0);
    }

    @Transactional
    public Page<ProductDto> getPageOfProducts (int pageIndex, int pageSize, @Nullable MultiValueMap<String, String> filters)
    {
        return findAll (pageIndex, pageSize, filters).map(Product::toProductDto);
    }
//-------------- Редактирование товаров ---------------------------------

    @Transactional
    public Product createProduct (String title, double price, int rest, String productCategoryName)
    {
        Product p = new Product();
        ProductsCategory category = productCategoryService.findByName (productCategoryName); //< бросает ResourceNotFoundException
        p.update (title, price, rest, category);     //< бросает UnableToPerformException
        return productRepo.save (p);
    }

/** @param id {@code ID} продукта, свойства которого нужно изменить. Этот параметр не может быть {@code null}.
Любой другой параметр может быть {@code null}. Равенство параметра {@code null} расценивается как
нежелание изменять соответствующее ему свойство товара. */
    @Transactional
    public Product updateProduct (@NotNull Long id, String title, Double price, Integer rest, String productCategoryName)
    {
        Product p = findById (id);
        ProductsCategory category = null;

        if (productCategoryName != null)
            category = productCategoryService.findByName (productCategoryName);

        p.update (title, price, rest, category);
        return productRepo.save (p);
    }

/** Товар не удаляем, а обнуляем у него поле {@code rest}. В дальнейшем, при попытке добавить
    этот товар в корзину, проверяется его количество, и, т.к. оно равно 0, добавление не происходит.<p>
    Логично будет добавить к этому НЕвозможность показывать такой товар на витрине.*/
    @Transactional
    public void deleteById (Long id)
    {
        //TODO: Редактирование товаров пока не входит в план проекта.
        //TODO: добавить к этому НЕвозможность показывать такой товар на витрине.
        Product p = findById (id);  //< бросает ResourceNotFoundException
        p.setRest (0);
        //productRepo.delete(p);
    }
//-------------- Фильтры ------------------------------------------------
    @NotNull private Specification<Product> constructSpecification (@Nullable MultiValueMap<String, String> params)
    {
        Specification<Product> spec = Specification.where (null); //< создаём пустую спецификацию
        if (params != null)
        {
    //MultiValueMap.getFirst() returns the first value for the specified key, or null if none.
            String s;
            if ((s = params.getFirst (FILTER_MIN_PRICE)) != null && !s.isBlank())
            {
                double minPrice = Integer.parseInt (s);
                spec = spec.and (ProductSpecification.priceGreaterThanOrEqualsTo (minPrice));
            }

            if ((s = params.getFirst (FILTER_MAX_PRICE)) != null && !s.isBlank())
            {
                double maxPrice = Integer.parseInt (s);
                spec = spec.and (ProductSpecification.priceLessThanOrEqualsTo (maxPrice));
            }

            if ((s = params.getFirst (FILTER_TITLE)) != null && !s.isBlank())
            {
                spec = spec.and (ProductSpecification.titleLike (s));
            }
/*  Если, например, понадобится добавить фильтр, состоящий из ряда необязательных элементов, то для него нужно создать отдельную спецификацию, заполнить её при пом. Specification.or(…), а потом добавить в основную спецификайию при пом. Specification.add(…). */
        }
        return spec;
    }
}


