package us.synergize_apps.oliapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_projects.*
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.ui.activities.AddProjectActivity
import us.synergize_apps.oliapp.models.Project
import us.synergize_apps.oliapp.ui.activities.firestore.FireStoreClass
import us.synergize_apps.oliapp.ui.adapters.MyProjectsListAdapter

class ProjectsFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    fun successProjectsListFromFireStore(projectsList: ArrayList<Project>){
        hideProgressDialog()

        if (projectsList.size > 0){
            rv_my_project_items.visibility = View.VISIBLE
            no_projects_found.visibility = View.GONE

            rv_my_project_items.layoutManager = LinearLayoutManager(activity)
            rv_my_project_items.setHasFixedSize(true)
            val adapterProjects = MyProjectsListAdapter(requireActivity(), projectsList)
            rv_my_project_items.adapter = adapterProjects
        }else{
            rv_my_project_items.visibility = View.GONE
            no_projects_found.visibility = View.VISIBLE
        }
    }

    private fun getProjectListFromFireStore(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getProjectsList(this)
    }

    override fun onResume() {
        super.onResume()
        getProjectListFromFireStore()
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