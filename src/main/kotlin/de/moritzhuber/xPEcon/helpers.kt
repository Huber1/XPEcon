package de.moritzhuber.xPEcon

fun getExpAtLevel(level: Int): Int = when {
    level <= 16 -> (level * level) + 6 * level
    level <= 31 -> (2.5 * (level * level) - 40.5 * level + 360).toInt()
    else -> (4.5 * (level * level) - 162.5 * level + 2220).toInt()
}
