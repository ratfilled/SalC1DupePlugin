package org.null3054.salc1dupe.events

import org.bukkit.ChatColor
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Material
import org.bukkit.entity.Llama
import org.bukkit.entity.Donkey
import org.bukkit.entity.Entity
import org.bukkit.event.EventHandler
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractAtEntityEvent
import org.null3054.salc1dupe.SalC1Dupe
import java.util.ArrayList

class DupeListener(private val plugin: JavaPlugin): Listener {
    private val arrayList: ArrayList<Entity> = ArrayList()

    @EventHandler
    fun onChest(event: PlayerInteractAtEntityEvent) {
        if (!SalC1Dupe.INSTANCE!!.config.getBoolean("modules.donkey-dupe.enabled")) return
        if (arrayList.contains(event.rightClicked)) return
        val player = event.player
        val entity: Entity = event.rightClicked

        if (entity is Llama || entity is Donkey) {
            if (!(player.inventory.itemInMainHand.type == Material.CHEST || player.inventory.itemInOffHand.type == Material.CHEST)) return
            player.sendMessage(
                ChatColor.translateAlternateColorCodes(
                    '&',
                    SalC1Dupe.INSTANCE!!.config.getString("modules.donkey-dupe.messages.dupe-disabled")!!
                )
            )
            if (entity is Llama) {
                for (item in entity.inventory.contents) if (item != null) entity.world.dropItemNaturally(
                    entity.location,
                    item
                )
                if (entity.isCarryingChest) entity.isCarryingChest = false
            }
            if (entity is Donkey) {
                for (item in entity.inventory.contents) if (item != null) entity.world.dropItemNaturally(
                    entity.location,
                    item
                )
                if (entity.isCarryingChest) entity.isCarryingChest = false
            }
            arrayList.add(entity)
            event.isCancelled = true
        }
    }

    @EventHandler
    fun handleOnChestEvent(event: PlayerInteractEntityEvent) {
        if (!arrayList.contains(event.rightClicked)) return
        arrayList.remove(event.rightClicked)
        val entity: Entity = event.rightClicked
        if (entity is Donkey) entity.isCarryingChest = false
        if (entity is Llama) entity.isCarryingChest = false
        val player = event.player
        val item =
            if (player.inventory.itemInMainHand.type == Material.CHEST) player.inventory.itemInMainHand else player.inventory.itemInOffHand
        if (item.amount == 1) player.inventory.remove(item) else item.amount = item.amount - 1
        event.isCancelled = true
    }
}
