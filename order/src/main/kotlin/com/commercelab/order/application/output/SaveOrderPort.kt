package com.commercelab.order.application.output

import com.commercelab.order.domain.model.Order

interface SaveOrderPort {

    fun save(order: Order)
}