package de.moritzhuber.xPEcon

import net.querz.nbt.io.NBTUtil
import net.querz.nbt.tag.CompoundTag
import org.bukkit.OfflinePlayer
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

class OfflineManager(private val plugin: JavaPlugin, properties: Properties) {
    //private val levelName: String = properties.getProperty("level-name")
    private val levelName: String = "world"

    private fun getFile(player: OfflinePlayer): File = File("$levelName/playerdata/${player.uniqueId}.dat")

    /**
     * @throws PlayerNotFoundException if Player doesn't exist
     */
    private fun getCompoundTag(player: OfflinePlayer): CompoundTag {
        val file = getFile(player)
        if (!file.exists())
            throw PlayerNotFoundException()

        return NBTUtil.read(file).tag as CompoundTag
    }

    /**
     * @throws PlayerNotFoundException if Player doesn't exist
     */
    fun getExp(player: OfflinePlayer): Int {
        plugin.logger.info("Getting XP for offline Player ${player.uniqueId}")
        val tag = getCompoundTag(player)

        val level = tag.getInt("XpLevel")
        val exp = tag.getFloat("XpP")

        val wholeLevels = getTotalExpAtLevel(level)
        val toNextLevel = getExpNeededForNextLevel(level)

        return wholeLevels + Math.round(exp * toNextLevel)
    }

    /**
     * @throws PlayerNotFoundException if Player doesn't exist
     */
    fun setExp(player: OfflinePlayer, amount: Int) {
        plugin.logger.info("Setting XP for Offline Player with name ${player.name}")
        val required = getLevelsFromExp(amount)

        val tag = getCompoundTag(player)

        tag.putInt("XpLevel", required.first)
        tag.putFloat("XpP", required.second)

        NBTUtil.write(tag, getFile(player))
    }
}
