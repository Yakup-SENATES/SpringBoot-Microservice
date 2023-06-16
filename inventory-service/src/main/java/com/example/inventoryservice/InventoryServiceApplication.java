package com.example.inventoryservice;

import com.example.inventoryservice.model.Inventory;
import com.example.inventoryservice.repository.InventoryRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableEurekaClient
public class InventoryServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(InventoryServiceApplication.class, args);
    }

    @Bean
    public CommandLineRunner loadData(InventoryRepository repository) {
        return (args) -> {
            Inventory inventory = new Inventory();
            inventory.setSkuCode("sku-1");
            inventory.setQuantity(10);
            repository.save(inventory);

            Inventory inventory2 = new Inventory();
            inventory2.setSkuCode("sku-2");
            inventory2.setQuantity(20);
            repository.save(inventory2);
        };
    }
}
