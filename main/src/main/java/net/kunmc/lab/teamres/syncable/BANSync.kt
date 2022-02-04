package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.info
import com.flylib.flylib3.util.ready
import com.flylib.flylib3.util.warn
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.ResTeamImpl
import net.kunmc.lab.teamres.ResTeamImpl2
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.syncable.base.BaseSync
import net.kunmc.lab.teamres.util.SessionSafePlayer
import net.kunmc.lab.teamres.util.ex.PlayerBannedEvent
import net.kunmc.lab.teamres.util.ex.PlayerUnbannedEvent
import net.kyori.adventure.text.Component
import org.bukkit.BanList
import org.bukkit.Bukkit
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
        info("[BANSync]onBan")
        val p = getWithPlayer(e.p) ?: return
        syncBanned(p.second, SessionSafePlayer(e.p), true)
    }

    // TODO リログしようとしたときの表示が怪しい
    private fun syncBanned(team: ResTeamImpl2, bannedPlayer: SessionSafePlayer, isBanned: Boolean) {
        val name = bannedPlayer.offlinePlayer().name
        warn("[BANSync] ${bannedPlayer},${name} is now $isBanned")
        if (name == null) {
            warn("[BANSync] Failed to get Banned/Pardoned Player name,it may cause double banning/pardoning")
        }
        team.getTeam().entries
            .filter { it != name }
            .forEach {
                if (isBanned) {
                    // Ban the rest players
                    Bukkit.getBanList(BanList.Type.NAME).addBan(it, "${name}がBANされました", null, null)
                    Bukkit.getOnlinePlayers().firstOrNull { t -> t.name == it }
                        ?.kick(Component.text("${name}がBANされました"))
                } else {
                    // UnBan the rest players
                    Bukkit.getBanList(BanList.Type.NAME).pardon(it)
                }
            }
    }

    @EventHandler
    fun onUnban(e: PlayerUnbannedEvent) {
        val name = e.p.name
        if (name == null) {
            warn("[BANSync] Failed to get the name of player unbanned")
            return
        }
        val p = getWitPlayerName(name) ?: return
        syncBanned(p, SessionSafePlayer(e.p), false)
    }

    private fun getWitPlayerName(name: String): ResTeamImpl2? {
        return this.teamManager.teams().firstOrNull { it.getTeam().entries.contains(name) }
    }
}