package com.app_computer_ecom.dack

import com.google.firebase.firestore.FirebaseFirestore

object GlobalDatabase {
    var database: FirebaseFirestore = FirebaseFirestore.getInstance()
}