@file:Suppress("unused")

package com.jessecorbett.diskord.util.experimental

import com.jessecorbett.diskord.DiscordWebSocket
import com.jessecorbett.diskord.EventListener
import com.jessecorbett.diskord.api.client.ChannelClient
import com.jessecorbett.diskord.api.model.Message
import com.jessecorbett.diskord.api.websocket.events.DiscordEvent
import com.jessecorbett.diskord.event.*
import com.jessecorbett.diskord.util.ClientStore
import com.jessecorbett.diskord.util.authorId
import org.slf4j.LoggerFactory

class Bot constructor(val token: String) : EventListener() {
    companion object {
        private val LOG = LoggerFactory.getLogger(Bot::class.java)
    }

    private val clientStore = ClientStore(token)
    private val webSocket = DiscordWebSocket(token, this)

    val commandGroups = mutableListOf<CommandGroup>()
    val eventHandlers = mutableMapOf<DiscordEvent, List<EventHandler<EventData>>>()
    val responses = ArrayList<String>()

    override suspend fun onEvent(event: DiscordEvent, data: EventData) {
        LOG.debug("Incoming Event ({}) --> {}", event, data)
        LOG.trace("eventHandlers = {}", eventHandlers)

        eventHandlers[DiscordEvent.ANY]?.forEach { handler ->
            handler(event, data, clientStore)
        }
        eventHandlers[event]?.forEach { handler ->
            handler(event, data, clientStore)
        }
    }

    override suspend fun onMessageCreate(message: Message) {
        commandGroups.forEach { group ->
            if (message.content.startsWith(group.prefix)) {
                val tokens = message.content.split(" ")
                for (command in group.commands) {
                    if (command.key == tokens[0].drop(group.prefix.length)) {
                        command.value(tokens.drop(1), message.authorId, clientStore.channels[message.channelId], clientStore)
                        return
                    }
                }
            }
        }
    }
}

typealias Command = suspend (List<String>, String, ChannelClient, ClientStore) -> Unit
data class CommandGroup(val prefix: CharSequence, val commands: MutableMap<String, Command> = mutableMapOf())

typealias EventHandler<T> = suspend (DiscordEvent, T, ClientStore) -> Unit
data class EventHandlerGroup(val handlers: MutableMap<DiscordEvent, MutableList<EventHandler<EventData>>> = mutableMapOf())

fun bot(token: String, block: Bot.() -> Unit) = Bot(token).apply(block)

fun Bot.commands(prefix: Char = '!', block: CommandGroup.() -> Unit) = commands(prefix.toString(), block)

fun Bot.commands(prefix: CharSequence = "!", block: CommandGroup.() -> Unit) {
    val group = CommandGroup(prefix)
    group.apply(block)
    commandGroups += group
}

fun CommandGroup.command(command: String, action: Command) {
    commands[command] = action
}

fun Bot.events(block: EventHandlerGroup.() -> Unit) {
    val group = EventHandlerGroup()
    group.apply(block)
    eventHandlers += group.handlers
}

private fun <T: EventData> EventHandlerGroup.event(event: DiscordEvent, action: EventHandler<T>) {
    @Suppress("UNCHECKED_CAST")
    handlers.computeIfAbsent(event) { mutableListOf() }.add(action as EventHandler<EventData>)
}

