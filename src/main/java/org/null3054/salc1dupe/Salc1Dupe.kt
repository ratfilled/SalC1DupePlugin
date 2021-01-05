package org.null3054.salc1dupe

import org.null3054.salc1dupe.commands.ReloadCommand
import org.bukkit.plugin.java.JavaPlugin
import org.null3054.salc1dupe.events.DupeListener

class SalC1Dupe : JavaPlugin() {
    companion object {
        var INSTANCE: SalC1Dupe? = null
    }

    override fun onEnable() {
        INSTANCE = this

        getCommand("dupereload")?.setExecutor(ReloadCommand(this))

        server.pluginManager.registerEvents(DupeListener(this), this)

        super.onEnable()
    }
}