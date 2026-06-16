package com.commercelab.product.application.output

import com.commercelab.product.domain.model.Product

interface LoadProductPort {

    fun load(id: Long): Product?
}