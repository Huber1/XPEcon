package de.moritzhuber.xPEcon

import net.milkbowl.vault.economy.Economy
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.Bukkit
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import kotlin.math.absoluteValue

class XPEconomy(
    private val xpService: PlayerXPService,
) : Economy {
    override fun isEnabled(): Boolean = true

    override fun getName(): String = "XPEcon"

    override fun hasBankSupport(): Boolean = false

    override fun fractionalDigits(): Int = 0

    override fun format(amount: Double): String = "$amount XP"

    override fun currencyNamePlural(): String = "XP"

    override fun currencyNameSingular(): String = "XP"

    @Deprecated("Deprecated in Java", ReplaceWith("hasAccount(player)"))
    override fun hasAccount(playerName: String?): Boolean = true

    override fun hasAccount(player: OfflinePlayer?): Boolean = true

    @Deprecated("Deprecated in Java", ReplaceWith("hasAccount(player, worldName)"))
    override fun hasAccount(playerName: String?, worldName: String?): Boolean = true

    override fun hasAccount(player: OfflinePlayer?, worldName: String?): Boolean = true

    @Deprecated("Deprecated in Java", ReplaceWith("getBalance(player)"))
    override fun getBalance(playerName: String): Double {
        val player = Bukkit.getOfflinePlayer(playerName)
        return getBalance(player)
    }

    override fun getBalance(player: OfflinePlayer): Double = xpService.get(player).toDouble()

    @Deprecated("Deprecated in Java", ReplaceWith("getBalance(player, world)"))
    override fun getBalance(playerName: String, world: String?): Double = getBalance(playerName)

    override fun getBalance(player: OfflinePlayer, world: String?): Double = getBalance(player)

    fun setBalance(player: OfflinePlayer, amount: Double): EconomyResponse {
        val bal = getBalance(player)
        xpService.set(player, amount.toInt())
        val new = getBalance(player)
        return EconomyResponse(
            (bal - amount).absoluteValue,
            new,
            EconomyResponse.ResponseType.SUCCESS,
            null
        )
    }

    @Deprecated("Deprecated in Java", ReplaceWith("has(player)"))
    override fun has(playerName: String, amount: Double): Boolean {
        val player = Bukkit.getOfflinePlayer(playerName)
        return has(player, amount)
    }

    override fun has(player: OfflinePlayer, amount: Double): Boolean = xpService.get(player) >= amount

    @Deprecated("Deprecated in Java", ReplaceWith("has(player, world, amount)"))
    override fun has(playerName: String, worldName: String?, amount: Double): Boolean = has(playerName, amount)

    override fun has(player: OfflinePlayer, worldName: String?, amount: Double): Boolean = has(player, amount)

    @Deprecated("Deprecated in Java")
    override fun withdrawPlayer(playerName: String, amount: Double): EconomyResponse {
        val player = Bukkit.getOfflinePlayer(playerName)
        return withdrawPlayer(player, amount)
    }

    override fun withdrawPlayer(player: OfflinePlayer, amount: Double): EconomyResponse =
        xpService.withdraw(player, amount.toInt())

    @Deprecated("Deprecated in Java", ReplaceWith("withdrawPlayer(player, amount)"))
    override fun withdrawPlayer(playerName: String, worldName: String?, amount: Double): EconomyResponse =
        withdrawPlayer(playerName, amount)

    override fun withdrawPlayer(player: OfflinePlayer, worldName: String?, amount: Double): EconomyResponse =
        withdrawPlayer(player, amount)

    @Deprecated("Deprecated in Java")
    override fun depositPlayer(playerName: String, amount: Double): EconomyResponse {
        val player = Bukkit.getOfflinePlayer(playerName)
        return depositPlayer(player, amount)
    }

    override fun depositPlayer(player: OfflinePlayer, amount: Double): EconomyResponse =
        xpService.deposit(player, amount.toInt())

    @Deprecated("Deprecated in Java", ReplaceWith("depositPlayer(player, amount)"))
    override fun depositPlayer(playerName: String, worldName: String?, amount: Double): EconomyResponse =
        depositPlayer(playerName, amount)

    override fun depositPlayer(player: OfflinePlayer, worldName: String?, amount: Double): EconomyResponse =
        depositPlayer(player, amount)

    @Deprecated("Deprecated in Java")
    override fun createBank(name: String?, player: String): EconomyResponse {
        val offlinePlayer = Bukkit.getOfflinePlayer(player)
        return createBank(name, offlinePlayer)
    }

    override fun createBank(name: String?, player: OfflinePlayer): EconomyResponse = EconomyResponse(
        0.0,
        xpService.get(player).toDouble(),
        EconomyResponse.ResponseType.NOT_IMPLEMENTED,
        "Not Implemented",
    )

    override fun deleteBank(name: String?): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    override fun bankBalance(name: String?): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    override fun bankHas(name: String?, amount: Double): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    override fun bankWithdraw(name: String?, amount: Double): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    override fun bankDeposit(name: String?, amount: Double): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "isBankOwner(name, player)",
            "net.milkbowl.vault.economy.EconomyResponse",
        )
    )
    override fun isBankOwner(name: String?, playerName: String?): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    override fun isBankOwner(name: String?, player: OfflinePlayer?): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    @Deprecated(
        "Deprecated in Java", ReplaceWith(
            "isBankMember(name, player)",
            "net.milkbowl.vault.economy.EconomyResponse",
        )
    )
    override fun isBankMember(name: String?, playerName: String?): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    override fun isBankMember(name: String?, player: OfflinePlayer?): EconomyResponse =
        EconomyResponse(0.0, 0.0, EconomyResponse.ResponseType.NOT_IMPLEMENTED, "Not Implemented")

    override fun getBanks(): MutableList<String> = mutableListOf()

    @Deprecated("Deprecated in Java", ReplaceWith("createPlayerAccount(player)"))
    override fun createPlayerAccount(playerName: String?): Boolean = false

    override fun createPlayerAccount(player: OfflinePlayer?): Boolean = false

    @Deprecated("Deprecated in Java", ReplaceWith("createPlayerAccount(player, worldName)"))
    override fun createPlayerAccount(playerName: String?, worldName: String?): Boolean = false

    override fun createPlayerAccount(player: OfflinePlayer?, worldName: String?): Boolean = false
}
