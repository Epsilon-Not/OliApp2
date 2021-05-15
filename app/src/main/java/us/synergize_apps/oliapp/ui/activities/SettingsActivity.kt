package us.synergize_apps.oliapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_settings.*
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.models.User
import us.synergize_apps.oliapp.ui.activities.firestore.FireStoreClass
import us.synergize_apps.oliapp.ui.fragments.activities.LoginActivity
import us.synergize_apps.oliapp.ui.fragments.activities.UserProfileActivity
import us.synergize_apps.oliapp.utils.Constants
import us.synergize_apps.oliapp.utils.GlideLoader

class SettingsActivity : BaseActivity(), View.OnClickListener {

    private lateinit var oliUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        tv_edit.setOnClickListener(this)
        btn_logout.setOnClickListener(this)
        setupActionBar()

        tv_edit.setOnClickListener(this@SettingsActivity)
        btn_logout.setOnClickListener(this@SettingsActivity)
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if (v != null) {
            when(v.id){

                R.id.tv_edit ->{
                    val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
                    intent.putExtra(Constants.USER_EXTENDED_INFO, oliUserDetails)
                    startActivity(intent)
                }

                R.id.btn_logout ->{
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }

            }
        }
    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_settings_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_settings_activity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getUserDetails(this)
    }

    fun userDetailsSuccess(user: User) {
        oliUserDetails = user
        hideProgressDialog()
        GlideLoader(this@SettingsActivity).loadUserPicture(user.image, iv_user_photo)
        tv_name.text = "${user.firstName} ${user.lastName}"
        tv_email.text = user.email
        tv_mobile_number.text = "${user.mobile}"
    }
}