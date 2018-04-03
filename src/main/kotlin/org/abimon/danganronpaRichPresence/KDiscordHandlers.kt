package org.abimon.spiralRP

import club.minnced.discord.rpc.DiscordEventHandlers
import club.minnced.discord.rpc.DiscordJoinRequest

var DiscordEventHandlers.readyHandler: () -> Unit
    get() = { ready.accept() }
    set(func) { ready = DiscordEventHandlers.OnReady(func) }
var DiscordEventHandlers.disconnectedHandler: (Int, String) -> Unit
    get() = { errorCode, msg -> disconnected.accept(errorCode, msg) }
    set(func) { disconnected = DiscordEventHandlers.OnStatus(func) }
var DiscordEventHandlers.erroredHandler: (Int, String) -> Unit
    get() = { errorCode, msg -> errored.accept(errorCode, msg) }
    set(func) { errored = DiscordEventHandlers.OnStatus(func) }
var DiscordEventHandlers.joinGameHandler: (String) -> Unit
    get() = { secret -> joinGame.accept(secret) }
    set(func) { joinGame = DiscordEventHandlers.OnGameUpdate(func) }
var DiscordEventHandlers.spectateGameHandler: (String) -> Unit
    get() = { secret -> spectateGame.accept(secret) }
    set(func) { spectateGame = DiscordEventHandlers.OnGameUpdate(func) }
var DiscordEventHandlers.joinRequestHandler: (DiscordJoinRequest) -> Unit
    get() = { request -> joinRequest.accept(request) }
    set(func) { joinRequest = DiscordEventHandlers.OnJoinRequest(func) }