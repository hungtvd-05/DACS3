package com.app_computer_ecom.dack.model

data class Ward private constructor(
    val Id: String,
    val Name: String,
    val Level: String
) {
    companion object {
        fun create(Id: String, Name: String, Level: String): Ward {
            return Ward(Id, Name, Level)
        }
    }

    constructor() : this("", "", "")
}
