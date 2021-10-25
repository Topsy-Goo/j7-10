package ru.gb.antonov.j710.monolith.beans.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.OurValidationException;
import ru.gb.antonov.j710.monolith.beans.errorhandlers.UnableToPerformException;
import ru.gb.antonov.j710.monolith.beans.services.ProductService;
import ru.gb.antonov.j710.monolith.entities.Product;
import ru.gb.antonov.j710.monolith.entities.dtos.ProductDto;

import java.security.Principal;
import java.util.Optional;
import java.util.stream.Collectors;

import static ru.gb.antonov.j710.monolith.Factory.PROD_PAGESIZE_DEF;

@RestController
@RequestMapping ("/api/v1/products")
@RequiredArgsConstructor
@CrossOrigin ("*")
public class ProductController
{
    private final ProductService productService;

    //@Value ("${views.shop.items-per-page-def}")
    private final int pageSize = PROD_PAGESIZE_DEF;
//--------------------------------------------------------------------

    //http://localhost:18181/api/v1/products/page?p=0
    @GetMapping ("/page")
    public Page<ProductDto> getProductsPage (
            @RequestParam (defaultValue="0", name="p", required=false) Integer pageIndex,
            @RequestParam MultiValueMap<String, String> filters)
    {
        return productService.getPageOfProducts (pageIndex, pageSize, filters);
    }

    //http://localhost:18181/api/v1/products/11
    @GetMapping ("/{id}")
    public ProductDto findById (@PathVariable Long id, @RequestHeader(required=false) String username, Principal principal)//TODO:убрать username и Principal
    {
        if (id == null)
            throw new UnableToPerformException ("Не могу выполнить поиск для товара id: "+ id);
        return productService.findById (id).toProductDto();
    }
//------------------- Редактирование товара ----------------------------

   //http://localhost:18181/api/v1/products   POST
    @PostMapping
    public Optional<ProductDto> createProduct (@RequestBody @Validated ProductDto pdto, BindingResult br)
    //  Нельзя изменять последовательность следующих параметров: @Validated ProductDto pdto, BindingResult br
    {   if (br.hasErrors())
        {   //преобразуем набор ошибок в список сообщений, и пакуем в одно общее исключение (в наше заранее для это приготовленное исключение).
            throw new OurValidationException (br.getAllErrors().stream()
                                                .map (ObjectError::getDefaultMessage)
                                                .collect (Collectors.toList()));
        }
        Product p = productService.createProduct (pdto.getTitle(), pdto.getPrice(), pdto.getRest(),
                                                  pdto.getCategory());
        return toOptionalProductDto (p);
    }

   //http://localhost:18181/api/v1/products   PUT
    @PutMapping
    public Optional<ProductDto> updateProduct (@RequestBody ProductDto pdto)
    {
        Product p = productService.updateProduct (pdto.getProductId(), pdto.getTitle(), pdto.getPrice(),
                                                  pdto.getRest(), pdto.getCategory());
        return toOptionalProductDto (p);
    }

    //http://localhost:18181/api/v1/products/delete/11
    @GetMapping ("/delete/{id}")
    public void deleteProductById (@PathVariable Long id)
    {
        if (id == null)
            throw new UnableToPerformException ("Не могу удалить товар (Unable to delete product) id: "+ id);
        productService.deleteById (id);
    }
//----------------------------------------------------------------------
    private static Optional<ProductDto> toOptionalProductDto (Product p)
    {
        return p != null ? Optional.of (p.toProductDto())
                         : Optional.empty();
    }
}