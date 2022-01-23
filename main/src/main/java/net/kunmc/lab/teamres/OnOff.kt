package net.kunmc.lab.teamres

/**
 * Class express on or off
 */
class OnOff(val isOn: Boolean) {
    override fun toString() = if (isOn) "on" else "off"
    override fun equals(other: Any?): Boolean {
        if (other is OnOff) {
            return isOn == other.isOn
        }
        return false
    }

    override fun hashCode(): Int {
        return isOn.hashCode()
    }

    fun toggled() = OnOff(!isOn)

    companion object {
        fun getByString(str: String): OnOff? {
            return when (str) {
                "on" -> OnOff(true)
                "off" -> OnOff(false)
                else -> null
            }
        }

        val ON = OnOff(true)
        val OFF = OnOff(false)
    }
}