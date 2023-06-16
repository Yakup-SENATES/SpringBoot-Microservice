package com.example.orderservice.dto;

import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderRequest {
    List<OrderLineItemsDto> orderLineItemsList;

}
