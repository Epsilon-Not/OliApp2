package us.synergize_apps.oliapp.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import us.synergize_apps.oliapp.R
import us.synergize_apps.oliapp.utils.Constants

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val  sharedPreferences = getSharedPreferences(Constants.OLIAPP_PREFERENCES,
            Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!
        main.text = "$userName is now logged in."
    }
}