package sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger

import sherlockbot.clubespe.espe.edu.ec.sherlockbot.App

object AppInjector {
    fun init(app: App) {
        DaggerAppComponent.builder()
            .appModule(AppModule())
            .build()
            .inject(app)
    }
}