package us.synergize_apps.oliapp.ui.fragments

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.dialog_progress.*
import us.synergize_apps.oliapp.R

open class BaseFragment : Fragment() {

    private lateinit var oliProgressDialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_base, container, false)
    }

    fun showProgressDialog(text: String) {
        oliProgressDialog = Dialog(requireActivity())
        oliProgressDialog.setContentView(R.layout.dialog_progress)
        oliProgressDialog.tv_progress_text.text = text
        oliProgressDialog.setCancelable(false)
        oliProgressDialog.setCanceledOnTouchOutside(false)
        oliProgressDialog.show()
    }

    fun hideProgressDialog(){
        oliProgressDialog.dismiss()
    }
}