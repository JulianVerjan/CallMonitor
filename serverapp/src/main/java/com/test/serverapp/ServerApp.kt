package com.test.serverapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Base class for maintaining global application state.
 */
@HiltAndroidApp
class ServerApp : Application()
