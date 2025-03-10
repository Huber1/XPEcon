package de.moritzhuber.xPEcon

import kotlin.math.max

fun getTotalExpAtLevel(level: Int): Int = when {
    level <= 16 -> (level * level) + 6 * level
    level <= 31 -> (2.5 * (level * level) - 40.5 * level + 360)
    else -> (4.5 * (level * level) - 162.5 * level + 2220)
}.toInt()

fun getExpNeededForNextLevel(level: Int): Int = when {
    level <= 15 -> 2 * level + 7
    level <= 30 -> 5 * level - 38
    else -> 9 * level - 158
}

fun getLevelsFromExp(exp: Int): Pair<Int, Float> {
    var amount = max(0, exp)

    var level = 0
    while (amount > 0) {
        val expToLevel = getExpNeededForNextLevel(level)

        if (amount >= expToLevel) {
            // Enough exp for whole level
            level++
            amount -= expToLevel
        } else {
            // Not enought exp, fraction
            return Pair(level, amount.toFloat() / expToLevel.toFloat())
        }
    }
    return Pair(level, 0F)
}