package com.jessecorbett.diskord.internal

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.treeToValue
import com.jessecorbett.diskord.EventListener
import com.jessecorbett.diskord.api.websocket.events.DiscordEvent
import com.jessecorbett.diskord.event.*

private data class EventContext(val eventListener: EventListener, val event: DiscordEvent, val data: JsonNode)

@Suppress("REDUNDANT_INLINE_SUSPEND_FUNCTION_TYPE")
private suspend inline fun <reified T, reified V: EventData> fireEvent(
        ctx: EventContext,
        eventFunction: suspend (T) -> Unit,
        dataFunction: (T) -> V
) {
    val data = jsonMapper.treeToValue<T>(ctx.data)
    eventFunction(data)
    ctx.eventListener.onEvent(ctx.event, dataFunction(data))
}

suspend fun dispatchEvent(eventListener: EventListener, event: DiscordEvent, data: JsonNode) {
    eventListener.onEvent(event)

    val ctx = EventContext(eventListener, event, data)
    when (event) {
        DiscordEvent.READY -> fireEvent(ctx, eventListener::onReady, ::ReadyData)
        DiscordEvent.RESUMED -> fireEvent(ctx, eventListener::onResumed, ::ResumedData)
        DiscordEvent.CHANNEL_CREATE -> fireEvent(ctx, eventListener::onChannelCreate, ::ChannelCreateData)
        DiscordEvent.CHANNEL_UPDATE -> fireEvent(ctx, eventListener::onChannelUpdate, ::ChannelUpdateData)
        DiscordEvent.CHANNEL_DELETE -> fireEvent(ctx, eventListener::onChannelDelete, ::ChannelDeleteData)
        DiscordEvent.CHANNEL_PINS_UPDATE -> fireEvent(ctx, eventListener::onChannelPinsUpdate, ::ChannelPinsUpdateData)
        DiscordEvent.GUILD_CREATE -> fireEvent(ctx, eventListener::onGuildCreate, ::GuildCreateData)
        DiscordEvent.GUILD_UPDATE -> fireEvent(ctx, eventListener::onGuildUpdate, ::GuildUpdateData)
        DiscordEvent.GUILD_DELETE -> fireEvent(ctx, eventListener::onGuildDelete, ::GuildDeleteData)
        DiscordEvent.GUILD_BAN_ADD -> fireEvent(ctx, eventListener::onGuildBanAdd, ::GuildBanAddData)
        DiscordEvent.GUILD_BAN_REMOVE -> fireEvent(ctx, eventListener::onGuildBanRemove, ::GuildBanRemoveData)
        DiscordEvent.GUILD_EMOJIS_UPDATE -> fireEvent(ctx, eventListener::onGuildEmojiUpdate, ::GuildEmojisUpdateData)
        DiscordEvent.GUILD_INTEGRATIONS_UPDATE -> fireEvent(ctx, eventListener::onGuildIntegrationsUpdate, ::GuildIntegrationsUpdateData)
        DiscordEvent.GUILD_MEMBER_ADD -> fireEvent(ctx, eventListener::onGuildMemberAdd, ::GuildMemberAddData)
        DiscordEvent.GUILD_MEMBER_UPDATE -> fireEvent(ctx, eventListener::onGuildMemberUpdate, ::GuildMemberUpdateData)
        DiscordEvent.GUILD_MEMBER_DELETE -> fireEvent(ctx, eventListener::onGuildMemberRemove, ::GuildMemberDeleteData)
        DiscordEvent.GUILD_MEMBERS_CHUNK -> fireEvent(ctx, eventListener::onGuildMemberChunk, ::GuildMembersChunkData)
        DiscordEvent.GUILD_ROLE_CREATE -> fireEvent(ctx, eventListener::onGuildRoleCreate, ::GuildRoleCreateData)
        DiscordEvent.GUILD_ROLE_UPDATE -> fireEvent(ctx, eventListener::onGuildRoleUpdate, ::GuildRoleUpdateData)
        DiscordEvent.GUILD_ROLE_DELETE -> fireEvent(ctx, eventListener::onGuildRoleDelete, ::GuildRoleDeleteData)
        DiscordEvent.MESSAGE_CREATE -> fireEvent(ctx, eventListener::onMessageCreate, ::MessageCreateData)
        DiscordEvent.MESSAGE_UPDATE -> fireEvent(ctx, eventListener::onMessageUpdate, ::MessageUpdateData)
        DiscordEvent.MESSAGE_DELETE -> fireEvent(ctx, eventListener::onMessageDelete, ::MessageDeleteData)
        DiscordEvent.MESSAGE_DELETE_BULK -> fireEvent(ctx, eventListener::onMessageBulkDelete, ::MessageDeleteBulkData)
        DiscordEvent.MESSAGE_REACTION_ADD -> fireEvent(ctx, eventListener::onMessageReactionAdd, ::MessageReactionAddData)
        DiscordEvent.MESSAGE_REACTION_REMOVE -> fireEvent(ctx, eventListener::onMessageReactionRemove, ::MessageReactionRemoveData)
        DiscordEvent.MESSAGE_REACTION_REMOVE_ALL -> fireEvent(ctx, eventListener::onMessageReactionRemoveAll, ::MessageReactionRemoveAllData)
        DiscordEvent.PRESENCE_UPDATE -> fireEvent(ctx, eventListener::onPresenceUpdate, ::PresenceUpdateData)
        DiscordEvent.TYPING_START -> fireEvent(ctx, eventListener::onTypingStart, ::TypingStartData)
        DiscordEvent.USER_UPDATE -> fireEvent(ctx, eventListener::onUserUpdate, ::UserUpdateData)
        DiscordEvent.VOICE_STATE_UPDATE -> fireEvent(ctx, eventListener::onVoiceStateUpdate, ::VoiceStateUpdateData)
        DiscordEvent.VOICE_SERVER_UPDATE -> fireEvent(ctx, eventListener::onVoiceServerUpdate, ::VoiceServerUpdateData)
        DiscordEvent.WEBHOOKS_UPDATE -> fireEvent(ctx, eventListener::onWebhooksUpdate, ::WebhooksUpdateData)
    }
}
