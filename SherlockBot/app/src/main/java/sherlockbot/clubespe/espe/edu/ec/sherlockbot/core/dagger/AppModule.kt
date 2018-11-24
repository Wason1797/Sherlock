package sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger.scope.AppScope
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.core.dagger.viewmodel.ViewModelModule
import dagger.Module
import dagger.Provides

@Module(includes = [ViewModelModule::class])
class AppModule {

    @AppScope
    @Provides
    fun provideDatabaseReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().reference!!
    }
}