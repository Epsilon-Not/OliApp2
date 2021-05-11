package us.synergize_apps.oliapp.utils

import android.app.Activity
import android.content.Intent
import android.provider.MediaStore
import us.synergize_apps.oliapp.ui.activities.BaseActivity

object Constants {
    const val USERS: String = "users"
    const val OLIAPP_PREFERENCES: String = "OliAppPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val USER_EXTENDED_INFO: String = "user extended info"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 3

    fun showImageChoose(activity: Activity) {
        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }
}