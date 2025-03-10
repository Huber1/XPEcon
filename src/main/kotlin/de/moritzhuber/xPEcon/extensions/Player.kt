package de.moritzhuber.xPEcon.extensions

import de.moritzhuber.xPEcon.getExpNeededForNextLevel
import de.moritzhuber.xPEcon.getLevelsFromExp
import de.moritzhuber.xPEcon.getTotalExpAtLevel
import org.bukkit.entity.Player

var Player.combinedExp: Int
    get() {
        val wholeLevels = getTotalExpAtLevel(level)
        val toNextLevel = getExpNeededForNextLevel(level)

        return wholeLevels + Math.round(exp * toNextLevel)
    }
    set(amount) {
        exp = 0F
        level = 0
        totalExperience = 0

        val required = getLevelsFromExp(amount)

        level = required.first
        exp = required.second

        totalExperience = amount
    }