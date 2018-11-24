package sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.BotViewModel
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.androidarch.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(BotViewModel::class)
    abstract fun bindMainViewModel(mainViewModel: BotViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}