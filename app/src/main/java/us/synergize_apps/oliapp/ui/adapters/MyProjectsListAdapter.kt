package us.synergize_apps.oliapp.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_list_layout.view.*
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.models.Project
import us.synergize_apps.oliapp.ui.fragments.ProjectsFragment
import us.synergize_apps.oliapp.utils.GlideLoader

open class MyProjectsListAdapter (
    private val context: Context,
    private val list: ArrayList<Project>,
    private val fragment: ProjectsFragment
): RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return MyViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent,
                false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model = list[position]

        if (holder is MyViewHolder){
            GlideLoader(context).loadProjectPicture(model.image, holder.itemView.iv_item_image)
            holder.itemView.tv_project_name.text = model.title
            holder.itemView.tv_project_language.text = model.languages

            holder.itemView.ib_delete_product.setOnClickListener {
                fragment.deleteProject(model.id)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}