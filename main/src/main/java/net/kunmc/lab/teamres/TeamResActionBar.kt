package net.kunmc.lab.teamres

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import com.flylib.flylib3.util.everyTick
import com.flylib.flylib3.util.ready
import net.kunmc.lab.teamres.syncable.Syncables
import net.kyori.adventure.text.Component

class TeamResActionBar(val teamManager: TeamManager, override val flyLib: FlyLib) : FlyLibComponent {
    init {
        ready {
            everyTick {
                everyTick()
            }
        }
    }

    @Suppress("SpellCheckingInspection")
    private fun everyTick() {
        val activeSyncable = teamManager.activeSyncable.toMutableList()
        if (activeSyncable.isEmpty()) return
        val activeSyncables =
            Syncables.values().filter { activeSyncable.contains(it.lazy.get(Pair(flyLib, teamManager))) }
        val activeSyncablesText = "[" + activeSyncables.joinToString { it.displayName } + "]"
        val activeSyncablesComponent = Component.text(activeSyncablesText)

        teamManager.teams().map { it.all() }.flatten().mapNotNull { it.player() }.forEach {
            it.sendActionBar(activeSyncablesComponent)
        }
    }
}