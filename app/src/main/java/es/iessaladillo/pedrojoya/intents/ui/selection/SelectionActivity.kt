package es.iessaladillo.pedrojoya.intents.ui.selection

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.RadioButton
import androidx.activity.viewModels
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
        const val EXTRA_POKEMON: String = "EXTRA_POKEMON"
        fun newIntent(context: Context, pokemon: Pokemon): Intent {
            return Intent(context, SelectionActivity::class.java)
                .putExtra(EXTRA_POKEMON, pokemon)
        }
    }

    private lateinit var binding: SelectionActivityBinding
    private lateinit var radioButtons: Array<RadioButton>
    private lateinit var imageViews: Array<ImageView>

    private val viewModel: SelectionActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = SelectionActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        receiveDataFromBattleActivity()
        setupFields()
        setupViews()
        observePokemon()
    }

    override fun onBackPressed() {
        setActivityResult()
        super.onBackPressed()
    }

    private fun setActivityResult() {
        val pokemon: Pokemon? = viewModel.selectPokemon.value
        if (pokemon != null) {
            val data: Intent = BattleActivity.newIntent(this, pokemon)
            setResult(RESULT_OK, data)
        }
    }

    private fun receiveDataFromBattleActivity() {
        chechIntentData()
        val pokemon: Pokemon? = intent.getParcelableExtra(EXTRA_POKEMON)

        if (pokemon != null) {
            viewModel.changeSelectedPokemon(pokemon)
        }
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


    private fun observePokemon() {
        viewModel.selectPokemon.observe(this) {
            showSelectedPokemonChange(it)
        }
    }

    private fun showSelectedPokemonChange(pokemon: Pokemon) {
        showSelectedRadioButton(pokemon)
    }

    private fun showSelectedRadioButton(selectedPokemon: Pokemon) {
        var pokemon: Pokemon

        for (radioButton in radioButtons) {
            pokemon = radioButton.tag as Pokemon
            if (selectedPokemon != pokemon) {
                radioButton.isChecked = false
            } else {
                if(!radioButton.isChecked) {
                    radioButton.isChecked = true
                }
            }
        }
    }

    private fun chechIntentData() {
        if (intent == null || !intent.hasExtra(EXTRA_POKEMON)) {
            throw RuntimeException("La información del intent no es correcta")
        }
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

    private fun checkRadioButton(view: View) {
        val selectedRadioButton: RadioButton = view as RadioButton
        val pokemon: Pokemon = selectedRadioButton.tag as Pokemon
        viewModel.changeSelectedPokemon(pokemon)
    }

    private fun listenOnClickimage(view: View) {
        val radioButton: RadioButton = view.tag as RadioButton
        checkRadioButton(radioButton)
    }
}
