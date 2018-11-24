package sherlockbot.clubespe.espe.edu.ec.sherlockbot

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

import dagger.android.AndroidInjection
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.controller.BoardController
import javax.inject.Inject

class BotActivity : AppCompatActivity(){


    @Inject lateinit var boardControl: BoardController
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var viewModel: BotViewModel
    private lateinit var nMov: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bot)
        nMov=findViewById(R.id.txtMovement)

        lifecycle.addObserver(boardControl)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(BotViewModel::class.java)

        viewModel.sherlockLiveData.observe(this, Observer {sherlock ->
            nMov.text = sherlock?.movement_command
            boardControl.setDirectionMotor(sherlock?.movement_command?:"still")
        })
        viewModel.sherlockLiveData.saveGPS("1;1")
    }
}
