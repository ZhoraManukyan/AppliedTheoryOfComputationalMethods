package com.example.appliedtheoryofcomputationalmethods

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform