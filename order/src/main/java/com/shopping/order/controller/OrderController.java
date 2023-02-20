package com.shopping.order.controller;

import com.shopping.order.dto.OrderRequest;
import com.shopping.order.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;
    @PostMapping("place-order")
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory", fallbackMethod = "fallbackMethod")
    public String placeOrder(@RequestBody OrderRequest orderRequest) {
        orderService.placeOrder(orderRequest);
        return "Order Created successfully";
    }

    public String fallbackMethod(OrderRequest orderRequest, RuntimeException runtimeException){
        return "Oops something went wrong please try again!";
    }
}
