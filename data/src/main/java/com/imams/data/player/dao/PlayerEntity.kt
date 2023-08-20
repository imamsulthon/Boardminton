package com.imams.data.player.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("player_entity")
data class PlayerEntity(

    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    @ColumnInfo("full_name") val fullName: String,
    @ColumnInfo("first_name") val firstName: String,
    @ColumnInfo("last_name") val lastName: String,
    @ColumnInfo("height") val height: Int,
    @ColumnInfo("weight") val weight: Int,
    @ColumnInfo("gender") val gender: String,
    @ColumnInfo("hand_play") val handPlay: String,
    @ColumnInfo("dob") val dob: Long,
    @ColumnInfo("photo_profile_uri") val photoProfileUri: String,

)