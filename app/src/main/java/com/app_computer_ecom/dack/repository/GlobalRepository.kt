package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.repository.impl.AddressRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.BannerRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.BrandRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.CartRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.CategoryRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.FavoriteRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.OrderRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.ProductRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.RatingAndCommentRepositoryImpl
import com.app_computer_ecom.dack.repository.impl.UserRepositoryImpl

object GlobalRepository {
    var bannerRepository: BannerRepository = BannerRepositoryImpl()
    var brandRepository: BrandRepository = BrandRepositoryImpl()
    var categoryRepository: CategoryRepository = CategoryRepositoryImpl()
    var productRepository: ProductRepository = ProductRepositoryImpl()
    var cartRepository: CartRepository = CartRepositoryImpl()
    var addressRepository: AddressRepository = AddressRepositoryImpl()
    var orderRepository: OrderRepository = OrderRepositoryImpl()
    var favoriteRepository: FavoriteRepository = FavoriteRepositoryImpl()
    var userRepository: UserRepository = UserRepositoryImpl()
    var ratingAndCommentRepository: RatingAndCommentRepository = RatingAndCommentRepositoryImpl()
}