package org.abimon.danganronpaRichPresence

import club.minnced.discord.rpc.DiscordEventHandlers
import club.minnced.discord.rpc.DiscordRPC
import org.abimon.colonelAccess.handle.MemoryAccessor
import org.abimon.spiralRP.RichPresenceBuilder
import java.io.File
import java.io.InputStreamReader
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
    if(!isDiscordRunning()) {
        if(EnumOS.ourOS in EnumOS.UNIX) {
            System.err.println("Error: No Discord instance detected. If you're positive Discord is running, you may be running this as root without environment variables.")
            System.err.println("To run this with admin permissions as your user, run \"sudo -E java -jar ${EnumOS::class.java.protectionDomain.codeSource.location.path}\"")
        } else {
            System.err.println("Error: No Discord instance detected!")
        }

        return
    }

    val discord = DiscordRPC.INSTANCE

    val DR1_CLIENT_ID = "388225825385742337"
    val DR2_CLIENT_ID = "388346252976193538"

    val DR2_ROOM_NAMES = mapOf(
            0 to "Test Room",
            1 to "Beach",
            2 to "Airport",
            3 to "Rocketpunch Mall",
            4 to "Usami Corral",
            5 to "Old Building",
            6 to "Old Building - Dining Hall",
            7 to "Old Building - Dining Hall",
            8 to "Old Building - Kitchen",

            100 to "Hotel Mirai",
            101 to "Hotel - Lobby",
            102 to "Hotel - Restaurant",
            104 to "Hajime's Cottage"
    )

    discord.Discord_Initialize(DR2_CLIENT_ID, DiscordEventHandlers(), false, null)
    discord.Discord_Register(DR2_CLIENT_ID, "http://192.168.0.24:11038/notification?name=Launching")

    val updateExec = Executors.newSingleThreadScheduledExecutor { task -> Thread(task).apply { isDaemon = true } }
    updateExec.schedule(5, TimeUnit.SECONDS) { discord.Discord_RunCallbacks() }

    val GAME_TITLES = arrayOf("Danganronpa Trigger Happy Havoc", "Danganronpa 2 Goodbye Despair")

    val (pid, danganronpa) = EnumOS.ourOS.getDRGame() ?: error("No Danganronpa game found")

    println("Danganronpa game found with PID $pid")

    val memoryAccessor = MemoryAccessor.accessorForSystem(pid)

    var prevRoom: Int? = null

    while (true) {
        val roomAddress = danganronpa.roomLocations[EnumOS.ourOS] ?: break
        val (data, readResponse) = memoryAccessor.readMemory(roomAddress, 4)

        if(data == null)
            error("Error: Read Response $readResponse; data == null. Shutting down")

        val roomID = data.getInt(0)
        if (prevRoom != roomID) {
            if(roomID == 0) {
                discord.updatePresence {
                    smallImageKey = "dr2"
                    largeImageKey = "dr2_menu"
                    state = "At the main menu"
                    partyID = UUID.randomUUID().toString()
                    partyMax = 4
                    partySize = 1

                    joinSecret = UUID.randomUUID().toString()
                    matchSecret = UUID.randomUUID().toString()
                    spectateSecret = UUID.randomUUID().toString()
                }
            } else {
                discord.updatePresence {
                    smallImageKey = "dr2"
                    largeImageKey = "dr2_menu"
                    state = "Exploring ${DR2_ROOM_NAMES[roomID] ?: "0x${roomID.toString(16)}"}"
                }
            }
            prevRoom = roomID
        }

        Thread.sleep(1000)
    }
}

/** OSX Only */
fun isDiscordRunning(): Boolean {
    val TEMP_PATH = System.getenv("XDG_RUNTIME_DIR") ?: System.getenv("TMPDIR") ?: System.getenv("TMP") ?: System.getenv("TEMP") ?: "/tmp"

    for(i in 0 until 10) {
        val socket = File("$TEMP_PATH/discord-ipc-$i")
        if(socket.exists())
            return true
    }

    return false
}

fun readProcess(cmd: String): List<String> {
    val p = Runtime.getRuntime().exec(cmd)
    p.waitFor(5, TimeUnit.SECONDS)

    return InputStreamReader(p.inputStream).readLines()
}

//fun IPCWrapper.updatePresence(pid: Int, init: RichPresenceBuilder.() -> Unit) {
//    queue.offer(IPCRequest(
//            Opcode.FRAME,
//            mapOf(
//                    "cmd" to "SET_ACTIVITY",
//                    "nonce" to UUID.randomUUID(),
//                    "args" to mapOf(
//                            "pid" to pid,
//                            "activity" to richPresence(init)
//                    )
//            )
//    ))
//}

fun DiscordRPC.updatePresence(init: RichPresenceBuilder.() -> Unit) {
    val rp = RichPresenceBuilder()
    rp.init()

    this.Discord_UpdatePresence(rp.build())
}

fun ScheduledExecutorService.schedule(every: Long, unit: TimeUnit, task: () -> Unit) = scheduleAtFixedRate(task, 0, every, unit)