package com.milomobile.bathrooms4tp.data.adapter

import com.google.firebase.firestore.DocumentSnapshot

interface FirestoreAdapter<T> {
    fun documentToModel(document: DocumentSnapshot) : T?
    fun modelToDocument(model: T) : Map<String, Any>
}