package com.commercelab.product.adapter.input.web

import com.commercelab.product.adapter.input.web.dto.CreateProductRequest
import com.commercelab.product.adapter.input.web.dto.GetProductResponse
import com.commercelab.product.adapter.input.web.dto.UpdateProductRequest
import com.commercelab.product.adapter.input.web.error.ErrorResponse
import com.commercelab.product.application.input.ProductCommandUseCase
import com.commercelab.product.application.input.ProductQueryUseCase
import com.commercelab.product.application.input.UpdateProductCommand
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "Product", description = "상품 API")
@RequestMapping(value = ["/api/products"])
@RestController
class ProductController(
    private val productCommandUseCase: ProductCommandUseCase,
    private val productQueryUseCase: ProductQueryUseCase
) {

    @Operation(summary = "상품 생성", description = "판매 가능한 상품 정보를 생성합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "상품 생성 성공"),
            ApiResponse(
                responseCode = "400",
                description = "잘못된 상품 생성 요청",
                content = [
                    Content(schema = Schema(implementation = ErrorResponse::class))
                ]
            )
        ]
    )
    @PostMapping
    fun createProduct(@Valid @RequestBody request: CreateProductRequest) {
        productCommandUseCase.createProduct(request.toCreateProductCommand())
    }

    @Operation(summary = "상품 조회", description = "상품 식별자로 상품 정보를 조회합니다.")
    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                description = "상품 조회 성공",
                content = [
                    Content(schema = Schema(implementation = GetProductResponse::class))
                ]
            ),
            ApiResponse(
                responseCode = "404",
                description = "상품을 찾을 수 없음",
                content = [
                    Content(schema = Schema(implementation = ErrorResponse::class))
                ]
            )
        ]
    )
    @GetMapping("/{productId}")
    fun getProduct(
        @Parameter(description = "상품 식별번호", example = "1")
        @PathVariable productId: Long
    ) : GetProductResponse? {
        val productResult = productQueryUseCase.getProduct(productId);
        return GetProductResponse(
            id = productResult.id,
            name = productResult.name,
            description = productResult.description,
            price = productResult.price
        )
    }


    @Operation(summary = "상품 수정", description = "상품 식별자 및 요청정보로 상품 정보를 수정합니다.")
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "상품 수정 성공"),
            ApiResponse(
                responseCode = "404",
                description = "상품을 찾을 수 없음",
                content = [
                    Content(schema = Schema(implementation = ErrorResponse::class))
                ]
            )
        ]
    )
    @PatchMapping("/{productId}")
    fun updateProduct(@PathVariable productId: Long, @Valid @RequestBody request: UpdateProductRequest) {

        productCommandUseCase.updateProduct(request.toUpdateProductCommand(productId))
    }

}
