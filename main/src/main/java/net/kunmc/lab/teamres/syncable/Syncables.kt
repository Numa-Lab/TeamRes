@file:Suppress("SpellCheckingInspection")

package net.kunmc.lab.teamres.syncable

/**
 * Enum of all syncable types.
 */
enum class Syncables(val displayName: String, val syncable: Syncable) {
    Test("Test", TestSyncable());

    companion object {
        fun getByString(name: String): Syncables? {
            return values().find { it.displayName == name }
        }
    }
}