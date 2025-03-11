package de.moritzhuber.xPEcon

import de.moritzhuber.xPEcon.commands.EcoCommand
import de.moritzhuber.xPEcon.commands.balanceCommand
import io.papermc.paper.command.brigadier.Commands
import io.papermc.paper.plugin.lifecycle.event.handler.LifecycleEventHandler
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.milkbowl.vault.economy.Economy
import org.bukkit.Bukkit
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.ServicePriority
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.time.LocalDateTime
import java.util.*


class XPEcon : JavaPlugin() {

    private val properties = Properties()
    private val offlineManager = OfflineManager(this, properties)
    private val xpService = PlayerXPService(this, offlineManager)
    internal lateinit var economy: Economy

    @Suppress("UnstableApiUsage")
    override fun onEnable() {
        saveDefaultConfig()
        properties.load(File("server.properties").inputStream())

        Bukkit.getServicesManager().register(Economy::class.java, XPEconomy(xpService), this, ServicePriority.Normal)

        if (!setupEconomy()) {
            logger.severe("Could not find vault dependency. Disabling plugin")
            server.pluginManager.disablePlugin(this)
            return
        }

        lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS, LifecycleEventHandler { event ->
            val commands: Commands = event.registrar()
            commands.register(
                balanceCommand(this),
                listOf("bal"),
            )
            commands.register(
                EcoCommand(this).command(),
                listOf("eco")
            )
        })
    }

    private fun setupEconomy(): Boolean {
        if (server.pluginManager.getPlugin("Vault") == null) {
            return false
        }
        val rsp = server.servicesManager.getRegistration(
            Economy::class.java
        )
        if (rsp == null) {
            return false
        }
        economy = rsp.provider
        return true
    }

    override fun onDisable() {
        // Plugin shutdown logic
        cleanupPlayerDatBackups()
    }

    private fun cleanupPlayerDatBackups() {
        val folder = File("XPEcon")
        val now = LocalDateTime.now()
        val lastWeek = now.minusWeeks(1)

        for(playerFolder in folder.listFiles() ?: arrayOf()) {
            if(!playerFolder.isDirectory) continue

            for (file in playerFolder.listFiles() ?: arrayOf()) {
                val dateString = file.name.split(".").subList(1,2).joinToString(".")
                val date = LocalDateTime.parse(dateString)
                if (date.isBefore(lastWeek))
                    file.delete()
            }

            if(playerFolder.listFiles()?.isEmpty() != false)
                playerFolder.deleteRecursively()
        }
    }
}