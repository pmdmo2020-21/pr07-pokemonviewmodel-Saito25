package es.iessaladillo.pedrojoya.intents.ui.winner

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.WinnerActivityBinding
import es.iessaladillo.pedrojoya.intents.ui.selection.SelectionActivity
import java.lang.RuntimeException

class WinnerActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_WINNER: String = "EXTRA_WINNER"
        fun newIntent(context: Context, pokemon: Pokemon): Intent {
            return Intent(context, WinnerActivity::class.java)
                .putExtra(EXTRA_WINNER, pokemon)
        }
    }

    private lateinit var binding: WinnerActivityBinding
    private val viewModel: WinnerActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = WinnerActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        receiveDataFromBattleActivity()
        observePokemon()
    }

    private fun receiveDataFromBattleActivity() {
        checkIntentDataFromBattleActivity()

        val pokemon: Pokemon? = intent.getParcelableExtra(EXTRA_WINNER)

        if (pokemon != null) {
            viewModel.changeWinnerPokemon(pokemon)
        }
    }

    private fun observePokemon() {
        viewModel.winnerPokemon.observe(this) { showWinnerPokemonChange(it) }
    }

    private fun checkIntentDataFromBattleActivity() {
        if (intent == null || !intent.hasExtra(EXTRA_WINNER)) {
            throw RuntimeException("La información proporcionada por el intent no es válida")
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showWinnerPokemonChange(pokemon: Pokemon) {
        binding.ivWinnerPokemon.setImageDrawable(getDrawable(pokemon.drawable))
        binding.txtWinnerSubtitle.text = getString(pokemon.name)
    }

   /* private fun setupViews() {
        binding.ivWinnerPokemon.setImageDrawable(getDrawable(pokemon.drawable))
        binding.txtWinnerSubtitle.text = getString(pokemon.name)
    }*/

}