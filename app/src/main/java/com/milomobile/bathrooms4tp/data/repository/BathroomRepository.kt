package com.milomobile.bathrooms4tp.data.repository

import arrow.core.Either
import arrow.core.getOrElse
import com.google.firebase.firestore.FirebaseFirestore
import com.milomobile.bathrooms4tp.data.model.bathroom_models.BathroomAdapter
import com.milomobile.bathrooms4tp.data.model.bathroom_models.Bathrooms
import com.milomobile.bathrooms4tp.presentation.base.BaseError
import kotlinx.coroutines.tasks.await

interface BathroomRepository {
    suspend fun getBathrooms() : Either<BaseError, Bathrooms>

    suspend fun submitBathroom(documentMap: Map<String, Any?>) : Either<BaseError, Unit>
    suspend fun submitRating()
}

class BathroomRepositoryImpl(
    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
) : BathroomRepository {

    companion object {
        const val BATHROOM_COLLECTION = "bathrooms"
        const val SUBMITTED_BATHROOM_COLLECTION = "submitted_bathrooms"

        const val GOOGLE_MAPS_PACKAGE = "com.google.android.apps.maps"
        const val GOOGLE_MAPS_QUERY = "geo:0,0?q="
    }

    //TODO: Revisit Arrow usage here
    override suspend fun getBathrooms() : Either<BaseError, Bathrooms> =
        Either.catch {
            val result = firestore.collection(BATHROOM_COLLECTION).get().await()
            result.documents.mapNotNull { documentSnapshot ->
                BathroomAdapter().documentToModel(documentSnapshot).getOrElse { adapterError ->
                    when (adapterError) {
                        is BaseError.AdapterError.NecessaryDataMissing -> {
                            //TODO: Report the error message to analytics
                            null
                        }
                        is BaseError.AdapterError.UnexpectedTypeCast -> {
                            //TODO: Report the error message to analytics
                            null
                        }
                        is BaseError.AdapterError.UnknownExceptionCaught -> {
                            //TODO: Report the error message to analytics
                            throw adapterError
                        }
                    }
                }
            }
        }.mapLeft {
            BaseError.QueryError(
                it.message ?: "Failed to load bathroom, please check internet and try again."
            )
        }

    override suspend fun submitBathroom(documentMap: Map<String, Any?>): Either<BaseError, Unit> =
        Either.catch {
            firestore.collection(SUBMITTED_BATHROOM_COLLECTION).document().set(documentMap).await()
            Unit
        }.mapLeft {
            BaseError.QueryError(
                it.message ?: "Failed to submit bathroom, please check internet and try again."
            )
        }



    override suspend fun submitRating() {
        TODO("Not yet implemented")
    }

}