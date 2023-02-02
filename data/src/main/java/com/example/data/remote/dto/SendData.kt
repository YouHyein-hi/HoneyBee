package com.example.data.remote.dto

import com.example.domain.model.DomainSendData

/**
 * 2023-02-02
 * pureum
 */
//data class SendData(
//    val pureum:String
//)
//
//fun SendData.toDomainSendData() = DomainSendData(pureum)

data class SendData(
    val info: Info,
    val results: List<Result>
)
data class Info(
    val count: Int,
    val next: String,
    val pages: Int,
    val prev: String
)
data class Result(
    val created: String,
    val episode: List<String>,
    val gender: String,
    val id: Int,
    val image: String,
    val location: Location,
    val name: String,
    val origin: Origin,
    val species: String,
    val status: String,
    val type: String,
    val url: String
)
data class Location(
    val name: String,
    val url: String
)
data class Origin(
    val name: String,
    val url: String
)

fun SendData.toDomainSendData(): List<DomainSendData> =
    results.map { DomainSendData(it.name,it.status,it.species,it.image) }