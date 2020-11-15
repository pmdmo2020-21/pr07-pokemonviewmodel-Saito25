package es.iessaladillo.pedrojoya.intents.ui.selection

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon

class SelectionActivityViewModel : ViewModel() {

    private val _selectPokemon: MutableLiveData<Pokemon> =
        MutableLiveData(Database.getRandomPokemon())
    val selectPokemon: LiveData<Pokemon>
        get() = _selectPokemon

    fun changeSelectedPokemon(pokemon: Pokemon) {
        _selectPokemon.value = pokemon
    }
}