package br.com.tairoroberto.remoteok.home.view

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.tairoroberto.remoteok.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager
                ?.beginTransaction()
                ?.replace(R.id.container, HomeFragment())
                ?.commit()
    }
}
