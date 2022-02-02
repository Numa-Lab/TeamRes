package net.kunmc.lab.teamres.syncable

import com.destroystokyo.paper.event.player.PlayerJumpEvent
import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.info
import com.flylib.flylib3.util.warn
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.util.Vector

class JumpSync(flyLib: FlyLib, teamManager: TeamManager) : BaseSync(flyLib, teamManager), Listener {
    init {
        flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
    }

    @EventHandler
    fun onJump(e: PlayerJumpEvent) {
        val p = getWithPlayer(e.player) ?: return
        p.second.all()
            .filter { it.isOnline && it != SessionSafePlayer(e.player) }
            .mapNotNull { it.player() }
            .filter { playerIsOnGround(it) }
            .forEach {
                it.isJumping = true
                it.velocity = it.velocity.setY(0.5)
            }
    }

    private fun playerIsOnGround(player: Player): Boolean {
        return !player.location.clone().block.location.clone().subtract(0.0, 0.01, 0.0).block.type.isAir
    }
}