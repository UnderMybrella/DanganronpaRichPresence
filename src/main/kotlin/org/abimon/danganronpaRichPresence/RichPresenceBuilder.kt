package org.abimon.spiralRP

import club.minnced.discord.rpc.DiscordRichPresence

class RichPresenceBuilder {
    var state: String? = null
    var details: String? = null
    var startTimestamp: Long? = null
    var endTimestamp: Long? = null
    var largeImageKey: String? = null
    var largeImageText: String? = null
    var smallImageKey: String? = null
    var smallImageText: String? = null
    var partyID: String? = null
    var partySize: Int? = null
    var partyMax: Int? = null
    var matchSecret: String? = null
    var joinSecret: String? = null
    var spectateSecret: String? = null

    fun withState(state: String?): RichPresenceBuilder {
        this.state = state
        return this
    }

    fun build(): DiscordRichPresence {
        val rp = DiscordRichPresence()

        rp.state = state
        rp.details = details
        if(startTimestamp != null)
            rp.startTimestamp = startTimestamp!!
        if(endTimestamp != null)
            rp.endTimestamp = endTimestamp!!

        rp.largeImageKey = largeImageKey
        rp.largeImageText = largeImageText
        rp.smallImageKey = smallImageKey
        rp.smallImageText = smallImageText

        rp.partyId = partyID
        if(partySize != null)
            rp.partySize = partySize!!
        if(partyMax != null)
            rp.partyMax = partyMax!!

        rp.matchSecret = matchSecret
        rp.joinSecret = joinSecret
        rp.spectateSecret = spectateSecret

        return rp
    }
}

fun richPresence(init: RichPresenceBuilder.() -> Unit): DiscordRichPresence {
    val builder = RichPresenceBuilder()
    builder.init()
    return builder.build()
}