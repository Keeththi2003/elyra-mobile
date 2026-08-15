package com.keeththigan.elyra.core.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseProvider {

    val auth: FirebaseAuth
        get() = FirebaseAuth.getInstance()

    val firestore: FirebaseFirestore
        get() = FirebaseFirestore.getInstance()
}