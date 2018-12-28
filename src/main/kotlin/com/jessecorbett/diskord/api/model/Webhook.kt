package com.jessecorbett.diskord.api.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.jessecorbett.diskord.api.model.User

data class Webhook(
        @JsonProperty("id") val id: String,
        @JsonProperty("guild_id") val guildId: String?,
        @JsonProperty("channel_id") val channelId: String,
        @JsonProperty("user") val user: User?,
        @JsonProperty("name") val defaultName: String?,
        @JsonProperty("avatar") val defaultAvatarHash: String?,
        @JsonProperty("token") val token: String
)
