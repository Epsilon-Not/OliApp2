package us.synergize_apps.oliapp.ui.activities.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import us.synergize_apps.oliapp.models.Project
import us.synergize_apps.oliapp.models.User
import us.synergize_apps.oliapp.ui.activities.AddProjectActivity
import us.synergize_apps.oliapp.ui.activities.ProjectDetailsActivity
import us.synergize_apps.oliapp.ui.fragments.activities.LoginActivity
import us.synergize_apps.oliapp.ui.fragments.activities.UserProfileActivity
import us.synergize_apps.oliapp.ui.activities.RegisterActivity
import us.synergize_apps.oliapp.ui.activities.SettingsActivity
import us.synergize_apps.oliapp.ui.fragments.DashboardFragment
import us.synergize_apps.oliapp.ui.fragments.ProjectsFragment
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

    fun getProjectsList(fragment: Fragment){
        oliFireStore.collection(Constants.PROJECTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document->
                Log.e("Projects List", document.documents.toString())
                val projectsList: ArrayList<Project> = ArrayList()
                for (doc in document.documents) {
                    val project = doc.toObject(Project::class.java)
                    project!!.id = doc.id

                    projectsList.add(project)
                }

                when(fragment){
                    is ProjectsFragment ->{
                        fragment.successProjectsListFromFireStore(projectsList)
                    }
                }
            }

    }

    fun getProjectDetails(activity: ProjectDetailsActivity, projectID: String){
        oliFireStore.collection(Constants.PROJECTS)
            .document(projectID)
            .get()
            .addOnSuccessListener { document->
                Log.e(activity.javaClass.simpleName, document.toString())
                val project = document.toObject(Project::class.java)
                if (project != null) {
                    activity.projectDetailsSuccess(project)
                }

            }
            .addOnFailureListener { e->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error getting proj details", e)
            }
    }


    fun deleteProject(fragment: ProjectsFragment, projectID: String){
        oliFireStore.collection(Constants.PROJECTS)
            .document(projectID)
            .delete()
            .addOnSuccessListener {
                fragment.projectDeleteSuccess()
            }
            .addOnFailureListener {e ->
                fragment.hideProgressDialog()
                Log.e(fragment.requireActivity().javaClass.simpleName,
                "Error Deleting Project",
                e)
            }
    }



    fun getDashboardItemsList(fragment: DashboardFragment){
        oliFireStore.collection(Constants.PROJECTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())

                val projectsList: ArrayList<Project> = ArrayList()

                for (doc in document.documents){
                    val project = doc.toObject(Project::class.java)!!
                    project.id = doc.id
                    projectsList.add(project)
                }

                fragment.successDashboardItemsList(projectsList)
            }
            .addOnFailureListener { e->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName, "Error loading projects", e)
            }
    }
}