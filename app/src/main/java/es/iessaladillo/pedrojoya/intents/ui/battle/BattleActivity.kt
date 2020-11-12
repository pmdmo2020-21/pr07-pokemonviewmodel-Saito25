package es.iessaladillo.pedrojoya.intents.ui.battle

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import es.iessaladillo.pedrojoya.intents.data.local.Database
import es.iessaladillo.pedrojoya.intents.data.local.model.Pokemon
import es.iessaladillo.pedrojoya.intents.databinding.BattleActivityBinding
import es.iessaladillo.pedrojoya.intents.ui.selection.SelectionActivity
import es.iessaladillo.pedrojoya.intents.ui.winner.WinnerActivity
import java.lang.IllegalArgumentException
import java.lang.RuntimeException

@Suppress("DEPRECATION")
class BattleActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_POKEMON_ID: String = "EXTRA_POKEMON_ID"

        fun newIntent(context: Context, pokemonId: Long): Intent {
            return Intent(context, BattleActivity::class.java)
                .putExtra(EXTRA_POKEMON_ID, pokemonId)
        }
    }

    private lateinit var binding: BattleActivityBinding
    private val pokemonIds: LongArray = longArrayOf(0, 0)
    private lateinit var selectFisrtPokemonCall: ActivityResultLauncher<Intent>
    private lateinit var selectSecondPokemonCall: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = BattleActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupFields()
        setupViews()
    }

    private fun setupFields() {
        selectFisrtPokemonCall =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (checkActivityResult(result.resultCode, result.data)) {
                    sendPokemonForSet(
                        result.data, binding.ivBattleFirstPokemon,
                        binding.txtBattleFirstPokemonName, 0
                    )
                }
            }

        selectSecondPokemonCall =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (checkActivityResult(result.resultCode, result.data)) {
                    sendPokemonForSet(
                        result.data, binding.ivBattleSecondPokemon,
                        binding.txtBattleSecondPokemonName, 1
                    )
                }
            }
    }

    private fun sendPokemonForSet(
        data: Intent?,
        ivBattleFirstPokemon: ImageView,
        txtBattleFirstPokemonName: TextView,
        indexOfArray: Int
    ) {
        if (data == null) {
            throw IllegalArgumentException("La información recibida no puede ser nula")
        }

        pokemonIds[indexOfArray] = data.getLongExtra(EXTRA_POKEMON_ID, 0)
        setPokemonById(
            ivBattleFirstPokemon,
            txtBattleFirstPokemonName,
            pokemonIds[indexOfArray]
        )
    }

    private fun checkActivityResult(resultCode: Int, data: Intent?): Boolean {
        return resultCode == RESULT_OK && data != null

    }

    private fun setupViews() {
        setPokemonsRandoms()
        binding.llBattleTopRectangle.setOnClickListener {
            navigateToSelectionActivity(pokemonIds[0], selectFisrtPokemonCall)
        }
        binding.llBattleMiddleRectangle.setOnClickListener {
            navigateToSelectionActivity(pokemonIds[1], selectSecondPokemonCall)
        }
        binding.btnBattleLaunchBattle.setOnClickListener {
            val winnerPokemon: Long = chooseWinnerPokemon()
            navigateToWinnerActivity(winnerPokemon)
        }
    }

    private fun chooseWinnerPokemon(): Long {
        val pokemon1: Pokemon = Database.getPokemonById(pokemonIds[0])
        val pokemon2: Pokemon = Database.getPokemonById(pokemonIds[1])

        return if (pokemon1.attack > pokemon2.attack) pokemon1.id else pokemon2.id
    }

    private fun navigateToWinnerActivity(winnerPokemon: Long) {
        val intentForWinnerActivity = WinnerActivity.newIntent(this, winnerPokemon)
        startActivity(intentForWinnerActivity)
    }

    private fun navigateToSelectionActivity(
        pokemonId: Long,
        activityResultLauncher: ActivityResultLauncher<Intent>
    ) {
        val intentforSelectionActivity: Intent = SelectionActivity.newIntent(this, pokemonId)
        activityResultLauncher.launch(intentforSelectionActivity)
    }

    private fun setPokemonsRandoms() {
        setPokemonRandom(
            binding.ivBattleFirstPokemon, binding.txtBattleFirstPokemonName,
            0
        )
        setPokemonRandom(
            binding.ivBattleSecondPokemon, binding.txtBattleSecondPokemonName,
            1
        )
    }

    private fun setPokemonById(
        ivBattlePokemon: ImageView,
        txtBattlePokemonName: TextView, pokemonId: Long
    ) {
        val pokemon: Pokemon = Database.getPokemonById(pokemonId)

        setPokemonImageForBattle(ivBattlePokemon, pokemon.drawable)
        setPokemonNameForBattle(txtBattlePokemonName, pokemon.name)
    }

    private fun setPokemonRandom(
        imageOfPokemon: ImageView,
        nameOfPokemon: TextView,
        positionInArray: Int
    ) {
        val choosedPokemon: Pokemon = chooseRandomPokemon()

        setPokemonImageForBattle(imageOfPokemon, choosedPokemon.drawable)
        setPokemonNameForBattle(nameOfPokemon, choosedPokemon.name)
        pokemonIds[positionInArray] = choosedPokemon.id
    }

    private fun chooseRandomPokemon(): Pokemon {
        return Database.getRandomPokemon()
    }

    private fun setPokemonImageForBattle(imageView: ImageView, drawable: Int) {
        imageView.setImageDrawable(getDrawable(drawable))
    }

    private fun setPokemonNameForBattle(textView: TextView, name: Int) {
        textView.text = getText(name)
    }
}