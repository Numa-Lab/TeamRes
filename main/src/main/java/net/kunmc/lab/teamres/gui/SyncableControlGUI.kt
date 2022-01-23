package net.kunmc.lab.teamres.gui

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import com.flylib.flylib3.gui.inventory.ChestGUI
import com.flylib.flylib3.gui.inventory.InventoryGUIEntry
import com.flylib.flylib3.gui.inventory.InventoryPos
import com.flylib.flylib3.item.ItemData
import com.flylib.flylib3.util.info
import net.kunmc.lab.teamres.OnOff
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.Syncables
import net.kyori.adventure.text.Component
import org.bukkit.Material

class SyncableControlGUI(override val flyLib: FlyLib, val teamManager: TeamManager) : FlyLibComponent {
    val gui = ChestGUI(flyLib, Component.text("責任選択"), 6).also {
        updateGUI(it)
    }

    fun updateGUI() = updateGUI(gui)

    private fun updateGUI(gui: ChestGUI) {
        if (teamManager.teams().isEmpty()) {
            // No Team Registered
            return
        }

        (1..6).forEach { y ->
            (1..9).forEach { x ->
                updateGUI(gui[x, y], InventoryPos(x, y).index())
            }
        }
    }

    private fun updateGUI(entry: InventoryGUIEntry, index: Int) {
        val syncable = Syncables.values().getOrNull(index) ?: return
        if (teamManager.teams().isEmpty()) {
            // No Team Registered
            return
        } else {
            val team = teamManager.teams().first()
            val onOff = OnOff(team.affected().contains(syncable.lazy.get(Pair(flyLib, teamManager))))
            updateGUI(entry, syncable, onOff)
        }
    }

    private fun updateGUI(entry: InventoryGUIEntry, syncable: Syncables, onOff: OnOff) {
        val data = ItemData(
            material = if (onOff.isOn) {
                Material.GREEN_WOOL
            } else {
                Material.GRAY_WOOL
            },
            name = Component.text(syncable.displayName),
            lore = syncable.descriptions
        )

        entry.itemStack = data.build()
        entry.click = {
            onCall(syncable, onOff.toggled())
        }
    }

    private fun onCall(syncable: Syncables, toggledState: OnOff) {
        info("onCall: ToggledState:${toggledState},Syncable:${syncable}")
        teamManager.teams().forEach {
            it.effect(syncable.lazy.get(Pair(flyLib, teamManager)), toggledState)
        }
        updateGUI()
    }
}