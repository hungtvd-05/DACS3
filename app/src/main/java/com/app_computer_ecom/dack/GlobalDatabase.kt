package com.app_computer_ecom.dack

import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore

object GlobalDatabase {
    val database: FirebaseFirestore = Firebase.firestore
}