package com.shopping.order.service;

import com.shopping.order.dto.InventoryResponse;
import com.shopping.order.entity.OrderItems;
import com.shopping.order.repository.OrderRepository;
import com.shopping.order.dto.OrderItemRequest;
import com.shopping.order.dto.OrderRequest;
import com.shopping.order.entity.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;

    public void placeOrder(OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderItems> orderItems = orderRequest.getOrderItemRequests().stream().map(this::mapToOderItem).toList();
        order.setOrderItems(orderItems);

        List<String> skuCodes = order.getOrderItems().stream().map(OrderItems::getSkuCode).toList();

        // Check Product available or not then place order
        InventoryResponse[] inventoryResponses = webClient.get()
                .uri("http://Inventory-Service/api/inventory", uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();

        System.out.println(Arrays.stream(inventoryResponses).findAny());

        boolean allProductInStocks = Arrays.stream(inventoryResponses).allMatch(InventoryResponse::isInStock);

        if (allProductInStocks){
//            orderRepository.save(order);
            System.out.println(allProductInStocks);

        } else {

            throw new IllegalArgumentException("Product is not is stock please try again");
//            System.out.println("Product is not in stock, please try again later");
        }

    }

    private OrderItems mapToOderItem(OrderItemRequest orderItemRequest) {

        OrderItems orderItems = new OrderItems();
        orderItems.setSkuCode(orderItemRequest.skuCode());
        orderItems.setPrice(orderItemRequest.price());
        orderItems.setQuantity(orderItemRequest.quantity());

        return orderItems;
    }
}
