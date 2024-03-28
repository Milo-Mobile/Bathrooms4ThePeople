package com.milomobile.bathrooms4tp.data.repository

import arrow.core.Either
import com.google.firebase.firestore.FirebaseFirestore
import com.milomobile.bathrooms4tp.data.model.bathroom_models.BathroomAdapter
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathrooms
import com.milomobile.bathrooms4tp.presentation.base.QueryError
import kotlinx.coroutines.tasks.await

interface BathroomRepository {
    suspend fun getBathrooms() : Either<QueryError, Bathrooms>
    suspend fun submitRating()
}

class BathroomRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : BathroomRepository {

    companion object {
        const val BATHROOM_COLLECTION = "bathrooms"
    }

    override suspend fun getBathrooms() : Either<QueryError, Bathrooms> =
        Either.catch {
            val result = firestore.collection(BATHROOM_COLLECTION).get().await()
            result.documents.mapNotNull {
                BathroomAdapter().documentToModel(it)
            }
        }.mapLeft {
            QueryError(it.message ?: "Failed to load story, please check internet and try again.")
        }


    override suspend fun submitRating() {
        TODO("Not yet implemented")
    }

}