package com.example.orderservice.service;

import com.example.orderservice.dto.InventoryResponse;
import com.example.orderservice.dto.OrderLineItemsDto;
import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderLineItems;
import com.example.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository repository;
    private final WebClient.Builder webClientBuilder;

    public String placeOrder(OrderRequest request) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = request.getOrderLineItemsList().stream().map(this::mapToDto).toList();
        order.setOrderLineItemsList(orderLineItems);

        List<String> skuCodes = order.getOrderLineItemsList().stream().map(
                OrderLineItems::getSkuCode
        ).toList();


        //Call Inventory Service, and place order if product is in stock
        InventoryResponse[] response = webClientBuilder.build().get().uri("http://inventory-service/api/inventory",
                        uriBuilder -> uriBuilder.queryParam("sku-code", skuCodes).build())
                .retrieve()
                .bodyToMono(InventoryResponse[].class)
                .block();
        if (response == null ){
            return "::placeOrder Error while getting inventory";
        }
        boolean allProductsInStock = Arrays.stream(response).allMatch(InventoryResponse::isInStock);

        if (!allProductsInStock)
            throw new RuntimeException("Product is not in stock, please try another love ");

        order.setOrderNumber(UUID.randomUUID().toString());
        Order save = repository.save(order);
        return "Order placed successfully and the order number is " + save.getOrderNumber() + " and the total amount is " + save.getOrderLineItemsList().get(0).getPrice();
    }

    private OrderLineItems mapToDto(OrderLineItemsDto i) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setId(i.getId());
        orderLineItems.setQuantity(i.getQuantity());
        orderLineItems.setPrice(i.getPrice());
        orderLineItems.setSkuCode(i.getSkuCode());
        return orderLineItems;
    }


}
