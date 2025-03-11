package de.moritzhuber.xPEcon.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import de.moritzhuber.xPEcon.exceptions.PlayerNotFoundException
import de.moritzhuber.xPEcon.XPEcon
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

@Suppress("UnstableApiUsage")
fun balanceCommand(plugin: XPEcon): LiteralCommandNode<CommandSourceStack> = Commands.literal("balance")
    .executes { ctx ->
        val sender: CommandSender = ctx.source.sender
        val executor: Entity? = ctx.source.executor

        if (executor != null && executor is Player) {
            val xp = plugin.economy.getBalance(executor).toInt()
            sendOwnBalanceMessage(executor, xp)
        } else if (sender is Player) {
            val xp = plugin.economy.getBalance(sender).toInt()
            sendOwnBalanceMessage(sender, xp)
        } else {
            sender.sendMessage(Component.text("This command must be executed by a player", NamedTextColor.RED))
        }

        Command.SINGLE_SUCCESS
    }
    .then(
        Commands.argument("player", StringArgumentType.word())
            .requires { it.sender.hasPermission("xpecon.balance.others") }
            .suggests { _, builder ->
                plugin.server.onlinePlayers.forEach { player ->
                    builder.suggest(player.name)
                }
                builder.buildFuture()
            }
            .executes { ctx ->
                val playerName = StringArgumentType.getString(ctx, "player")

                try {
                    val xp = plugin.economy.getBalance(Bukkit.getOfflinePlayer(playerName)).toInt()
                    ctx.source.sender.sendMessage(
                        Component.text("Balance of ", NamedTextColor.GREEN)
                            .append(Component.text(playerName, NamedTextColor.RED))
                            .append(Component.text(": ", NamedTextColor.GREEN))
                            .append(Component.text(xp, NamedTextColor.RED))
                            .append(Component.text(" XP", NamedTextColor.GREEN))
                    )
                } catch (exception: PlayerNotFoundException) {
                    ctx.source.sender.sendMessage(
                        Component.text("Player ", NamedTextColor.GOLD)
                            .append(Component.text(playerName, NamedTextColor.RED))
                            .append(Component.text(" does not exist", NamedTextColor.GOLD))
                    )
                }
                Command.SINGLE_SUCCESS
            })
    .build()

private fun sendOwnBalanceMessage(sender: CommandSender, xp: Int) {
    sender.sendMessage(
        Component.text("Balance: ", NamedTextColor.GREEN)
            .append(Component.text(xp, NamedTextColor.RED))
            .append(Component.text(" XP", NamedTextColor.GREEN))
    )
}