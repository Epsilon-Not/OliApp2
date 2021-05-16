package us.synergize_apps.oliapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.ui.activities.AddProjectActivity
import kotlinx.android.synthetic.main.fragment_projects.*
import us.synergize_apps.oliapp.models.Project
import us.synergize_apps.oliapp.ui.activities.firestore.FireStoreClass

class ProjectsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun successProjectsListFromFireStore(projectsList: ArrayList<Project>){
        hideProgressDialog()
        for (i in projectsList){
            Log.i("Project Title", i.title)
        }
    }

    private fun getProjectListGromFireStore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProjectsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProjectListGromFireStore()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_projects, container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_project_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_add_project) {
            startActivity(Intent(activity, AddProjectActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }


}