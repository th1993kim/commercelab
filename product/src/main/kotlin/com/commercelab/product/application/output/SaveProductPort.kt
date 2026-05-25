package com.commercelab.product.application.output

import com.commercelab.product.domain.model.Product

interface SaveProductPort {

    fun save(product : Product)
}