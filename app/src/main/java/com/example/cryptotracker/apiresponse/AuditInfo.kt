package com.example.cryptotracker.apiresponse

data class AuditInfo(
    val auditStatus: Int,
    val auditTime: String,
    val auditor: String,
    val coinId: String,
    val reportUrl: String,
    val score: String
)