package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.nextTick
import com.flylib.flylib3.util.warn
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityPickupItemEvent
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.event.inventory.InventoryCreativeEvent
import org.bukkit.event.inventory.InventoryDragEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.PlayerInventory

class InventorySync(flyLib: FlyLib, teamManager: TeamManager) : BaseSync(flyLib, teamManager), Listener {
    init {
        flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
    }

    @EventHandler
    fun onCreative(e: InventoryCreativeEvent) {
        if (e.inventory is PlayerInventory && (e.whoClicked as Player).inventory == e.inventory) {
            syncInventory(e.whoClicked as Player, e.inventory as PlayerInventory)
        }
    }

    @EventHandler
    fun onDropItem(e: PlayerDropItemEvent) {
        syncInventory(e.player, e.player.inventory)
    }

    @EventHandler
    fun onPickUp(e: EntityPickupItemEvent) {
        if (e.entity is Player) {
            syncInventory(e.entity as Player, (e.entity as Player).inventory)
        }
    }

    @EventHandler
    fun onDragItem(e: InventoryDragEvent) {
        syncInventory(e.whoClicked as Player, e.whoClicked.inventory)
    }

    @EventHandler
    fun onInventoryClick(e: InventoryClickEvent) {
        if (e.clickedInventory == null) {
            return // Player Clicked OutSide
        }
        // Player Changed its inventory
        syncInventory(e.whoClicked as Player, e.whoClicked.inventory)
    }

    @EventHandler
    fun onItemConsumed(e: PlayerItemConsumeEvent) {
        syncInventory(e.player, e.player.inventory)
    }

    @EventHandler
    fun onOffHand(e:PlayerSwapHandItemsEvent){
        syncInventory(e.player,e.player.inventory)
    }

    /**
     * Sync Whole Inventory
     */
    private fun syncInventory(e: Player, inventory: PlayerInventory) {
        val p = getWithPlayer(e) ?: return
        nextTick {
            p.second.all()
                .filter { it != SessionSafePlayer(e) }
                .mapNotNull { it.player() }
                .forEach {
                    syncInventory(inventory, it.inventory)
                }
        }
    }

    private fun syncInventory(from: PlayerInventory, to: PlayerInventory) {
        to.contents = from.contents
    }

    ///// ここまで実際の実装
    ///// ここから途中抜け・参加対策用

    @EventHandler
    fun onPlayerJoined(e: PlayerJoinEvent) {
        val p = getWithPlayer(e.player) ?: return
        val first = p.second.all().filter { it != SessionSafePlayer(e.player) }.firstNotNullOfOrNull { it.player() }
        if (first == null) {
            // ほかにチームメンバーがいないのでヨシ！
            return
        } else {
            syncInventory(first, first.inventory)
        }
    }
}