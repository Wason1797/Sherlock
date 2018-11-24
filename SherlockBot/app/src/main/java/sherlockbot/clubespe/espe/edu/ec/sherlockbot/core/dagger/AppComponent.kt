package sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger

import sherlockbot.clubespe.espe.edu.ec.sherlockbot.App
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger.scope.AppScope
import dagger.Component
import dagger.android.AndroidInjectionModule


@AppScope
@Component(modules = [AndroidInjectionModule::class, AppModule::class, AndroidBindingModule::class])
interface AppComponent {
    fun inject(app: App)
}