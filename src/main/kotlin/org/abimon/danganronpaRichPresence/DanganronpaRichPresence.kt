package org.abimon.danganronpaRichPresence

import org.abimon.colonelAccess.handle.MemoryAccessor
import org.abimon.presence4k.IPCWrapper
import org.abimon.presence4k.RichPresenceBuilder
import org.abimon.presence4k.objects.IPCRequest
import org.abimon.presence4k.objects.Opcode
import org.abimon.presence4k.richPresence
import java.util.*


fun main(args: Array<String>) {
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

    val GAME_TITLES = arrayOf("Danganronpa Trigger Happy Havoc", "Danganronpa 2 Goodbye Despair")

    val (pid, danganronpa) = EnumOS.ourOS.getDRGame() ?: error("No Danganronpa game found")

    println("Danganronpa game found with PID $pid")

    //val discord = IPCWrapper("/var/folders/rn/m4lrzjyd6h39t5xgp2n02h4w0000gp/T/discord-ipc-0", DR2_CLIENT_ID)
//    val mac = SystemB.INSTANCE
//
//    val task = IntByReference()
//    val taskResponse = KernReturn.valueOf(mac.task_for_pid(mac.mach_task_self(), DANGAN_PID!!, task))!!
//
//    if (taskResponse != KernReturn.KERN_SUCCESS)
//        error("Error: Task Response $taskResponse ≠ KERN_SUCCESS; run with sudo maybe?")

    val memoryAccessor = MemoryAccessor.accessorForSystem(pid)

    var prevRoom: Int? = null

    while (true) {
        //val data = PointerByReference()
        //val size = IntByReference()

        //val readResponse = KernReturn.valueOf(mac.vm_read(task.value, DR2_ROOM_ADDRESS, 4, data, size))

        //if (readResponse != KernReturn.KERN_SUCCESS)
        //    error("Error: Read Response $readResponse ≠ KERN_SUCCESS; shutting down")

        val roomAddress = danganronpa.roomLocations[EnumOS.ourOS] ?: break
        val (data, readResponse) = memoryAccessor.readMemory(roomAddress, 4)

        if(data == null)
            error("Error: Read Response $readResponse; data == null. Shutting down")

        val roomID = data.getInt(0)
        println("[0x${roomAddress.toString(16)}] Room ID: $roomID")
        if (prevRoom != roomID) {
//            discord.updatePresence(DANGAN_PID!!) {
//                smallImageKey = "dr2"
//                largeImageKey = "dr2_menu"
//                state = "Exploring ${DR2_ROOM_NAMES[roomID] ?: "0x${roomID.toString(16)}"}"
//            }
            println("Exploring ${DR2_ROOM_NAMES[roomID] ?: "0x${roomID.toString(16)}"}")

            prevRoom = roomID
        }

        Thread.sleep(1000)
    }
}

fun IPCWrapper.updatePresence(pid: Int, init: RichPresenceBuilder.() -> Unit) {
    queue.offer(IPCRequest(
            Opcode.FRAME,
            mapOf(
                    "cmd" to "SET_ACTIVITY",
                    "nonce" to UUID.randomUUID(),
                    "args" to mapOf(
                            "pid" to pid,
                            "activity" to richPresence(init)
                    )
            )
    ))
}