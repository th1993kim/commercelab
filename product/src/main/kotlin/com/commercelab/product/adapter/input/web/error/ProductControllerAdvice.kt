package com.commercelab.product.adapter.input.web.error

import com.commercelab.product.application.exception.ProductNotFoundException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
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

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(e: MethodArgumentNotValidException): ResponseEntity<ValidationErrorResponse> {
        val errorList = e.bindingResult
            .fieldErrors
            .map {
                ValidationErrorResponse.FieldErrorResponse(
                    field = it.field,
                    message = it.defaultMessage ?: "해당 필드의 입력값이 유효하지 않습니다."
                )
            }

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ValidationErrorResponse(
                    errorCode = ErrorCode.INVALID_INPUT.name,
                    message = "요청 입력값이 유효하지 않습니다.",
                    errors = errorList
                )
            )
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun handle(e: IllegalArgumentException): ResponseEntity<ErrorResponse> {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(
                ErrorResponse(
                    errorCode = ErrorCode.INVALID_INPUT.name,
                    message = e.message
                )
            )
    }
}
