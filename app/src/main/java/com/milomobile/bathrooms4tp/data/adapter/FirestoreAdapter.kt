package com.milomobile.bathrooms4tp.data.adapter

import arrow.core.Either
import com.google.firebase.firestore.DocumentSnapshot
import com.milomobile.bathrooms4tp.presentation.base.BaseError

interface FirestoreAdapter<T> {
    fun documentToModel(document: DocumentSnapshot) : Either<BaseError.AdapterError, T?>
    fun modelToDocument(model: T) : Either<BaseError.AdapterError, Map<String, Any?>>
}