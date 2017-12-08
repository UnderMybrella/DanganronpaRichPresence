package org.abimon.danganronpaRichPresence

enum class EnumGame(val processNames: Array<String>, val roomLocations: Map<EnumOS, Long>) {
    DR1(arrayOf("Danganronpa Trigger Happy Havoc", "DR_us.exe"), MemoryAddresses.DR1_ROOM_LOCATION),
    DR2(arrayOf("Danganronpa 2 Goodbye Despair", "DR2_us.exe"), MemoryAddresses.DR2_ROOM_LOCATION)
}