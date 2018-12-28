package com.jessecorbett.diskord.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant

data class Message(
        @JsonProperty("id") val id: String,
        @JsonProperty("channel_id") val channelId: String,
        @JsonProperty("author") val author: User,
        @JsonProperty("content") val content: String,
        @JsonProperty("timestamp") val sentAt: Instant,
        @JsonProperty("edited_timestamp") val editedAt: Instant?,
        @JsonProperty("tts") val isTTS: Boolean,
        @JsonProperty("mention_everyone") val mentionsEveryone: Boolean,
        @JsonProperty("mentions") val usersMentioned: List<User> = emptyList(),
        @JsonProperty("mention_roles") val rolesIdsMentioned: List<String> = emptyList(),
        @JsonProperty("attachments") val attachments: List<Attachment> = emptyList(),
        @JsonProperty("embeds") val embeds: List<Embed> = emptyList(),
        @JsonProperty("reactions") val reactions: List<Reaction> = emptyList(),
        @JsonProperty("nonce") val validationNonce: String?,
        @JsonProperty("pinned") val isPinned: Boolean,
        @JsonProperty("webhook_id") val webhookId: String?,
        @JsonProperty("type") val type: MessageType,
        @JsonProperty("activity") val activity: MessageActivity?,
        @JsonProperty("application") val application: MessageApplication?
)

enum class MessageType(val code: Int) {
    DEFAULT(0),
    RECIPIENT_ADD(1),
    RECIPIENT_REMOVE(2),
    CALL(3),
    CHANNEL_NAME_CHANGE(4),
    CHANNEL_ICON_CHANGE(5),
    CHANNEL_PINNED_MESSAGE(6),
    GUILD_MEMBER_JOIN(7)
}

data class MessageActivity(
        @JsonProperty("type") val type: MessageActivityType,
        @JsonProperty("party_id") val partyId: String
)

enum class MessageActivityType(val code: Int) {
    JOIN(0),
    SPECTATE(1),
    LISTEN(2),
    JOIN_REQUEST(3)
}

data class MessageApplication(
        @JsonProperty("id") val id: String,
        @JsonProperty("cover_image") val coverImage: String,
        @JsonProperty("description") val description: String,
        @JsonProperty("icon") val icon: String,
        @JsonProperty("name") val name: String
)
