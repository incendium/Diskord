@file:Suppress("unused")

package com.jessecorbett.diskord.util

import com.jessecorbett.diskord.api.client.ChannelClient
import com.jessecorbett.diskord.api.client.GuildClient
import com.jessecorbett.diskord.api.exception.DiscordCompatibilityException
import com.jessecorbett.diskord.api.model.*
import com.jessecorbett.diskord.api.rest.*
import com.jessecorbett.diskord.api.rest.Embed
import com.jessecorbett.diskord.api.rest.EmbedField


/**
 * Message extensions
 */
val Message.isFromUser: Boolean
    get() = !author.isBot

val Message.isFromBot: Boolean
    get() = author.isBot && this.webhookId == null

val Message.isFromWebhook: Boolean
    get() = webhookId != null

val Message.authorId: String
    get() = author.id

val Message.words: List<String>
    get() = content.split(" ")


/**
 * User extensions
 */
fun String.toUserMention() = "<@$this>"

val User.mention: String
    get() = id.toUserMention()

val User.hasCustomAvatar: Boolean
    get() = avatarHash != null


/**
 * Role extensions
 */
fun String.toRoleMention() = "<@&$this>"

val Role.mention: String
    get() = id.toRoleMention()


/**
 * Channel extensions
 */
val Channel.mention: String
    get() = "#$name"


/**
 * Emoji extensions
 */
val Emoji.isUnicode: Boolean
    get() = id == null

val Emoji.tag: String
    get() = if (isUnicode) name else "<${if (isAnimated) "a" else ""}:$name:$id>"


/**
 * Client extensions
 */
suspend fun ChannelClient.sendMessage(message: String) = createMessage(CreateMessage(message))

suspend fun ChannelClient.sendMessage(embed: Embed) = createMessage(CreateMessage("", embed = embed))

suspend fun ChannelClient.sendEmbed(block: Embed.() -> Unit) = sendMessage(embed(block))

suspend fun GuildClient.changeNickname(nickname: String) = changeMemberNickname(PatchGuildMemberNickname(nickname))

suspend fun GuildClient.changeNickname(userId: String, nickname: String) = updateMember(userId, PatchGuildMember(nickname))

suspend fun ClientStore.channel(channelId: String) = channels[channelId].get()

suspend fun ClientStore.guild(channelId: String) = guilds[channel(channelId).guildId
        ?: throw DiscordCompatibilityException("Expected guild id for channel $channelId but found none")].get()

suspend fun ClientStore.user(userId: String) = discord.getUser(userId)
