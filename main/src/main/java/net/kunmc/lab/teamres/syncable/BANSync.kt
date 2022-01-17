package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.info
import com.flylib.flylib3.util.ready
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.util.ex.PlayerBannedEvent
import net.kunmc.lab.teamres.util.ex.PlayerUnbannedEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class BANSync(override val flyLib: FlyLib, override val teamManager: TeamManager) : BaseSync(flyLib, teamManager),
    Listener {
    init {
        ready {
            flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
        }
    }

    @EventHandler
    fun onBan(e: PlayerBannedEvent) {
        info("onBan")
    }

    @EventHandler
    fun onUnban(e: PlayerUnbannedEvent) {
        // TODO Not Working yet
        info("onUnban")
    }
}