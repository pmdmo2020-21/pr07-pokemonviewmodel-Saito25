package es.iessaladillo.pedrojoya.intents.ui.winner

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon

class WinnerActivityViewModel : ViewModel() {

    private val _winnerPokemon: MutableLiveData<Pokemon> =
        MutableLiveData(Database.getRandomPokemon())
    val winnerPokemon: LiveData<Pokemon>
        get() = _winnerPokemon

    fun changeWinnerPokemon(pokemon: Pokemon) {
        _winnerPokemon.value = pokemon
    }
}