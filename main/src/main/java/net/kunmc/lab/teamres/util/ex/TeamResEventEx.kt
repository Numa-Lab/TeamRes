package net.kunmc.lab.teamres.util.ex

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import com.flylib.flylib3.util.info
import com.flylib.flylib3.util.ready
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityRegainHealthEvent

class TeamResEventEx(override val flyLib: FlyLib) : FlyLibComponent, Listener {
    init {
        ready {
            register()
        }
    }

    private fun register() {
        flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
    }

    @EventHandler
    fun onRegain(e: EntityRegainHealthEvent) {
        if (e.entity is Player) {
            flyLib.event.callEvent(PlayerHealEvent(e.entity as Player, e.amount, e))
        }
    }

    @EventHandler
    fun onDamage(e: EntityDamageEvent) {
        if (e.entity is Player) {
            flyLib.event.callEvent(PlayerDamageEvent(e.entity as Player, e.damage, e))
        }
    }
}