package com.myapps.wtsacademy2.Interfaces

import com.myapps.wtsacademy2.ModelClasses.ProductsMD

interface ProductActionListener {
    fun onEditProduct(product: ProductsMD.Data)
    fun onDeleteProduct(product: ProductsMD.Data)
}
