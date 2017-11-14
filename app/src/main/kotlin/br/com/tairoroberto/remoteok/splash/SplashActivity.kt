package br.com.tairoroberto.remoteok.splash

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import br.com.tairoroberto.remoteok.R
import br.com.tairoroberto.remoteok.home.view.MainActivity
import org.jetbrains.anko.startActivity
import java.util.*

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Timer().schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    startActivity<MainActivity>()
                    finish()
                }
            }
        }, 1500)

    }
}
