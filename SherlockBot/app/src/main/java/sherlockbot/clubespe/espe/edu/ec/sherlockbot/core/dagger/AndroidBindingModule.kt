package sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger

import sherlockbot.clubespe.espe.edu.ec.sherlockbot.BotActivity
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger.scope.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AndroidBindingModule {

    @ActivityScope
    @ContributesAndroidInjector
    abstract fun contributeMainActivity(): BotActivity
}