package com.example.inventoryservice.service;

import com.example.inventoryservice.dto.InventoryResponse;
import com.example.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository repository;
    @Transactional(readOnly = true)
    @SneakyThrows // exception handling. Use custom exception handling in production
    public List<InventoryResponse> isInStock(List<String> skuCode){
        log.info("::isInStock wait started");
        try {
            Thread.sleep(5000); // slow behaviour is simulated
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("::isInStock wait finished");
        return repository.findBySkuCodeIn(skuCode).stream()
                .map(i ->
                    InventoryResponse.builder()
                            .skuCode(i.getSkuCode())
                            .isInStock(i.getQuantity() > 0)
                            .build() ).toList();
    }
}
