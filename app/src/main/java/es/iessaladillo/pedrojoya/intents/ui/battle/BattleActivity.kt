package es.iessaladillo.pedrojoya.intents.ui.battle

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.BattleActivityBinding
import es.iessaladillo.pedrojoya.intents.ui.selection.SelectionActivity
import es.iessaladillo.pedrojoya.intents.ui.winner.WinnerActivity
import java.lang.NullPointerException

class BattleActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POKEMON: String = "EXTRA_POKEMON"
        fun newIntent(context: Context, pokemon: Pokemon): Intent {
            return Intent(context, BattleActivity::class.java)
                .putExtra(EXTRA_POKEMON, pokemon)
        }
    }

    private val viewModel: BattleActivityViewModel by viewModels()
    private lateinit var binding: BattleActivityBinding
    private lateinit var selectFisrtPokemonCall: ActivityResultLauncher<Intent>
    private lateinit var selectSecondPokemonCall: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BattleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFields()
        setupViews()
        observePokemons()
    }

    private fun setupFields() {
        selectFisrtPokemonCall =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (checkActivityResult(result.resultCode, result.data)) {
                    val pokemon: Pokemon? = result.data?.getParcelableExtra(EXTRA_POKEMON)
                    if(pokemon != null) {
                        viewModel.changeFirstPokemon(pokemon)
                    }
                }
            }

        selectSecondPokemonCall =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (checkActivityResult(result.resultCode, result.data)) {
                    val pokemon: Pokemon? = result.data?.getParcelableExtra(EXTRA_POKEMON)
                    if(pokemon != null) {
                        viewModel.changeSecondPokemon(pokemon)
                    }
                }
            }
    }

    private fun setupViews() {
        binding.llBattleTopRectangle.setOnClickListener {
            navigateToSelectionActivity(viewModel.firstPokemon, selectFisrtPokemonCall)
        }
        binding.llBattleMiddleRectangle.setOnClickListener {
            navigateToSelectionActivity(viewModel.secondPokemon, selectSecondPokemonCall)
        }
        binding.btnBattleLaunchBattle.setOnClickListener {
            val winnerPokemon: Pokemon = viewModel.getWinnerPokemon()
            navigateToWinnerActivity(winnerPokemon)
        }
    }

    private fun observePokemons() {
        viewModel.firstPokemon.observe(this) { showFirstPokemonChange(it) }
        viewModel.secondPokemon.observe(this) { showSecondPokemonChange(it) }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showFirstPokemonChange(pokemon: Pokemon) {
        binding.ivBattleFirstPokemon.setImageDrawable(getDrawable(pokemon.drawable))
        binding.txtBattleFirstPokemonName.text = getString(pokemon.name)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun showSecondPokemonChange(pokemon: Pokemon) {
        binding.ivBattleSecondPokemon.setImageDrawable(getDrawable(pokemon.drawable))
        binding.txtBattleSecondPokemonName.text = getString(pokemon.name)
    }


    private fun checkActivityResult(resultCode: Int, data: Intent?): Boolean {
        return resultCode == RESULT_OK && data != null
    }

    private fun navigateToWinnerActivity(winnerPokemon: Pokemon) {
        val intentForWinnerActivity = WinnerActivity.newIntent(this, winnerPokemon)
        startActivity(intentForWinnerActivity)
    }

    private fun navigateToSelectionActivity(
        pokemon: LiveData<Pokemon>,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ) {
        val pokemonNoNull = pokemon.value ?: throw NullPointerException()
        val intentforSelectionActivity: Intent = SelectionActivity.newIntent(this, pokemonNoNull)
        activityResultLauncher.launch(intentforSelectionActivity)
    }


}