fun EventHandlerGroup.on(vararg event: DiscordEvent, action: EventHandler<EventData>) = event.forEach { event(it, action) }
fun EventHandlerGroup.any(action: EventHandler<EventData>) = event(DiscordEvent.ANY, action)
fun EventHandlerGroup.ready(action: EventHandler<ReadyData>) = event(DiscordEvent.READY, action)
fun EventHandlerGroup.resumed(action: EventHandler<ResumedData>) = event(DiscordEvent.RESUMED, action)
fun EventHandlerGroup.channelCreate(action: EventHandler<ChannelCreateData>) = event(DiscordEvent.CHANNEL_CREATE, action)
fun EventHandlerGroup.channelUpdate(action: EventHandler<ChannelUpdateData>) = event(DiscordEvent.CHANNEL_UPDATE, action)
fun EventHandlerGroup.channelDelete(action: EventHandler<ChannelDeleteData>) = event(DiscordEvent.CHANNEL_DELETE, action)
fun EventHandlerGroup.channelPinsUpdate(action: EventHandler<ChannelPinsUpdateData>) = event(DiscordEvent.CHANNEL_PINS_UPDATE, action)
fun EventHandlerGroup.guildCreate(action: EventHandler<GuildCreateData>) = event(DiscordEvent.GUILD_CREATE, action)
fun EventHandlerGroup.guildUpdate(action: EventHandler<GuildUpdateData>) = event(DiscordEvent.GUILD_UPDATE, action)
fun EventHandlerGroup.guildDelete(action: EventHandler<GuildDeleteData>) = event(DiscordEvent.GUILD_DELETE, action)
fun EventHandlerGroup.guildBanAdd(action: EventHandler<GuildBanAddData>) = event(DiscordEvent.GUILD_BAN_ADD, action)
fun EventHandlerGroup.guildBanRemove(action: EventHandler<GuildBanRemoveData>) = event(DiscordEvent.GUILD_BAN_REMOVE, action)
fun EventHandlerGroup.guildEmojisUpdate(action: EventHandler<GuildEmojisUpdateData>) = event(DiscordEvent.GUILD_EMOJIS_UPDATE, action)
fun EventHandlerGroup.guildIntegrationsUpdate(action: EventHandler<GuildIntegrationsUpdateData>) = event(DiscordEvent.GUILD_INTEGRATIONS_UPDATE, action)
fun EventHandlerGroup.guildMemberAdd(action: EventHandler<GuildMemberAddData>) = event(DiscordEvent.GUILD_MEMBER_ADD, action)
fun EventHandlerGroup.guildMemberUpdate(action: EventHandler<GuildMemberUpdateData>) = event(DiscordEvent.GUILD_MEMBER_UPDATE, action)
fun EventHandlerGroup.guildMemberDelete(action: EventHandler<GuildMemberDeleteData>) = event(DiscordEvent.GUILD_MEMBER_DELETE, action)
fun EventHandlerGroup.guildMembersChunk(action: EventHandler<GuildMembersChunkData>) = event(DiscordEvent.GUILD_MEMBERS_CHUNK, action)
fun EventHandlerGroup.guildRoleCreate(action: EventHandler<GuildRoleCreateData>) = event(DiscordEvent.GUILD_ROLE_CREATE, action)
fun EventHandlerGroup.guildRoleUpdate(action: EventHandler<GuildRoleUpdateData>) = event(DiscordEvent.GUILD_ROLE_UPDATE, action)
fun EventHandlerGroup.guildRoleDelete(action: EventHandler<GuildRoleDeleteData>) = event(DiscordEvent.GUILD_ROLE_DELETE, action)
fun EventHandlerGroup.messageCreate(action: EventHandler<MessageCreateData>) = event(DiscordEvent.MESSAGE_CREATE, action)
fun EventHandlerGroup.messageUpdate(action: EventHandler<MessageUpdateData>) = event(DiscordEvent.MESSAGE_UPDATE, action)
fun EventHandlerGroup.messageDelete(action: EventHandler<MessageDeleteData>) = event(DiscordEvent.MESSAGE_DELETE, action)
fun EventHandlerGroup.messageDeleteBulk(action: EventHandler<MessageDeleteBulkData>) = event(DiscordEvent.MESSAGE_DELETE_BULK, action)
fun EventHandlerGroup.messageReactionAdd(action: EventHandler<MessageReactionAddData>) = event(DiscordEvent.MESSAGE_REACTION_ADD, action)
fun EventHandlerGroup.messageReactionRemove(action: EventHandler<MessageReactionRemoveData>) = event(DiscordEvent.MESSAGE_REACTION_REMOVE, action)
fun EventHandlerGroup.messageReactionRemoveAll(action: EventHandler<MessageReactionRemoveAllData>) = event(DiscordEvent.MESSAGE_REACTION_REMOVE_ALL, action)
fun EventHandlerGroup.presenceUpdate(action: EventHandler<PresenceUpdateData>) = event(DiscordEvent.PRESENCE_UPDATE, action)
fun EventHandlerGroup.typingStart(action: EventHandler<TypingStartData>) = event(DiscordEvent.TYPING_START, action)
fun EventHandlerGroup.userUpdate(action: EventHandler<UserUpdateData>) = event(DiscordEvent.USER_UPDATE, action)
fun EventHandlerGroup.voiceStateUpdate(action: EventHandler<VoiceStateUpdateData>) = event(DiscordEvent.VOICE_STATE_UPDATE, action)
fun EventHandlerGroup.voiceServerUpdate(action: EventHandler<VoiceServerUpdateData>) = event(DiscordEvent.VOICE_SERVER_UPDATE, action)
fun EventHandlerGroup.webhooksUpdate(action: EventHandler<WebhooksUpdateData>) = event(DiscordEvent.WEBHOOKS_UPDATE, action)
