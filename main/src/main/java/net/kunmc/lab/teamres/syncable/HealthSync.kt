package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.TeamManager
import org.bukkit.entity.Player

class HealthSync(flyLib: FlyLib, teamManager: TeamManager) :
    OnlineEveryTickSync(
        flyLib, teamManager,
        everyTick = { resTeam: ResTeam, leader: Player, member: Player ->
            member.health = leader.health
        },
        start = { resTeam: ResTeam, player: Player, player1: Player -> player1.sendMessage("Health Started with leader:${player}") }
    ) {
}