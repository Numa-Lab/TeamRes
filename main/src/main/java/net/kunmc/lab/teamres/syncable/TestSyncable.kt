package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import com.flylib.flylib3.FlyLibComponent
import com.flylib.flylib3.task.FRunnableContext
import com.flylib.flylib3.util.event
import com.flylib.flylib3.util.filter
import com.flylib.flylib3.util.info
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.util.SessionSafePlayer
import org.bukkit.OfflinePlayer
import org.bukkit.entity.Player
import org.bukkit.event.player.PlayerMoveEvent

class TestSyncable(override val flyLib: FlyLib) : Syncable, FlyLibComponent {
    override fun startSync(team: ResTeam) {
        info("startSync#Team")
        team.all().forEach {
            startSync(team, it)
        }
    }

    override fun startSync(team: ResTeam, p: SessionSafePlayer) {
        info("startSync#Player")
        event<PlayerMoveEvent, PlayerMoveEvent> {
            it
        }.filter { e: PlayerMoveEvent, _: FRunnableContext -> SessionSafePlayer(e.player) == p }
            .then { it.player.sendMessage("You are in team:${team},and moved") }
    }

    override fun endSync(team: ResTeam) {
        info("endSync#Team")
        team.all().forEach {
            endSync(team, it)
        }
    }

    override fun endSync(team: ResTeam, p: SessionSafePlayer) {
        info("endSync#Player")
        if (p.isOnline) {
            p.player()!!.sendMessage("You are now end")
        }
    }
}