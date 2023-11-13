package com.gmail.remember.data.datastore.serializers

import androidx.datastore.core.Serializer
import com.gmail.remember.models.ProfileModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject

class ProfilePreferencesSerializer @Inject constructor() :

    Serializer<ProfileModel> {

    override val defaultValue = ProfileModel()

    override suspend fun readFrom(input: InputStream): ProfileModel =
        try {
            Json.decodeFromString(
                ProfileModel.serializer(), input.readBytes().decodeToString()
            )
        } catch (serialization: SerializationException) {
            ProfileModel()
        }

    override suspend fun writeTo(t: ProfileModel, output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(ProfileModel.serializer(), t)
                    .encodeToByteArray()
            )
        }
    }
}