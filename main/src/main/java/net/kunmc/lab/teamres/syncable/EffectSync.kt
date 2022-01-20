package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.info
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kunmc.lab.teamres.util.SessionSafePlayer
import net.kunmc.lab.teamres.util.ex.*
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.potion.PotionEffect

class EffectSync(flyLib: FlyLib, teamManager: TeamManager) : BaseSync(flyLib, teamManager), Listener {
    init {
        flyLib.plugin.server.pluginManager.registerEvents(this, flyLib.plugin)
    }

    override fun startSync(team: ResTeam, p: SessionSafePlayer) {
        super.startSync(team, p)
        // Need To Clear All Effects from Player
        p.player()!!.let {
            it.activePotionEffects.forEach { pf ->
                it.removePotionEffect(pf.type)
            }
            assert(it.activePotionEffects.isEmpty()) { "While removing all effects, there are still effects left!" }
        }

    }

    @EventHandler
    fun onEffectAdded(e: PlayerEffectAddedEvent) {
        info("[EffectSync]onEffectAdded:${e.p},Old:${e.before},New:${e.after}")
        if (e.cause == PlayerEffectEvent.Cause.PLUGIN) {
            info("[EffectSync]onEffectAdded:Returning")
            return
        }

        if (!checkRestEffects(e.before, e.after)) {
            info("[EffectSync]onEffectChanged:Detect Downgrading Effect")
            // Downgrading Change Event is called when the player is affected by a beacon.
            // But,In that player's screen, the effect is not overwritten.
            // So just ignore this.
            return
        }


        val p = getWithPlayer(e.p) ?: return
        p.second.all()
            .filter { it != SessionSafePlayer(e.p) }
            .mapNotNull { it.player() }
            .forEach {
                it.addPotionEffect(e.after)
            }
    }

    @EventHandler
    fun onEffectCleared(e: PlayerEffectClearedEvent) {
        info("[EffectSync]onEffectCleared:${e.p},Old:${e.before},New:${e.after}")
        if (e.cause == PlayerEffectEvent.Cause.PLUGIN) {
            info("[EffectSync]onEffectCleared:Returning")
            return
        }
        val p = getWithPlayer(e.p) ?: return
        p.second.all()
            .filter { it != SessionSafePlayer(e.p) }
            .mapNotNull { it.player() }
            .forEach {
                it.removePotionEffect(e.before.type)
            }
    }

    @EventHandler
    fun onEffectChanged(e: PlayerEffectChangedEvent) {
        info("[EffectSync]onEffectChanged:${e.p},Old:${e.before},New:${e.after}")
        if (e.cause == PlayerEffectEvent.Cause.PLUGIN) {
            info("[EffectSync]onEffectChanged:Returning")
            return
        }

        if (!checkRestEffects(e.before, e.after)) {
            info("[EffectSync]onEffectChanged:Detect Downgrading Effect")
            // Downgrading Change Event is called when the player is affected by a beacon.
            // But,In that player's screen, the effect is not overwritten.
            // So just ignore this.
            return
        }

        val p = getWithPlayer(e.p) ?: return
        p.second.all()
            .filter { it != SessionSafePlayer(e.p) }
            .mapNotNull { it.player() }
            .forEach {
                it.removePotionEffect(e.before.type)
                it.addPotionEffect(e.after)
            }
    }

    @EventHandler
    fun onEffectRemoved(e: PlayerEffectRemovedEvent) {
        info("[EffectSync]onEffectRemoved:${e.p},Old:${e.before},New:${e.after}")
        if (e.cause == PlayerEffectEvent.Cause.PLUGIN) {
            info("[EffectSync]onEffectRemoved:Returning")
            return
        }

        if (e.cause == PlayerEffectEvent.Cause.BEACON) {
            info("[EffectSync]onEffectRemoved:Detect Beacon Overwriting")
            e.isCancelled = true
            return
        }

        val p = getWithPlayer(e.p) ?: return
        p.second.all()
            .filter { it != SessionSafePlayer(e.p) }
            .mapNotNull { it.player() }
            .forEach {
                it.removePotionEffect(e.before.type)
            }
    }

    /**
     * Check if [new] is stronger than [old] or if it has equal amplifier,then lasts longer one.
     * @return need to remove going one,and register new one([new]).
     */
    private fun checkRestEffects(old: PotionEffect?, new: PotionEffect): Boolean {
        if (old != null) {
            if (old.amplifier == new.amplifier) {
                return old.duration < new.duration
            } else return old.amplifier < new.amplifier
        } else {
            return true
        }
    }
}