package com.trevjonez.markov

class MarkovChain(private val n: Int) {
    private val chain = mutableMapOf<List<String>, MutableList<String>>()

    fun addText(text: String) {
        val words = text.split("\\W+".toRegex()) // разбиваем текст по всем не-буквенным символам
            .filter { it.isNotEmpty() } // отфильтровываем пустые строки
        if (words.size < n + 1) return
        for (i in 0 until words.size - n) {
            val key = words.subList(i, i + n)
            val value = words[i + n]
            chain.getOrPut(key) { mutableListOf() }.add(value)
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
            val nextWord = nextWords.random()
            words.add(nextWord)
            currentKey = currentKey.drop(1) + nextWord
        }
        return words.joinToString(" ")
    }
}
