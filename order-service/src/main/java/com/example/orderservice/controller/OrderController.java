package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod") // burdan inventory çağırıldığı için
    @TimeLimiter(name="inventory")
    public String placeOrder(@RequestBody OrderRequest request){
        service.placeOrder(request);
        return "Order placed successfully";
    }

    public String fallbackMethod(OrderRequest request, RuntimeException exception){
        return "Oops! Something went wrong, please order after some time!";
    }


}
