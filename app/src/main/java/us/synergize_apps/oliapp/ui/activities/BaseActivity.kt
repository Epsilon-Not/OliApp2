package us.synergize_apps.oliapp.ui.activities

import android.app.Dialog
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.os.Handler
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.dialog_progress.*
import us.synergize_apps.oliapp.R



/**
 * A base activity class is used to define the functions and members which we will use in all the activities.
 * It inherits the AppCompatActivity class so in other activity class we will replace the AppCompatActivity with BaseActivity.
 */
// START
open class BaseActivity : AppCompatActivity() {

    private var backPressedOnce = false
    private lateinit var oliProgressDialog: Dialog
    /**
     * A function to show the success and error messages in snack bar component.
     */
    fun showErrorSnackBar(message: String, errorMessage: Boolean) {
        val snackBar =
            Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage) {
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this@BaseActivity,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

    fun showProgressDialog(text: String) {
        oliProgressDialog = Dialog(this)

        // Set screen content from layout resource
        oliProgressDialog.setContentView(R.layout.dialog_progress)

        oliProgressDialog.tv_progress_text.text = text

        oliProgressDialog.setCancelable(false)
        oliProgressDialog.setCanceledOnTouchOutside(false)

        oliProgressDialog.show()
    }

    fun hideProgressDialog() {
        oliProgressDialog.dismiss()
    }

    fun doubleBackToExit() {

        if (backPressedOnce) {
            super.onBackPressed()
            return
        }

        this.backPressedOnce = true

        Toast.makeText(
            this@BaseActivity,
            resources.getString(R.string.back_again_to_exit), Toast.LENGTH_SHORT
        ).show()

        @Suppress("DEPRECATION")
        Handler().postDelayed({ backPressedOnce = false }, 2000)
    }
}
