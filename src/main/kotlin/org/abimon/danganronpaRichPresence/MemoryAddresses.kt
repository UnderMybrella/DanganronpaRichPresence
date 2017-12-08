package org.abimon.danganronpaRichPresence

object MemoryAddresses {
    val DR1_ROOM_LOCATION: Map<EnumOS, Long> = mapOf()

    val DR2_ROOM_LOCATION: Map<EnumOS, Long> = mapOf(
            EnumOS.WINDOWS to 0x3A6580L,
            EnumOS.MAC to 0x10041CFF4
    )

    val DR2_CHAPTER: Map<EnumOS, Long> = mapOf(
            EnumOS.MAC to 0x1004C3650
    )

    val DR2_OPTION: Map<EnumOS, Long> = mapOf(
            EnumOS.MAC to 0x1020FD72C
    )
}