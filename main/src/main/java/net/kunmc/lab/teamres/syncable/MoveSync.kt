package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class MoveSync(flyLib: FlyLib, teamManager: TeamManager) : BaseSync(flyLib, teamManager), Listener {
    init {
        flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
    }

    @EventHandler
    fun onMove(e: PlayerMoveEvent) {
        if (e.hasChangedPosition()) {
            val p = getWithPlayer(e.player) ?: return
            p.second.all()
                .filter { it != SessionSafePlayer(e.player) }
                .mapNotNull { it.player() }
                .forEach {
                    val loc = it.location.clone()
                    loc.x = loc.x + (e.to.x - e.from.x)
                    loc.z = loc.z + (e.to.z - e.from.z)
                    it.teleport(loc)
                }
        }
    }
}