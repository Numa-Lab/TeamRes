package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.util.everyTick
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.ResTeamImpl
import net.kunmc.lab.teamres.TeamManager
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player

/**
 * @note member param in each lambda can be leader
 */
open class OnlineEveryTickSync(
    override val flyLib: FlyLib,
    private val teamManager: TeamManager,
    val start: (ResTeam, Player, Player) -> Unit = { _: ResTeam, leader: Player, member: Player -> },
    val everyTick: (ResTeam, Player, Player) -> Unit,
    val end: (ResTeam, Player, Player) -> Unit = { _: ResTeam, leader: Player, member: Player -> }
) : BaseSync() {
    init {
        register()
    }

    private fun register() {
        everyTick {
            syncedOnlinePlayers.filter { it.isOnline }.mapNotNull { map(it) }
        }.then {
            it.forEach { p ->
                if (p.second.isOnline && p.third.isOnline) {
                    everyTick(p.first, p.second.player()!!, p.third.player()!!)
                }
            }
        }
    }

    private fun map(p: SessionSafePlayer): Triple<ResTeamImpl, SessionSafePlayer, SessionSafePlayer>? {
        if (!p.isOnline) return null
        val team = teamManager.getTeam(p)
        if (team != null) {
            val leader = team.getLeader()
            if (!leader.isOnline) return null
            return Triple(team, leader, p)
        }
        return null
    }

    private val syncedOnlinePlayers = mutableSetOf<SessionSafePlayer>()

    override fun startSync(team: ResTeam, p: SessionSafePlayer) {
        val e = map(p)
        if (e != null && team == e.first) {
            start(e.first, e.second.player()!!, e.third.player()!!)
        }
//        if (p.isOnline) {
//            val t = teamManager.getTeam(p)
//            if (t != null) {
//                val leader = t.getLeader()
//                if (leader.isOnline) {
//                    syncedOnlinePlayers.add(p)
//                    start(t, leader.player()!!, p.player()!!)
//                }
//            }
//        }
    }

    override fun endSync(team: ResTeam, p: SessionSafePlayer) {
        val e = map(p)
        if (e != null && team == e.first) {
            end(e.first, e.second.player()!!, e.third.player()!!)
        }
//        if (p.isOnline) {
//            val t = teamManager.getTeam(p)
//            if (t != null) {
//                val leader = t.getLeader()
//                if (leader.isOnline) {
//                    syncedOnlinePlayers.remove(p)
//                    end(team, leader.player()!!, p.player()!!)
//                }
//            }
//        }
    }
}