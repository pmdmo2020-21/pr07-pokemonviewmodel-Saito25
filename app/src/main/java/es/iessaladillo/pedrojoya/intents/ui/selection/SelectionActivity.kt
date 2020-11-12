package es.iessaladillo.pedrojoya.intents.ui.selection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.BattleActivityBinding
import es.iessaladillo.pedrojoya.intents.databinding.SelectionActivityBinding
import es.iessaladillo.pedrojoya.intents.ui.battle.BattleActivity
import java.lang.IllegalArgumentException
import java.lang.NullPointerException

class SelectionActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POKEMON_ID: String = "EXTRA_POKEMON_ID"
        fun newIntent(context: Context, pokemonId: Long): Intent {
            return Intent(context, SelectionActivity::class.java)
                .putExtra(EXTRA_POKEMON_ID, pokemonId)
        }
    }

    private lateinit var binding: SelectionActivityBinding
    private lateinit var radioButtons: Array<RadioButton>
    private lateinit var imageViews: Array<ImageView>
    private lateinit var pokemon: Pokemon


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelectionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        receiveDataFromBattleActivity()
        setupFields()
        setupViews()
        setupInitState()
    }

    override fun onBackPressed() {
        setActivityResult()
        super.onBackPressed()
    }

    private fun setActivityResult() {
        val intent: Intent = BattleActivity.newIntent(this, pokemon.id)
        setResult(RESULT_OK, intent)
    }

    private fun receiveDataFromBattleActivity() {
        val pokemon: Pokemon

        chechIntentData()
        pokemon = extractPokemonFromId(intent.getLongExtra(EXTRA_POKEMON_ID, 0))
        selectCurrentPokemon(pokemon)
    }

    private fun setupFields() {
        radioButtons = arrayOf(
            binding.rbSelectionFirstTopPokemon,
            binding.rbSelectionSecondTopPokemon,
            binding.rbSelectionFirstMiddlePokemon,
            binding.rbSelectionSecondMiddlePokemon,
            binding.rbSelectionFisrtBottomPokemon,
            binding.rbSelectionSecondBottomPokemon,
        )

        imageViews = arrayOf(
            binding.ivSelectionFirstTopPokemon,
            binding.ivSelectionSecondTopPokemon,
            binding.ivSelectionFirstMiddlePokemon,
            binding.ivSelectionSecondMiddlePokemon,
            binding.ivSelectionFirstBottomPokemon,
            binding.ivSelectionSecondBottomPokemon,
        )
    }

    private fun setupViews() {
        setupTagsOfRadioButtons()
        setupTagsForImageViews()
        setupsRadioButtonsListener()
        setupImageViewsListener()
    }

    private fun setupInitState() {
        var pokemon: Pokemon

        for (radioButton in radioButtons) {
            pokemon = radioButton.tag as Pokemon

            if (this.pokemon == pokemon) {
                radioButton.isChecked = true
                return
            }
        }
    }

    private fun chechIntentData() {
        if (intent == null || !intent.hasExtra(EXTRA_POKEMON_ID)) {
            throw RuntimeException("La información del intent no es correcta")
        }
    }

    private fun extractPokemonFromId(pokemonId: Long): Pokemon {
        val pokemon: Pokemon = Database.getPokemonById(pokemonId)
            ?: throw NullPointerException("No se ha encontrado el pokemon dado")

        return pokemon
    }

    private fun setupTagsOfRadioButtons() {
        for (i in radioButtons.indices) {
            radioButtons[i].tag = Database.getPokemonById(i.toLong() + 1)
        }
    }

    private fun setupTagsForImageViews() {
        for (i in imageViews.indices) {
            imageViews[i].tag = radioButtons[i]
        }
    }

    private fun setupsRadioButtonsListener() {
        for (i in radioButtons.indices) {
            radioButtons[i].setOnClickListener { view: View ->
                checkRadioButton(view)
            }
        }
    }

    private fun setupImageViewsListener() {
        for (i in imageViews.indices) {
            imageViews[i].setOnClickListener { view: View ->
                listenOnClickimage(view)
            }
        }
    }

    private fun listenOnClickimage(view: View) {
        val radioButton: RadioButton = view.tag as RadioButton
        radioButton.isChecked = true
        checkRadioButton(radioButton)
    }

    private fun checkRadioButton(view: View) {
        val selectedRadioButton: RadioButton = view as RadioButton

        for (radioButton in radioButtons) {
            if (selectedRadioButton != radioButton) {
                radioButton.isChecked = false
            }
        }
        selectCurrentPokemonFromView(view)
    }

    private fun selectCurrentPokemonFromView(view: View) {
        val pokemon: Pokemon = view.tag as Pokemon
        selectCurrentPokemon(pokemon)
    }


    private fun selectCurrentPokemon(pokemon: Pokemon) {
        this.pokemon = pokemon
    }
}
