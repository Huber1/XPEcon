package de.moritzhuber.xPEcon

import org.bukkit.plugin.java.JavaPlugin

class XPEcon : JavaPlugin() {

    val offlineManager = OfflineManager(this)

    override fun onEnable() {
        // Plugin startup logic
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
