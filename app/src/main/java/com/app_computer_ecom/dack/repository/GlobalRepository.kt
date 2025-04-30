package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.repository.impl.BannerRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.BrandRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.CartRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.CategoryRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.ProductRepositoryImpl

object GlobalRepository {
    var bannerRepository: BannerRepository = BannerRepositoryImpl()
    var brandRepository: BrandRepository = BrandRepositoryImpl()
    var categoryRepository: CategoryRepository = CategoryRepositoryImpl()
    var productRepository: ProductRepository = ProductRepositoryImpl()
    var cartRepository: CartRepository = CartRepositoryImpl()
}