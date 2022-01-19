package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.info
import com.flylib.flylib3.util.nextTick
import com.flylib.flylib3.util.ready
import net.kunmc.lab.teamres.ResTeamImpl
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kunmc.lab.teamres.util.SessionSafePlayer
import net.kunmc.lab.teamres.util.ex.PlayerDamageEvent
import net.kunmc.lab.teamres.util.ex.PlayerHealEvent
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import kotlin.math.min

class HealthSync(flyLib: FlyLib, teamManager: TeamManager) : BaseSync(flyLib, teamManager), Listener {
    init {
        ready {
            flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
        }
    }

    @EventHandler
    fun onHeal(e: PlayerHealEvent) {
        info("[HealthSync]onHeal")
        val pair = getWithPlayer(e.p) ?: return
        // Player is in syncedPlayer and teamManager
        syncHealth(pair)
    }

    @EventHandler
    fun onDamage(e: PlayerDamageEvent) {
        info("[HealthSync]onDamage")
        val pair = getWithPlayer(e.p) ?: return
        // Player is in syncedPlayer and teamManager
        syncHealth(pair)
    }

    private fun syncHealth(pair: Pair<Player, ResTeamImpl>) {
        /**
         * Potion damage is reflected next tick
         */
        nextTick {
            pair.second.all()
                .filter { it != SessionSafePlayer(pair.first) && it.isOnline }
                .map { it.player()!! }
                .forEach {
                    info("Syncing from ${pair.first.name} to ${it.name}")
                    it.health = min(it.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value, pair.first.health)
                }
        }
    }
}