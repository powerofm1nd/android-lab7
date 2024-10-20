package com.example.lab7.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "groups", indices = [Index(value = ["number"], unique = true)])
data class Group(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "group_id")
    var id: Int? = null,
    @ColumnInfo(name = "number")
    var number : String
)
