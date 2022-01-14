package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import net.kunmc.lab.teamres.ResTeam
import net.kunmc.lab.teamres.util.SessionSafePlayer

class BANSync(override val flyLib: FlyLib) : BaseSync() {
    override fun startSync(team: ResTeam, p: SessionSafePlayer) {
    }

    override fun endSync(team: ResTeam, p: SessionSafePlayer) {
    }
}