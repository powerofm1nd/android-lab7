package com.example.lab7.data
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface MainDbDao {
    //Student
    @Insert
    fun insertStudent(student: Student)
    @Query(value = "SELECT * FROM students")
    fun getAllStudnents() : Flow<List<Student>>
    @Update
    fun updateStudent(student: Student)
    @Delete
    fun deleteStudent(student: Student)

    @Query(value = "SELECT students.student_id, students.name, students.group_id " +
            "FROM students JOIN groups ON students.group_id = groups.group_id " +
            "WHERE groups.number = :groupNumber ORDER BY students.student_id ASC")
    fun getStudnentsByGroupNumber(groupNumber : String) : List<Student>


    //Group
    @Insert
    fun insertGroup(group: Group)
    @Query(value = "SELECT * FROM groups")
    fun getAllGroups() : Flow<List<Group>>
    @Delete
    fun deleteGroup(group: Group)
    @Update
    fun updateGroup(group: Group)

    //Student&Group
    @Query(value = "SELECT student_id, students.name as student_name, groups.group_id, groups.number as group_number FROM students JOIN groups ON students.group_id = groups.group_id")
    fun getAllStudentsWithGroups() : List<StudentWithGroup>

}