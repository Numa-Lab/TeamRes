package net.kunmc.lab.teamres.syncable

import com.flylib.flylib3.FlyLibComponent
import net.kunmc.lab.teamres.ResTeam

/**
 * Base syncable class
 */
abstract class BaseSync : Syncable, FlyLibComponent {
    override fun startSync(team: ResTeam) {
        team.all().forEach {
            startSync(team, it)
        }
    }

    override fun endSync(team: ResTeam) {
        team.all().forEach {
            endSync(team, it)
        }
    }
}