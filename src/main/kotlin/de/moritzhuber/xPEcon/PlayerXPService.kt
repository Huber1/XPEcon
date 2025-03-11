package de.moritzhuber.xPEcon

import de.moritzhuber.xPEcon.exceptions.OfflineModificationNotAllowedException
import de.moritzhuber.xPEcon.exceptions.PlayerNotFoundException
import de.moritzhuber.xPEcon.extensions.combinedExp
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.max

class PlayerXPService(private val plugin: JavaPlugin, private val offlineManager: OfflineManager) {
    fun get(player: OfflinePlayer): Int {
        // Online
        player.player?.let { p ->
            return p.combinedExp
        }

        return offlineManager.getExp(player)
    }

    fun set(player: OfflinePlayer, amount: Int) {
        val newAmount = max(0, amount)

        // Online
        player.player?.let { p ->
            p.combinedExp = newAmount
            return
        }

        if(!plugin.config.getBoolean("allow-offline"))
            throw OfflineModificationNotAllowedException()

        return offlineManager.setExp(player, newAmount)
    }

    fun withdraw(player: OfflinePlayer, amount: Int): EconomyResponse {
        val previous = get(player)
        val modified = previous - amount
        try {
            set(player, modified)
        } catch (e: Exception) {
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
        } catch (e: Exception) {
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
