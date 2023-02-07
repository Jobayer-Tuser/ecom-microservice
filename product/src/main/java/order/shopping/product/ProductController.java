package order.shopping.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    ProductService productService;
    @PostMapping("store")
    @ResponseStatus(HttpStatus.CREATED)
    public void create(@RequestBody ProductRequest productRequest){
        productService.createNewProduct(productRequest);
    }

    @GetMapping("index")
    @ResponseStatus(HttpStatus.OK)
    public List<ProductRequest> get(){
        return productService.getAllProducts();
    }
}
