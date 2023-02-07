package order.shopping.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ProductService {
    @Autowired
    private ProductRepository productRepository;
    public void createNewProduct(ProductRequest productRequest){

        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);

        log.info("Product {}  is saved", product.getId());
    }

    public List<ProductRequest> getAllProducts() {

        List<Product> products = productRepository.findAll();
        return products.stream().map(this::mapProductResponse).toList();
    }

    private ProductRequest mapProductResponse(Product product){
        return ProductRequest.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }
}
