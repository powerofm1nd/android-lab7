package com.example.lab7.data
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "students",
    foreignKeys = [
     ForeignKey(
         entity = Group::class,
         parentColumns = arrayOf("group_id"),
         childColumns = arrayOf("group_id"),
         onUpdate = ForeignKey.CASCADE,
         onDelete = ForeignKey.CASCADE
     )
    ]
)

data class Student (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "student_id")
    val id: Int? = null,
    @ColumnInfo(name = "name")
    var name: String,
    @ColumnInfo(name = "group_id")
    var group_id : Int?
)