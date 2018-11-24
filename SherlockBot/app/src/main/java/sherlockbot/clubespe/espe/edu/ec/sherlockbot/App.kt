package sherlockbot.clubespe.espe.edu.ec.sherlockbot

import android.app.Activity
import android.app.Application
import dagger.android.AndroidInjector
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger.AppInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import javax.inject.Inject

class App : Application(), HasActivityInjector {

    @Inject lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    override fun onCreate() {
        super.onCreate()
        AppInjector.init(this)
    }

    override fun activityInjector(): AndroidInjector<Activity> {
        return activityInjector
    }
}