package us.synergize_apps.oliapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.ui.activities.AddProjectActivity
import us.synergize_apps.oliapp.ui.activities.firestore.FireStoreClass
import kotlinx.android.synthetic.main.fragment_projects.*

class ProjectsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_projects, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        textView.text = "This is Products Fragment"
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