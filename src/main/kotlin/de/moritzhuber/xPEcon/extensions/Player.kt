package de.moritzhuber.xPEcon.extensions

import de.moritzhuber.xPEcon.getExpAtLevel
import org.bukkit.entity.Player

fun Player.combinedExp() {
    val toLevel = getExpAtLevel(level)

}
