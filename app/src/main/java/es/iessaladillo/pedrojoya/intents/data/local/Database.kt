package es.iessaladillo.pedrojoya.intents.data.local

import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.data.local.model.PokemonFactory
import java.lang.IllegalArgumentException
import java.lang.RuntimeException
import kotlin.random.Random

// TODO: Haz que Database implemente DataSource
object Database : DataSource{
    val pokemons: List<Pokemon> = PokemonFactory.sixPackPokemons()
    val random: Random = Random.Default

    override fun getRandomPokemon(): Pokemon {
        var randomSelectPokemon: Int = random.nextInt(6)

        return pokemons[randomSelectPokemon]
    }

    override fun getAllPokemons(): List<Pokemon> {
        return pokemons
    }

    override fun getPokemonById(id: Long): Pokemon {
        val pokemon = pokemons.filter { pokemon -> pokemon.id == id }

        if(pokemon.isEmpty()) {
            throw IllegalArgumentException("No existe ningún pokemon con ese ID")
        }

        return pokemon[0]
    }

}