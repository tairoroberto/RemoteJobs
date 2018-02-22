package com.remoteok

import android.app.Application
import android.content.Context
import android.support.test.runner.AndroidJUnitRunner

class RemoteOkTestRunner : AndroidJUnitRunner() {

    /**
     * Estamos configurando nosso apk de teste para ter como Application o nosso ApplicationTest.class
     * que criamos no ambiente de teste.
     */
    override fun newApplication(cl: ClassLoader?, className: String?, context: Context?): Application {
        return super.newApplication(cl, ApplicationTest::class.java.name, context)
    }
}