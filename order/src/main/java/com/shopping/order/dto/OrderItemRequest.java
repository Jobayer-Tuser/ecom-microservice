package com.shopping.order.dto;

public record OrderItemRequest(String skuCode, double price, int quantity) {
}
