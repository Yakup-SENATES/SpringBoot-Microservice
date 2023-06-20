package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderRequest;
import com.example.orderservice.service.OrderService;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService service;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CircuitBreaker(name="inventory",fallbackMethod = "fallbackMethod") // application.properties de Resillience4j instances.inventory ismiyle kullanıldı
    @TimeLimiter(name="inventory")
    @Retry(name="inventory")// application.properties de retry instances.inventory ismiyle kullanıldı
    public CompletableFuture<String> placeOrder(@RequestBody OrderRequest request){
        String response = service.placeOrder(request);
        return CompletableFuture.supplyAsync(()->response);
    }

    public CompletableFuture<String> fallbackMethod(OrderRequest request, RuntimeException exception){
        return CompletableFuture.supplyAsync(()->"Oops! Something went wrong, please order after some time!");
    }


}
