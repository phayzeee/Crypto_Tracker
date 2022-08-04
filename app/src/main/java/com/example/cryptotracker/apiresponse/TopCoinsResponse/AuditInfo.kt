package com.example.cryptotracker.apiresponse.TopCoinsResponse

data class AuditInfo(
    val auditStatus: Int,
    val auditTime: String,
    val auditor: String,
    val coinId: String,
    val reportUrl: String,
    val score: String
)