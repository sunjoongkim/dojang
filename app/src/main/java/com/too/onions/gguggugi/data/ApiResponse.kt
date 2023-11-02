package com.too.onions.gguggugi.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonElement
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import kotlinx.parcelize.Parceler

@Parcelize
data class ApiResponse(
    @SerializedName("data") val data: JsonElement,
    @SerializedName("message") val message: String? = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        data = Gson().fromJson(parcel.readString(), JsonElement::class.java),
        message = parcel.readString()
    )

    companion object : Parceler<ApiResponse> {
        override fun ApiResponse.write(parcel: Parcel, flags: Int) {
            parcel.writeString(Gson().toJson(data))
            parcel.writeString(message)
        }

        override fun create(parcel: Parcel): ApiResponse {
            return ApiResponse(parcel)
        }
    }
}
