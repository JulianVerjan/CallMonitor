package com.test.serverapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.test.serverapp.ui.server.ServerFragment
import dagger.hilt.android.AndroidEntryPoint

/**
 * Base activity class that use the support library action bar features.
 *
 * @see AppCompatActivity
 */
@AndroidEntryPoint
class ServerMainActivity : AppCompatActivity() {

    /**
     * Called when the activity is starting. This is where most initialization should go: calling
     * [AppCompatActivity.setContentView] to inflate the activity's UI, using
     * [AppCompatActivity.findViewById] to programmatically interact with widgets in the UI.
     *
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @see AppCompatActivity.onCreate
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, ServerFragment.newInstance())
                .commitNow()
        }
    }
}
