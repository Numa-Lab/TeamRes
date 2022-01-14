package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.TeamManager
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import kotlin.math.max
import kotlin.math.min

class HealthSync(flyLib: FlyLib, teamManager: TeamManager) :
    OnlineEveryTickSync(
        flyLib, teamManager,
        everyTick = { resTeam: ResTeam, leader: Player, member: Player ->
            if (!leader.isDead) {
                val health = max(min(member.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value, leader.health), 0.0)
                member.health = health
            }
        },
        start = { resTeam: ResTeam, leader: Player, member: Player -> member.sendMessage("Health Started with leader:${leader}") }
    ) {
        // TODO 一回OffにしてからOnにしても適用されない
}