package us.synergize_apps.oliapp.ui.fragments.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_user_profile.*
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.models.User
import us.synergize_apps.oliapp.ui.activities.BaseActivity
import us.synergize_apps.oliapp.ui.activities.MainActivity
import us.synergize_apps.oliapp.ui.activities.firestore.FireStoreClass
import us.synergize_apps.oliapp.utils.Constants
import us.synergize_apps.oliapp.utils.GlideLoader
import java.io.IOException


class UserProfileActivity : BaseActivity(), View.OnClickListener {

    private lateinit var oliUserDetails: User
    private var oliSelectedImageFileUri: Uri? = null
    private var oliProfileImageURL: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        if (intent.hasExtra(Constants.USER_EXTENDED_INFO)) {
            oliUserDetails = intent.getParcelableExtra(Constants.USER_EXTENDED_INFO)!!
        }

        til_first_name.isEnabled = false
        til_first_name.setText(oliUserDetails.firstName)

        til_last_name.isEnabled = false
        til_last_name.setText(oliUserDetails.lastName)

        til_email.isEnabled = false
        til_email.setText(oliUserDetails.email)

        iv_user_photo.setOnClickListener(this@UserProfileActivity)
        btn_submit.setOnClickListener(this@UserProfileActivity)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            when (view.id) {
                R.id.iv_user_photo -> {
                    if (ContextCompat.checkSelfPermission(
                            this,
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        )
                        == PackageManager.PERMISSION_GRANTED
                    ) {
                        Constants.showImageChoose(this)
                    } else {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE
                        )
                    }
                }

                R.id.btn_submit ->{
                    if (validateUserProfileDetails()){
                        showProgressDialog(resources.getString(R.string.please_wait))

                        if (oliSelectedImageFileUri != null) {
                            FireStoreClass().uploadImageToCloud(
                                this@UserProfileActivity,
                                oliSelectedImageFileUri!!
                            )
                        }else{
                            updateUserProfileDetails()
                        }



                    }
                }
            }
        }
    }

    private fun  updateUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()
        val mobileNumber = til_mobile_number.text.toString().trim { it <= ' ' }

        if (oliProfileImageURL.isNotEmpty()){
            userHashMap[Constants.USER_PROFILE_IMAGE] = oliProfileImageURL
        }

        if (mobileNumber.isNotEmpty()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }

        userHashMap[Constants.PROFILE_COMPLETE] = 1
        FireStoreClass().updateUserProfileData(this, userHashMap)
    }

    fun userProfileUpdateSuccess() {
        hideProgressDialog()
        Toast.makeText(
                this,
                resources.getString(R.string.profile_update_success),
                Toast.LENGTH_SHORT
        ).show()

        startActivity(Intent(this@UserProfileActivity, MainActivity::class.java))
        finish()


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            //If permission is granted
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChoose(this)
            } else {
                //Displaying another toast if permission is not granted
                Toast.makeText(
                    this,
                    resources.getString(R.string.read_storage_permission_denied),
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        oliSelectedImageFileUri = data.data!!

                        //iv_user_photo.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(oliSelectedImageFileUri!!,
                            iv_user_photo)
                    }catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            resources.getString(R.string.image_select_fail),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image select cancelled.")
        }
    }
    private fun validateUserProfileDetails(): Boolean {
        return when {

            // We have kept the user profile picture is optional.
            // The FirstName, LastName, and Email Id are not editable when they come from the login screen.
            // The Radio button for Gender always has the default selected value.

            // Check if the mobile number is not empty as it is mandatory to enter.
            TextUtils.isEmpty(til_mobile_number.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_mobile_number), true)
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL: String) {
        oliProfileImageURL = imageURL
        updateUserProfileDetails()
    }

}