package com.jessecorbett.diskord.api.websocket.events

import com.fasterxml.jackson.annotation.JsonProperty

data class Hello(
        @JsonProperty("heartbeat_interval") val heartbeatInterval: Long,
        @JsonProperty("_trace") val trace: List<String>
)
