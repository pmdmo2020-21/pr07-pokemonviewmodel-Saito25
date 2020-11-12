package es.iessaladillo.pedrojoya.intents.data.local.model

import es.iessaladillo.pedrojoya.intents.R

class PokemonFactory private constructor(){

    companion object {
        fun sixPackPokemons() : List<Pokemon> {
            return listOf(
                Pokemon(1, R.drawable.bulbasur, R.string.rb_Selection_bulbasur, 15),
                Pokemon(2, R.drawable.giratina, R.string.rb_Selection_giratina, 100),
                Pokemon(3, R.drawable.cubone, R.string.rb_Selection_cubone, 20),
                Pokemon(4, R.drawable.gyarados, R.string.rb_Selection_gyarados, 40),
                Pokemon(5, R.drawable.feebas, R.string.rb_Selection_feebas, 5),
                Pokemon(6, R.drawable.pikachu, R.string.rb_Selection_pikachu, 10),
            )
        }
    }
}