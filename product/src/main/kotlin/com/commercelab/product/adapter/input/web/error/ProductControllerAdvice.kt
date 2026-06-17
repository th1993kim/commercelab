package com.commercelab.product.adapter.input.web.error

import com.commercelab.product.application.exception.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class ProductControllerAdvice {

    @ExceptionHandler(ProductNotFoundException::class)
    fun handle(e: ProductNotFoundException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(
                ErrorResponse(
                    errorCode = ErrorCode.PRODUCT_NOT_FOUND.name,
                    message = e.message
                )
            )
    }
}