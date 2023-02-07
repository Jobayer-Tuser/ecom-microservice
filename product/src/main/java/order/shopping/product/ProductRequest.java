package order.shopping.product;

import lombok.Builder;

@Builder
public record ProductRequest(long id, String name, String description, double price) {
}
