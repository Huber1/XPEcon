package de.moritzhuber.xPEcon

import de.moritzhuber.xPEcon.extensions.combinedExp
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin

class PlayerXPService(private val plugin: JavaPlugin, private val offlineManager: OfflineManager) {
    fun get(player: OfflinePlayer): Int {
        // Online
        player.player?.let { p ->
            return p.combinedExp
        }

        return offlineManager.getExp(player)
    }

    fun set(player: OfflinePlayer, amount: Int) {
        // Online
        player.player?.let { p ->
            p.combinedExp = amount
            return
        }

        return offlineManager.setExp(player, amount)
    }

    fun withdraw(player: OfflinePlayer, amount: Int): EconomyResponse {
        val previous = get(player)
        val modified = previous - amount
        try {
            set(player, modified)
        } catch (e: PlayerNotFoundException) {
            return EconomyResponse(
                0.toDouble(),
                previous.toDouble(),
                EconomyResponse.ResponseType.FAILURE,
                e.message
            )
        }
        val new = get(player).toDouble()
        return EconomyResponse(
            previous - new,
            new,
            EconomyResponse.ResponseType.SUCCESS,
            null,
        )
    }

    fun deposit(player: OfflinePlayer, amount: Int): EconomyResponse {
        val previous = get(player)
        val modified = previous + amount
        try {
            set(player, modified)
        } catch (e: PlayerNotFoundException) {
            return EconomyResponse(
                0.toDouble(),
                previous.toDouble(),
                EconomyResponse.ResponseType.FAILURE,
                e.message
            )
        }
        val new = get(player).toDouble()
        return EconomyResponse(
            new - previous,
            new,
            EconomyResponse.ResponseType.SUCCESS,
            null,
        )
    }
}
