package us.synergize_apps.oliapp.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Project (
        val user_id: String = "",
        val user_name: String = "",
        val user_email: String = "",
        val title: String = "",
        val languages: String = "",
        val description: String = "",
        val repository: String = "",
        val image: String = "",
        var id: String = "",
): Parcelable