@file:Suppress("UnstableApiUsage")

package de.moritzhuber.xPEcon.commands

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.tree.LiteralCommandNode
import de.moritzhuber.xPEcon.XPEcon
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.command.brigadier.argument.ArgumentTypes
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.milkbowl.vault.economy.EconomyResponse
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player


/**
 * /eco
 *   set
 *     <amount>
 *     <player>
 *         <amount>
 *   give
 *      <amount>
 *      <player>
 *          <amount>
 *   take
 *      <amount>
 *      <player>
 *          <amount>
 */

class EcoCommand(private val plugin: XPEcon) {
    fun command(): LiteralCommandNode<CommandSourceStack> = Commands.literal("economy")
        .requires { it.sender.hasPermission("xpecon.economy") }
        .then(
            Commands.literal("set")
                .requires { it.sender.hasPermission("xpecon.economy.set") }
                .then(
                    Commands.argument("amount", IntegerArgumentType.integer(0))
                        .executes { ctx ->
                            val amount = IntegerArgumentType.getInteger(ctx, "amount")

                            val p: Player? = when {
                                ctx.source.executor is Player -> ctx.source.executor
                                ctx.source.sender is Player -> ctx.source.sender
                                else -> null
                            } as Player?

                            if (p == null) {
                                ctx.source.sender.sendMessage(
                                    Component.text(
                                        "This Command must be executed by a Player",
                                        NamedTextColor.RED
                                    )
                                )
                                return@executes Command.SINGLE_SUCCESS
                            }

                            val response = setBalance(p, amount)

                            if (response?.errorMessage != null) {
                                ctx.source.sender.sendMessage(
                                    Component.text("Error setting Balance: ", NamedTextColor.GOLD)
                                        .append(Component.text(response.errorMessage, NamedTextColor.RED))
                                )

                                return@executes Command.SINGLE_SUCCESS
                            }

                            ctx.source.sender.sendMessage(
                                Component.text("Your balance was set to ", NamedTextColor.GREEN)
                                    .append(Component.text(amount, NamedTextColor.RED))
                                    .append(Component.text(" XP", NamedTextColor.GREEN))
                            )

                            Command.SINGLE_SUCCESS
                        })
                .then(
                    Commands.argument("targets", ArgumentTypes.players())
                        .then(
                            Commands.argument("amount", IntegerArgumentType.integer(0))
                                .executes { ctx ->
                                    val targetResolver =
                                        ctx.getArgument("targets", PlayerSelectorArgumentResolver::class.java)
                                    val players = targetResolver.resolve(ctx.source)
                                    val amount = IntegerArgumentType.getInteger(ctx, "amount")

                                    val errors = mutableListOf<String>()
                                    players.forEach { p ->
                                        val res = setBalance(p, amount)
                                        if (res?.errorMessage != null)
                                            errors.add(p.name)
                                    }

                                    if (errors.isEmpty())
                                        ctx.source.sender.sendMessage(
                                            Component.text("Set Balance of ", NamedTextColor.GREEN)
                                                .append(
                                                    Component.text(
                                                        players.joinToString(", ") { it.name },
                                                        NamedTextColor.DARK_RED
                                                    )
                                                )
                                                .append(Component.text(" to ", NamedTextColor.GREEN))
                                                .append(Component.text(amount, NamedTextColor.RED))
                                                .append(Component.text(" XP", NamedTextColor.GREEN))
                                        )
                                    else
                                        ctx.source.sender.sendMessage(
                                            Component.text("Error setting balance of ", NamedTextColor.GOLD)
                                                .append(
                                                    Component.text(
                                                        errors.joinToString(", "),
                                                        NamedTextColor.DARK_RED
                                                    )
                                                )
                                        )

                                    Command.SINGLE_SUCCESS
                                })
                )
                .executes { ctx ->
                    ctx.source.sender.sendMessage(
                        Component.text("Usage of ", NamedTextColor.GOLD)
                            .append(Component.text("/eco set", NamedTextColor.WHITE))
                            .appendNewline()
                            .append(Component.text("/eco set ", NamedTextColor.WHITE))
                            .append(Component.text("<player> <amount>", NamedTextColor.GOLD))

                    )
                    Command.SINGLE_SUCCESS
                }
        )
        .then(Commands.literal("give"))
        .then(Commands.literal("take"))
        .build()

    private fun setBalance(p: OfflinePlayer, amount: Int): EconomyResponse? {
        val eco = plugin.economy
        val balance = eco.getBalance(p)

        val response = if (balance < amount)
            eco.depositPlayer(p, amount - balance)
        else if (balance > amount)
            eco.withdrawPlayer(p, balance - amount)
        else null

        return response
    }

}


