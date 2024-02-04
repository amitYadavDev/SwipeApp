package amitapps.media.swipeapp.models

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

data class ImagePart(
    val name: String,
    val file: File,
    val mediaType: MediaType = MediaType.parse("image/*")!!
) {
    fun toMultipartBodyPart(): MultipartBody.Part {
        val requestFile = RequestBody.create(mediaType, file)
        return MultipartBody.Part.createFormData(name, file.name, requestFile)
    }
}
