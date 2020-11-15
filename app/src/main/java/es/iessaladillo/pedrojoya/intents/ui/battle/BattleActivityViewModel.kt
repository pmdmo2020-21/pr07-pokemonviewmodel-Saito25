package es.iessaladillo.pedrojoya.intents.ui.battle

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import java.lang.NullPointerException

const val STATE_FIRST_POKEMON = "STATE_FIRST_POKEMON"
const val STATE_SECOND_POKEMON = "STATE_SECOND_POKEMON"

class BattleActivityViewModel(savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _firstPokemon: MutableLiveData<Pokemon> =
        savedStateHandle.getLiveData(STATE_FIRST_POKEMON, Database.getRandomPokemon())
    val firstPokemon: LiveData<Pokemon>
        get() = _firstPokemon

    private val _secondPokemon: MutableLiveData<Pokemon> =
        savedStateHandle.getLiveData(STATE_SECOND_POKEMON, Database.getRandomPokemon())
    val secondPokemon: LiveData<Pokemon>
        get() = _secondPokemon

    fun getWinnerPokemon(): Pokemon {
        val pokemon1 = firstPokemon.value ?: throw NullPointerException()
        val pokemon2 = secondPokemon.value ?: throw NullPointerException()

        return if (pokemon1.attack > pokemon2.attack) pokemon1 else pokemon2
    }

    fun changeFirstPokemon(pokemon: Pokemon) {
        _firstPokemon.value = pokemon
    }

    fun changeSecondPokemon(pokemon: Pokemon) {
        _secondPokemon.value = pokemon
    }
}