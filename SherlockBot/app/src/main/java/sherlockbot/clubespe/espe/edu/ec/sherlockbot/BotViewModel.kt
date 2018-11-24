package sherlockbot.clubespe.espe.edu.ec.sherlockbot

import android.arch.lifecycle.ViewModel
import sherlockbot.clubespe.espe.edu.ec.sherlockbot.model.SherlockLiveData
import javax.inject.Inject

class BotViewModel @Inject constructor(val sherlockLiveData: SherlockLiveData): ViewModel()