package net.kunmc.lab.teamres.util.ex

import com.destroystokyo.paper.event.block.BeaconEffectEvent
import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import com.flylib.flylib3.util.info
import com.flylib.flylib3.util.nextTick
import com.flylib.flylib3.util.ready
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.event.entity.EntityPotionEffectEvent
import org.bukkit.event.entity.EntityPotionEffectEvent.Action.*
import org.bukkit.event.entity.EntityRegainHealthEvent
import org.bukkit.event.player.PlayerCommandPreprocessEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.server.ServerCommandEvent

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

    @EventHandler
    fun onQuit(e: PlayerQuitEvent) {
        if (e.player.isBanned) {
            flyLib.event.callEvent(PlayerBannedEvent(e, e.player))
        }
    }

    @EventHandler
    fun playerPreCommand(e: PlayerCommandPreprocessEvent) {
        if (e.message.isNotEmpty()) {
            when (e.message.split(" ")[0]) {
                "/pardon", "/pardon-ip" -> {
                    // Pardon Command
                    invokeUnBanEvent(e.player as CommandSender)
                }
            }
        }
    }

    @EventHandler
    fun npPreCommand(e: ServerCommandEvent) {
        when (e.command) {
            "pardon", "pardon-ip" -> {
                // Pardon Command
                invokeUnBanEvent(e.sender as CommandSender)
            }
        }
    }

    /**
     * Collect Ban List,then wait 1 tick,Collect Ban List and compare them.
     */
    private fun invokeUnBanEvent(sender: CommandSender) {
        info("[ExEvent-Ban]Ban List Collecting...")
        val beforeBan = Bukkit.getBannedPlayers().map { SessionSafePlayer(it) }
        nextTick {
            val afterBan = Bukkit.getBannedPlayers().map { SessionSafePlayer(it) }
            val differ = beforeBan.minus(afterBan.toSet())
            info("[ExEvent-Ban]Ban List changed,differ:$differ")
            info("[ExEvent-Ban]After Ban List:$afterBan")
            info("[ExEvent-Ban]Before Ban List:$beforeBan")
            if (differ.isNotEmpty()) {
                differ.forEach { sf ->
                    flyLib.event.callEvent(PlayerUnbannedEvent(sf.offlinePlayer(), sender))
                }
            }
        }
    }


    @EventHandler
    fun onPotion(e: EntityPotionEffectEvent) {
        if (e.entity is Player) {
            val event = when (e.action) {
                ADDED -> PlayerEffectAddedEvent(
                    e.entity as Player,
                    e.oldEffect,
                    e.newEffect!!,
                    PlayerEffectEvent.Cause.convert(e.cause),
                    e
                )
                CHANGED -> PlayerEffectChangedEvent(
                    e.entity as Player,
                    e.oldEffect!!,
                    e.newEffect!!,
                    PlayerEffectEvent.Cause.convert(e.cause),
                    e
                )
                CLEARED -> PlayerEffectClearedEvent(
                    e.entity as Player,
                    e.oldEffect!!,
                    PlayerEffectEvent.Cause.convert(e.cause),
                    e
                )
                REMOVED -> PlayerEffectRemovedEvent(
                    e.entity as Player,
                    e.oldEffect!!,
                    PlayerEffectEvent.Cause.convert(e.cause),
                    e
                )
            }
            // Fire Event
            flyLib.event.callEvent(event)
        }
    }

    @EventHandler
    fun onBeacon(e: BeaconEffectEvent) {
        flyLib.event.callEvent(
            PlayerEffectAddedEvent(
                e.player,
                null,
                e.effect,
                PlayerEffectEvent.Cause.BEACON,
                e
            )
        )
    }
}