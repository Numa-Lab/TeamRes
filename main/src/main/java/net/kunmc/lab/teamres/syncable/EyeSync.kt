package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kunmc.lab.teamres.util.SessionSafePlayer
import net.kunmc.lab.teamres.util.ex.PlayerEyeMovingEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class EyeSync(flyLib: FlyLib, teamManager: TeamManager) : BaseSync(flyLib, teamManager), Listener {
    init {
        flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
    }

    @EventHandler
    fun onEyeMove(e: PlayerEyeMovingEvent) {
        val p = getWithPlayer(e.e.player) ?: return
        p.second.all()
            .filter { it != SessionSafePlayer(e.e.player) }
            .mapNotNull { it.player() }
            .forEach {
                val loc = it.location.clone()
                loc.yaw = loc.yaw + e.deltaYaw
                loc.pitch = loc.pitch + e.deltaPitch
                it.teleport(loc)
            }
    }
}