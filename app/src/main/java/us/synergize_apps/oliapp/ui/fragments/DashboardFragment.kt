package us.synergize_apps.oliapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import kotlinx.android.synthetic.main.fragment_dashboard.*
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.models.Project
import us.synergize_apps.oliapp.ui.activities.SettingsActivity
import us.synergize_apps.oliapp.ui.activities.firestore.FireStoreClass
import us.synergize_apps.oliapp.ui.adapters.DashboardProjectsListAdapter


class DashboardFragment : BaseFragment() {

    //private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onResume() {
        super.onResume()
        getDashboardItemsList()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)
        return root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        when (id) {

            R.id.action_settings -> {

                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun successDashboardItemsList(dashboardItemsList: ArrayList<Project>){
        hideProgressDialog()
        if (dashboardItemsList.size > 0){

            rv_dashboard_items.visibility = View.VISIBLE
            no_dashboard_projects_found.visibility = View.GONE

            rv_dashboard_items.layoutManager = GridLayoutManager(activity, 2)
            rv_dashboard_items.setHasFixedSize(true)

            val adapter = DashboardProjectsListAdapter(requireActivity(), dashboardItemsList)
            rv_dashboard_items.adapter = adapter

        }else {
            rv_dashboard_items.visibility = View.GONE
            no_dashboard_projects_found.visibility = View.VISIBLE
        }
    }

    fun getDashboardItemsList(){
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStoreClass().getDashboardItemsList(this@DashboardFragment)
    }
}