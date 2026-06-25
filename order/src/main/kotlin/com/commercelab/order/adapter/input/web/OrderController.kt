package com.commercelab.order.adapter.input.web

import com.commercelab.order.adapter.input.web.dto.CreateOrderRequest
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/api/orders")
@RestController
class OrderController {

    @PostMapping
    fun createOrder(@Valid @RequestBody request: CreateOrderRequest) {



    }
}