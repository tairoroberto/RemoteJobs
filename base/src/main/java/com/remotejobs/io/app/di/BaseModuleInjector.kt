package com.remotejobs.io.app.di

import android.app.Activity
import android.app.Service
import android.content.BroadcastReceiver
import android.content.ContentProvider
import android.content.Context
import com.remotejobs.io.app.CustomApplication
import dagger.android.*
import dagger.android.support.HasSupportFragmentInjector
import javax.inject.Inject
import androidx.fragment.app.Fragment as SupportFragment

/**
 * Cf. [dagger.android.DaggerApplication]
 */
abstract class BaseModuleInjector : HasActivityInjector,
        HasSupportFragmentInjector,
        HasServiceInjector,
        HasBroadcastReceiverInjector,
        HasContentProviderInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var broadcastReceiverInjector: DispatchingAndroidInjector<BroadcastReceiver>

    @Inject
    lateinit var fragmentSupportInject: DispatchingAndroidInjector<SupportFragment>

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var contentProviderInjector: DispatchingAndroidInjector<ContentProvider>

    private var needToInject = true

    abstract fun moduleInjector(daggerComponent: DaggerComponent, context: Context?): AndroidInjector<out BaseModuleInjector>

    /**
     * Inject an app component
     */
    fun inject(dependerContext: Any?) {
        var context: Context? = null

        when (dependerContext) {
            is Activity -> context = dependerContext.applicationContext
            is SupportFragment -> context = dependerContext.context
            is Service -> context = dependerContext.applicationContext
            is ContentProvider -> context = dependerContext.context
        }

        injectIfNecessary(CustomApplication.appComponent(context), context)
        injectByTypeClass(dependerContext)
    }

    /**
     * Inject a sub component
     */
    fun inject(daggerComponent: DaggerComponent, dependerContext: Context) {
        injectIfNecessary(daggerComponent, dependerContext)
        injectByTypeClass(dependerContext)
    }

    private fun injectByTypeClass(dependerContext: Any?) {
        when (dependerContext) {
            is Activity -> activityInjector.inject(dependerContext)
            is SupportFragment -> fragmentSupportInject.inject(dependerContext)
            is Service -> serviceInjector.inject(dependerContext)
            is BroadcastReceiver -> broadcastReceiverInjector.inject(dependerContext)
            is ContentProvider -> contentProviderInjector.inject(dependerContext)
        }
    }

    /**
     * Initialize component again
     */
    fun forceInject(dependerContext: Context) {
        needToInject = true
        inject(dependerContext)
    }

    /**
     * Initialize component again
     */
    fun forceInject(daggerComponent: DaggerComponent, dependerContext: Context) {
        needToInject = true
        inject(daggerComponent, dependerContext)
    }

    private fun injectIfNecessary(daggerComponent: DaggerComponent, context: Context?) {
        if (needToInject) {
            synchronized(this) {
                if (needToInject) {
                    val moduleInjector = moduleInjector(daggerComponent, context) as AndroidInjector<BaseModuleInjector>
                    moduleInjector.inject(this)
                    if (needToInject) {
                        throw IllegalStateException(
                                "The AndroidInjector returned from applicationInjector() did not inject the " + "DaggerApplication")
                    }
                }
            }
        }
    }

    @Inject
    internal fun setInjected() {
        needToInject = false
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = activityInjector

    override fun broadcastReceiverInjector(): DispatchingAndroidInjector<BroadcastReceiver> = broadcastReceiverInjector

    override fun serviceInjector(): DispatchingAndroidInjector<Service> = serviceInjector

    override fun contentProviderInjector(): DispatchingAndroidInjector<ContentProvider> = contentProviderInjector

    override fun supportFragmentInjector(): DispatchingAndroidInjector<SupportFragment> = fragmentSupportInject
}