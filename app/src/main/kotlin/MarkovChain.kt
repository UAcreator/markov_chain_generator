package com.example.myapplication.markov


import java.util.regex.Pattern

class MarkovChain(private val n: Int) {
    private val chain = mutableMapOf<List<String>, MutableMap<String, Int>>()

    fun addText(text: String) {
        val pattern = Pattern.compile("\\b\\w+\\b")
        val matcher = pattern.matcher(text)
        var words = mutableListOf<String>()
        while (matcher.find()) {
            words.add(matcher.group())
        }
        if (words.size < n + 1) return
        for (i in 0 until words.size - n) {
            val key = words.subList(i, i + n)
            val value = words[i + n]
            val valuesMap = chain.getOrPut(key) { mutableMapOf() }
            valuesMap[value] = valuesMap.getOrDefault(value, 0) + 1
        }
    }

    fun generatePhrase(length: Int): String {
        val keys = chain.keys.toList()
        var currentKey = keys.random()
        val words = mutableListOf<String>()
        words.addAll(currentKey)
        repeat(length - n) {
            val nextWords = chain[currentKey]
            if (nextWords == null) return@repeat
            val totalOccurrences = nextWords.values.sum()
            val wordFrequencies = nextWords.mapValues { (_, count) -> count.toDouble() / totalOccurrences }
            var nextWord = ""
            var maxProbability = 0.0
            for ((word, frequency) in wordFrequencies) {
                if (frequency > maxProbability) {
                    maxProbability = frequency
                    nextWord = word
                }
            }
            words.add(nextWord)
            currentKey = currentKey.drop(1) + nextWord
        }
        return words.joinToString(" ")
    }

    fun addFrequency() {
        for ((_, valuesMap) in chain) {
            var totalOccurences = 0
            for ((_, count) in valuesMap) {
                totalOccurences += count
            }
            for ((key, count) in valuesMap) {
                valuesMap[key] = count * 100 / totalOccurences
            }
        }
    }
}