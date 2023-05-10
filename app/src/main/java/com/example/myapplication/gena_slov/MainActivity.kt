package com.example.myapplication.gena_slov
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.trevjonez.markov.MarkovChain
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var generateButton: Button
    private lateinit var phraseTextView: TextView
    private val markovChain = MarkovChain(2) // Create an instance of MarkovChain with n=2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        generateButton = findViewById(R.id.generate_button)
        phraseTextView = findViewById(R.id.phrase_text_view)

        // Read words from file and pass them to MarkovChain instance
        try {
            BufferedReader(InputStreamReader(assets.open("words.txt"))).use { reader ->
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    markovChain.addText(line!!)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        // Set click listener for generateButton
        generateButton.setOnClickListener {
            // Generate a new phrase
            val generatedPhrase = markovChain.generatePhrase(10)

            // Display the generated phrase in the TextView
            phraseTextView.text = generatedPhrase
        }
    }
}
