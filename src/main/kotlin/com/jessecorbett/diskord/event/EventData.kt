package com.jessecorbett.diskord.event

import com.jessecorbett.diskord.api.Channel
import com.jessecorbett.diskord.api.Guild
import com.jessecorbett.diskord.api.Message
import com.jessecorbett.diskord.api.User
import com.jessecorbett.diskord.api.gateway.events.*
import com.jessecorbett.diskord.api.models.BulkMessageDelete
import com.jessecorbett.diskord.api.models.MessageDelete
import com.jessecorbett.diskord.api.models.VoiceState

sealed class EventData
data class ReadyData(val data: Ready) : EventData()
data class ResumedData(val data: Resumed) : EventData()
data class ChannelCreateData(val data: Channel) : EventData()
data class ChannelUpdateData(val data: Channel) : EventData()
data class ChannelDeleteData(val data: Channel) : EventData()
data class ChannelPinsUpdateData(val data: ChannelPinUpdate) : EventData()
data class GuildCreateData(val data: CreatedGuild) : EventData()
data class GuildUpdateData(val data: Guild) : EventData()
data class GuildDeleteData(val data: Guild) : EventData()
data class GuildBanAddData(val data: GuildBan) : EventData()
data class GuildBanRemoveData(val data: GuildBan) : EventData()
data class GuildEmojisUpdateData(val data: GuildEmojiUpdate) : EventData()
data class GuildIntegrationsUpdateData(val data: GuildIntegrationUpdate) : EventData()
data class GuildMemberAddData(val data: GuildMemberAdd) : EventData()
data class GuildMemberUpdateData(val data: GuildMemberUpdate) : EventData()
data class GuildMemberDeleteData(val data: GuildMemeberRemove) : EventData()
data class GuildMembersChunkData(val data: GuildMembersChunk) : EventData()
data class GuildRoleCreateData(val data: GuildRoleCreate) : EventData()
data class GuildRoleUpdateData(val data: GuildRoleUpdate) : EventData()
data class GuildRoleDeleteData(val data: GuildRoleDelete) : EventData()
data class MessageCreateData(val data: Message) : EventData()
data class MessageUpdateData(val data: MessageUpdate) : EventData()
data class MessageDeleteData(val data: MessageDelete) : EventData()
data class MessageDeleteBulkData(val data: BulkMessageDelete) : EventData()
data class MessageReactionAddData(val data: MessageReaction) : EventData()
data class MessageReactionRemoveData(val data: MessageReaction) : EventData()
data class MessageReactionRemoveAllData(val data: MessageReactionRemoveAll) : EventData()
data class PresenceUpdateData(val data: PresenceUpdate) : EventData()
data class TypingStartData(val data: TypingStart) : EventData()
data class UserUpdateData(val data: User) : EventData()
data class VoiceStateUpdateData(val data: VoiceState) : EventData()
data class VoiceServerUpdateData(val data: VoiceServerUpdate) : EventData()
data class WebhooksUpdateData(val data: WebhookUpdate) : EventData()
