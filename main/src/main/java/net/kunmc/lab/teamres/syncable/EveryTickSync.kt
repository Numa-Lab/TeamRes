package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.everyTick
import com.flylib.flylib3.util.ready
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.ResTeamImpl
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.OfflinePlayer

open class EveryTickSync(
    override val flyLib: FlyLib,
    open val teamManager: TeamManager,
    val start: (ResTeam, OfflinePlayer) -> Unit = { _: ResTeam, _: OfflinePlayer -> },
    val everyTick: (ResTeam, OfflinePlayer) -> Unit,
    val end: (ResTeam, OfflinePlayer) -> Unit = { _: ResTeam, _: OfflinePlayer -> }
) :
    BaseSync() {
    init {
        ready {
            register()
        }
    }

    open fun register() {
        everyTick {
            syncedPlayers.mapNotNull { map(it) }
        }.then {
            it.forEach { p ->
                everyTick(p.first, p.second)
            }
        }
    }

    private fun map(p: OfflinePlayer): Pair<ResTeamImpl, OfflinePlayer>? {
        val team = teamManager.getTeam(SessionSafePlayer(p))
        if (team != null) {
            return Pair(team, p)
        }
        return null
    }

    open val syncedPlayers = mutableSetOf<OfflinePlayer>()
    override fun startSync(team: ResTeam, p: SessionSafePlayer) {
        syncedPlayers.add(p.offlinePlayer())
        start(team, p.offlinePlayer())
    }

    override fun endSync(team: ResTeam, p: SessionSafePlayer) {
        syncedPlayers.remove(p.offlinePlayer())
        end(team, p.offlinePlayer())
    }
}