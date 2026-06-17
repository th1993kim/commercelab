package com.commercelab.product.adapter.input.web.error

import io.swagger.v3.oas.annotations.media.Schema

data class ValidationErrorResponse(
    @field:Schema(description = "에러 코드", example = "INVALID_INPUT")
    val errorCode: String,
    @field:Schema(description = "에러 메시지", example = "입력값이 유효하지 않습니다.")
    val message: String,
    @field:Schema(description = "입력값 오류가 발생한 필드 목록")
    val errors: List<FieldErrorResponse>

) {
    data class FieldErrorResponse(
        @field:Schema(description = "필드명", example = "name")
        val field: String,
        @field:Schema(description = "오류 사유", example = "이름은 빈 문자열로 넣을 수 없습니다.")
        val message: String
    )
}

