package net.kunmc.lab.teamres.gui

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import com.flylib.flylib3.gui.inventory.ChestGUI
import com.flylib.flylib3.gui.inventory.InventoryGUIEntry
import com.flylib.flylib3.gui.inventory.InventoryPos
import com.flylib.flylib3.item.ItemData
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.util.SessionSafePlayer
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta

class TeamControlGUI(override val flyLib: FlyLib, val teamManager: TeamManager) : FlyLibComponent {
    val gui = ChestGUI(flyLib, Component.text("チーム選択"), 6).also {
        assert(it.inventory.size == 54)
        updateGUI(it)
    }

    fun updateGUI() = updateGUI(gui)
    private fun updateGUI(gui: ChestGUI) {
        (1..6).forEach { y ->
            (1..9).forEach { x ->
                updateGUI(gui[x, y], InventoryPos(x, y).index())
            }
        }
    }

    private fun updateGUI(entry: InventoryGUIEntry, index: Int) {
        val team = teamManager.teams().getOrNull(index)
        if (team == null) {
            // No Team matched
            // Set to empty
            entry.itemStack = ItemData(Material.AIR).build()
            return
        }

        updateGUI(entry, getTeamItemStack(team), team)
    }

    private fun updateGUI(entry: InventoryGUIEntry, itemStack: ItemStack, team: ResTeam) {
        entry.itemStack = itemStack
        entry.click = {
            onClick(it.whoClicked as Player, team)
        }
    }

    private fun onClick(p: Player, resTeam: ResTeam) {
        if (resTeam.all().contains(SessionSafePlayer(p))) {
            // This player is already in this team.
            resTeam.remove(SessionSafePlayer(p))
            val comp = resTeam.name().append(Component.text("から抜けました"))
            p.sendMessage(comp)
        } else {
            val team = teamManager.getTeam(SessionSafePlayer(p))
            if (team != null) {
                // This player is already in another team.
                team.remove(SessionSafePlayer(p))
                resTeam.add(SessionSafePlayer(p))
                val comp =
                    team.name().append(Component.text("から")).append(resTeam.name()).append(Component.text("へ移動しました"))
                p.sendMessage(comp)
            } else {
                // This player is not in another team.
                resTeam.add(SessionSafePlayer(p))
                val comp = resTeam.name().append(Component.text("に参加しました"))
                p.sendMessage(comp)
            }
        }
        updateGUI()
    }

    private fun getTeamItemStack(team: ResTeam): ItemStack {
        if (team.all().isEmpty()) {
            // Team Member is not found.
            val data = ItemData(
                material = Material.BARRIER,
                name = team.name(),
                lore = listOf(Component.text("このチームには誰もいません"))
            )
            return data.build()
        } else {
            val data = ItemData(
                material = Material.PLAYER_HEAD,
                name = team.name(),
                lore = generateLore(team)
            )
            val stack = data.build()
            stack.editMeta {
                val skull = it as SkullMeta
                skull.owningPlayer = team.all().first().offlinePlayer()
            }
            return stack
        }
    }

    private fun generateLore(team: ResTeam): List<TextComponent> {
        return team.all()
            .mapNotNull { it.player() }
            .map { Component.text(it.name) }
    }
}