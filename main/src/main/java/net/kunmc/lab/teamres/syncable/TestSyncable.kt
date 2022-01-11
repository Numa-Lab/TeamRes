package net.kunmc.lab.teamres.syncable

import net.kunmc.lab.teamres.ResTeam
import org.bukkit.OfflinePlayer

class TestSyncable() : Syncable {
    override fun startSync(team: ResTeam) {
        println("startSync#Team")
    }

    override fun startSync(team: ResTeam, p: OfflinePlayer) {
        println("startSync#Player")
    }

    override fun endSync(team: ResTeam, p: OfflinePlayer) {
        println("endSync#Player")
    }

    override fun endSync(team: ResTeam) {
        println("endSync#Team")
    }
}