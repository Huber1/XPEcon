package de.moritzhuber.xPEcon

import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.IntegerArgumentType
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.context.CommandContext
import de.moritzhuber.xPEcon.extensions.combinedExp
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*


class XPEcon : JavaPlugin() {

    private val properties = Properties()
    private val offlineManager = OfflineManager(this, properties)
    private val xpService = PlayerXPService(this, offlineManager)

    @Suppress("UnstableApiUsage")
    override fun onEnable() {
        properties.load(File("server.properties").inputStream())

        Bukkit.getServicesManager().register(Economy::class.java, Econ(this, xpService), this, ServicePriority.Normal)

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, LifecycleEventHandler { event ->
            val commands: Commands = event.registrar()
            commands.register(
                Commands.literal("currentxp")
                    .then(
                        Commands.argument("player", StringArgumentType.word())
                            .executes { ctx ->
                                val player = StringArgumentType.getString(ctx, "player")
                                ctx.source.sender.sendPlainMessage(
                                    "$player has ${
                                        xpService.get(
                                            Bukkit.getOfflinePlayer(
                                                player
                                            )
                                        )
                                    } XP"
                                )

                                Command.SINGLE_SUCCESS
                            })
                    .executes { ctx: CommandContext<CommandSourceStack> ->
                        val player = ctx.source.sender as Player
                        player.sendPlainMessage("You have ${player.combinedExp} XP")
                        Command.SINGLE_SUCCESS
                    }
                    .build(),
            )

            commands.register(
                Commands.literal("setExp")
                    .then(
                        Commands.argument("amount", IntegerArgumentType.integer())
                            .executes { ctx ->
                                val amount = IntegerArgumentType.getInteger(ctx, "amount")

                                xpService.set(ctx.source.sender as Player, amount)

                                Command.SINGLE_SUCCESS
                            }.then(
                                Commands.argument("player", StringArgumentType.word())
                                    .executes { ctx ->
                                        val amount = IntegerArgumentType.getInteger(ctx, "amount")
                                        val player = StringArgumentType.getString(ctx, "player")

                                        xpService.set(Bukkit.getOfflinePlayer(player), amount)

                                        Command.SINGLE_SUCCESS
                                    }
                            )
                    )
                    .build(),
            )

            commands.register(
                Commands.literal("giveExp")
                    .then(
                        Commands.argument("amount", IntegerArgumentType.integer())
                            .executes { ctx ->
                                val amount = IntegerArgumentType.getInteger(ctx, "amount")

                                xpService.deposit(ctx.source.sender as Player, amount)

                                Command.SINGLE_SUCCESS
                            }
                            .then(
                                Commands.argument("player", StringArgumentType.word())
                                    .executes { ctx ->
                                        val amount = IntegerArgumentType.getInteger(ctx, "amount")
                                        val player = StringArgumentType.getString(ctx, "player")

                                        xpService.deposit(Bukkit.getOfflinePlayer(player), amount)

                                        Command.SINGLE_SUCCESS
                                    })
                    )
                    .build(),
            )
        })
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}