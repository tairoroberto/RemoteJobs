package com.remotejobs.io.app.home.view

import android.os.Bundle
import android.view.View.GONE
import com.google.android.instantapps.InstantApps
import com.remotejobs.io.app.view.BaseActivity

class FavoritesActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorites)
    }
}
