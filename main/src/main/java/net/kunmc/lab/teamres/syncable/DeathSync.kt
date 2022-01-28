package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.ready
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kunmc.lab.teamres.util.SessionSafePlayer
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class DeathSync(flyLib: FlyLib, teamManager: TeamManager) : BaseSync(flyLib, teamManager), Listener {
    init {
        ready {
            flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
        }
    }

    private val killedByTeam = mutableMapOf<SessionSafePlayer, String>()

    @EventHandler
    fun onDeath(e: PlayerDeathEvent) {
        if (killedByTeam.contains(SessionSafePlayer(e.entity))) {
            // This Player is killed by Team
            e.deathMessage(
                Component.text("${e.entity.name}は${killedByTeam[SessionSafePlayer(e.entity)]!!}")
                    .append(Component.text("が死んだので連帯責任を負った"))
            )
            killedByTeam.remove(SessionSafePlayer(e.entity))
        } else {
            val p = getWithPlayer(e.entity) ?: return
            p.second.all()
                .filter { it != SessionSafePlayer(e.entity) }
                .mapNotNull { it.player() }
                .forEach {
                    killedByTeam[SessionSafePlayer(it)] = e.entity.name
                    it.health = 0.0
                }
        }
    }
}