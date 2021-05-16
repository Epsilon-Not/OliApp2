package us.synergize_apps.oliapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_project_details.*
import kotlinx.android.synthetic.main.activity_settings.*
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.models.Project
import us.synergize_apps.oliapp.ui.activities.firestore.FireStoreClass
import us.synergize_apps.oliapp.utils.Constants
import us.synergize_apps.oliapp.utils.GlideLoader

class ProjectDetailsActivity : BaseActivity() {

    private var oliProjectID: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_project_details)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PROJECT_ID)){
            oliProjectID = intent.getStringExtra(Constants.EXTRA_PROJECT_ID)!!
            Log.i("ProjectID", oliProjectID)
        }

        getProjectDetails()
    }

    private fun getProjectDetails(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProjectDetails(this, oliProjectID)
    }

    fun projectDetailsSuccess(project: Project){
        hideProgressDialog()
        GlideLoader(this@ProjectDetailsActivity,).loadProjectPicture(
            project.image, iv_project_detail_image
        )
        tv_project_details_owner.text = project.user_name
        tv_project_details_email.text = project.user_email
        tv_project_details_description.text = project.description
        tv_project_details_languages.text = project.languages


    }

    private fun setupActionBar() {

        setSupportActionBar(toolbar_project_details_activity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
        }

        toolbar_project_details_activity.setNavigationOnClickListener { onBackPressed() }
    }

}