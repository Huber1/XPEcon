package de.moritzhuber.xPEcon

import org.bukkit.OfflinePlayer

class PlayerXPService(private val offlineManager: OfflineManager) {
    fun getXp(player: OfflinePlayer): Double {
        player.player?.let { p ->
            p.exp
            return getExpAtLevel(p.level) + p.exp
        }

        return offlineManager.getXp(player) ?: 0.0
    }

    fun setXp(player: OfflinePlayer, xp: Double) {

    }
}
