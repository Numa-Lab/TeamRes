package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLib
import net.kunmc.lab.teamres.TeamManager

class BANSync(override val flyLib: FlyLib, override val teamManager: TeamManager) : BaseSync(flyLib, teamManager) {
}