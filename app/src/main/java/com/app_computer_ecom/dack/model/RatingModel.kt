package com.app_computer_ecom.dack.model

import com.google.firebase.Timestamp

data class RatingModel private constructor(
    val id: String = "",
    val pid: String = "",
    var pname: String = "",
    var pimageUrl: String = "",
    val selectType: PriceInfo = PriceInfo(),
    val quantity: Int = 0,
    val uid: String = "",
    val uname: String = "",
    val avatar: String = "",
    val oid: String = "",
    val rating: Int = 0,
    val createdAt: Timestamp = Timestamp.now(),
    val commentModel: CommentModel? = null,
) {
    companion object {
        fun create(
            id: String = "",
            pid: String = "",
            pname: String = "",
            pimageUrl: String = "",
            selectType: PriceInfo = PriceInfo(),
            quantity: Int = 0,
            uid: String = "",
            uname: String = "",
            avatar: String = "",
            oid: String = "",
            rating: Int = 0,
            createdAt: Timestamp = Timestamp.now(),
            commentModel: CommentModel? = null,
        ): RatingModel {
            return RatingModel(id, pid, pname, pimageUrl, selectType, quantity, uid, uname, avatar, oid, rating, createdAt, commentModel)
        }
    }

    constructor(): this("", "", "", "", PriceInfo(), 0, "", "", "", "", 0, Timestamp.now(), null)
}
