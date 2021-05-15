package us.synergize_apps.oliapp.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_add_project.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.ui.activities.firestore.FireStoreClass
import us.synergize_apps.oliapp.utils.Constants
import us.synergize_apps.oliapp.utils.GlideLoader
import java.io.IOException


class AddProjectActivity : BaseActivity(), View.OnClickListener{


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_project)

        setupActionBar()

        iv_add_update_project.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.iv_add_update_project ->{
                    if (ContextCompat.checkSelfPermission(
                                    this,
                                    Manifest.permission.READ_EXTERNAL_STORAGE
                    )== PackageManager.PERMISSION_GRANTED){
                        Constants.showImageChoose(this@AddProjectActivity)
                    }else{ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            Constants.READ_STORAGE_PERMISSION_CODE)
                    }
                }
            }
        }
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
                    iv_add_update_project.setImageDrawable(ContextCompat.getDrawable(
                            this, R.drawable.ic_baseline_edit_24))

                    val selectedImageFileURI = data.data!!

                    try {
                        GlideLoader(this).loadUserPicture(selectedImageFileURI, iv_project_image)
                    }catch (e: IOException){
                        e.printStackTrace()
                    }
                }
            }
        }else if (resultCode == Activity.RESULT_CANCELED) {
            Log.e("Request Cancelled", "Image select cancelled.")
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_add_project_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24dp)
        }

        toolbar_add_project_activity.setNavigationOnClickListener { onBackPressed() }
    }


}