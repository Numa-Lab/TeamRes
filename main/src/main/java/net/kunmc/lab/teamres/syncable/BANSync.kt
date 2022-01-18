package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.info
import com.flylib.flylib3.util.ready
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.ResTeamImpl
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.util.SessionSafePlayer
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
        info("[BANSync]onBan")
        val p = getWithPlayer(e.p) ?: return
        syncBanned(p.second, SessionSafePlayer(e.p), true)
    }

    private fun syncBanned(team: ResTeamImpl, bannedPlayer: SessionSafePlayer, isBanned: Boolean) {
        val name = bannedPlayer.offlinePlayer().name
        team.all().forEach {
            if (isBanned)
                it.offlinePlayer().banPlayer("${name}がBANされました")
            else
                it.unban(flyLib)
        }
    }

    @EventHandler
    fun onUnban(e: PlayerUnbannedEvent) {
        info("[BANSync]onUnban")
        val p = getWithSessionSafePlayer(SessionSafePlayer(e.p)) ?: return
        syncBanned(p.second, SessionSafePlayer(e.p), false)
    }
}