package com.app_computer_ecom.dack.repository

import com.app_computer_ecom.dack.model.AddressModel

interface AddressRepository {
    suspend fun addAddress(address: AddressModel)
    suspend fun updateAddress(address: AddressModel)
    suspend fun deleteAddress(address: AddressModel)
    suspend fun getAddresses(): List<AddressModel>
    suspend fun getAddressById(id: String): AddressModel?
    suspend fun getDefaultAddress(): AddressModel?
}