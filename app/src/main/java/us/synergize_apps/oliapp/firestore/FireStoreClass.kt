package us.synergize_apps.oliapp.ui.activities.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import us.synergize_apps.oliapp.models.Project
import us.synergize_apps.oliapp.models.User
import us.synergize_apps.oliapp.ui.activities.AddProjectActivity
import us.synergize_apps.oliapp.ui.fragments.activities.LoginActivity
import us.synergize_apps.oliapp.ui.fragments.activities.UserProfileActivity
import us.synergize_apps.oliapp.ui.activities.RegisterActivity
import us.synergize_apps.oliapp.ui.activities.SettingsActivity
import us.synergize_apps.oliapp.utils.Constants

class FireStoreClass {


    private val oliFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        oliFireStore.collection(Constants.USERS)
            .document(userInfo.id)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {

                activity.userRegistrationSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Registration Error",
                    e
                )
            }

    }

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity) {

        oliFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                val user = document.toObject(User::class.java)!!


                val sharedPreferences =
                    activity.getSharedPreferences(
                        Constants.OLIAPP_PREFERENCES,
                        Context.MODE_PRIVATE
                    )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when (activity){
                    is LoginActivity -> {
                        activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailsSuccess(user)

                    }
                }
            }
            .addOnFailureListener { e ->
                // Hide the progress dialog if there is any error. And print the error in log.
                when (activity) {
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }

                }

                Log.e(
                    activity.javaClass.simpleName,
                    "Error while getting user details.",
                    e
                )
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>) {

        oliFireStore.collection(Constants.USERS).document(getCurrentUserID()).update(userHashMap)
                .addOnSuccessListener {e->
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.userProfileUpdateSuccess()
                        }
                    }
                }
                .addOnFailureListener {e->
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.hideProgressDialog()
                        }
                    }
                    Log.e(activity.javaClass.simpleName, "Error updating info.", e)
                }
    }

    fun uploadImageToCloud(activity: Activity, imageFileUri: Uri, imageType: String) {
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "."
                    + Constants.getFileExtension(activity, imageFileUri))
        sRef.putFile(imageFileUri!!).addOnSuccessListener { taskSnapshot->
            Log.e(
                "Firebase Image URL",
                taskSnapshot.metadata!!.reference!!.downloadUrl.toString()
            )
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when (activity) {
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddProjectActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }



                    }
                }
        }
            .addOnFailureListener { exception ->
                when (activity) {
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProjectActivity ->{
                        activity.hideProgressDialog()
                    }

                }
                Log.e(activity.javaClass.simpleName, exception.message, exception)
            }
    }

    fun uploadProjectDetails(activity: AddProjectActivity, projectInfo: Project){
        oliFireStore.collection(Constants.PROJECTS)
                .document()
                .set(projectInfo, SetOptions.merge())
                .addOnSuccessListener {
                    activity.projectUploadSuccess()
                }
                .addOnFailureListener { e->
                    activity.hideProgressDialog()
                    Log.e(activity.javaClass.simpleName, "Error Uploading Project", e)
                }
    }

}