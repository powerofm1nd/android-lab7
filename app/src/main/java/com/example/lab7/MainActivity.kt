package com.example.lab7

import GroupSpinnerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TableRow
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.asLiveData
import com.example.lab7.data.Group
import com.example.lab7.data.MainDb
import com.example.lab7.data.MainDbDao
import com.example.lab7.data.Student
import com.example.lab7.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity()
{
    lateinit var binding: ActivityMainBinding
    lateinit var db: MainDb
    lateinit var dao: MainDbDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        db = MainDb.getDb(this)
        dao = db.getDao()

        //Оновлення даних в таблиці з групами
        dao.getAllGroups().asLiveData().observe(this) { groups ->

            //Заповнення спінеру
            val groupAdapter = GroupSpinnerAdapter(this, groups)
            binding.spinnerGroups.adapter = groupAdapter
            binding.spinnerGroups.setSelection(0)

            val groupsTable = binding.groupsTable
            groupsTable.removeAllViews()

            //Заголовки
            val headersRow = TableRow(this)
            val textViewId = TextView(this)
            textViewId.text = "id"
            val textViewGroupNumber = TextView(this)
            textViewGroupNumber.text = "group_number"
            headersRow.addView(textViewId)
            headersRow.addView(textViewGroupNumber)
            groupsTable.addView(headersRow)

            //Вивід всіх груп
            groups.forEach { group ->
                val row = TableRow(this)
                val lp = TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT)
                row.layoutParams = lp

                val textViewId = TextView(this)
                textViewId.text = group.id.toString()
                val editGroupNumber = EditText(this)
                editGroupNumber.setText(group.number)

                val buttonDelete = Button(this)
                buttonDelete.setText("Delete")
                val buttonUpdate = Button(this)
                buttonUpdate.setText("Update")
                buttonUpdate.isEnabled = false

                editGroupNumber.addTextChangedListener {
                    buttonUpdate.isEnabled = true
                }

                //Обробка оновлення
                buttonUpdate.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            group.number = editGroupNumber.text.toString()
                            dao.updateGroup(group)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Group updated", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                //Обробка видалення
                buttonDelete.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            dao.deleteGroup(group)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Group deleted", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                row.addView(textViewId)
                row.addView(editGroupNumber)
                row.addView(buttonDelete)
                row.addView(buttonUpdate)
                groupsTable.addView(row)
            }
        }

        //Вивід всіх студентів
        dao.getAllStudnents().asLiveData().observe(this) { students ->
            val studentsTable = binding.studentsTable
            studentsTable.removeAllViews()

            // Заголовки
            val headersRow = TableRow(this)
            val textViewId = TextView(this)
            val textViewStudentName = TextView(this)
            val textViewGroupId = TextView(this)

            textViewId.text = "id"
            textViewGroupId.text = "group_id"
            textViewStudentName.text = "student_name"

            headersRow.addView(textViewId)
            headersRow.addView(textViewGroupId)
            headersRow.addView(textViewStudentName)
            studentsTable.addView(headersRow)

            students.forEach { student ->
                val row = TableRow(this)

                val textViewStudentId = TextView(this)
                val editTextStudentNameValue = EditText(this)
                val editTextGroupIdValue = EditText(this)
                val updateButton = Button(this)
                updateButton.text = "Update"
                updateButton.isEnabled = false
                val deleteButton = Button(this)
                deleteButton.text = "Delete"

                textViewStudentId.text = student.id.toString()
                editTextStudentNameValue.setText(student.name)
                editTextGroupIdValue.setText(student.group_id.toString())

                row.addView(textViewStudentId)
                row.addView(editTextGroupIdValue)
                row.addView(editTextStudentNameValue)
                row.addView(updateButton)
                row.addView(deleteButton)

                //розблокування кнопки update
                editTextStudentNameValue.addTextChangedListener {
                    updateButton.isEnabled = true
                }
                editTextGroupIdValue.addTextChangedListener {
                    updateButton.isEnabled = true
                }

                //Обробка оновлення
                updateButton.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            student.name = editTextStudentNameValue.text.toString()
                            student.group_id = editTextGroupIdValue.text.toString().toInt()
                            dao.updateStudent(student)
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Student updated", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                //Обробка видалення
                deleteButton.setOnClickListener {
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            dao.deleteStudent(student)

                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Student deleted", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            withContext(Dispatchers.Main) {
                                Toast.makeText(applicationContext, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }

                studentsTable.addView(row)
            }
        }
    }

    fun addStudentClick(view: View)
    {
        val studentName = binding.editTextNewStudentName.text.toString()
        val groupId = (binding.spinnerGroups.selectedItem as Group).id

        if (studentName == "")
            return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val student = Student(null, studentName, groupId)
                dao.insertStudent(student)
                withContext(Dispatchers.Main) {
                    binding.editTextNewStudentName.text.clear()
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun buttonAddGroupClick(view: View) {
        if (binding.editTextGroupName.text.toString() == "")
            return

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val group = Group(null, binding.editTextGroupName.text.toString())
                dao.insertGroup(group)
                withContext(Dispatchers.Main) {
                    binding.editTextGroupName.text.clear()
                }
            } catch (ex: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Error: ${ex.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    fun openFullTableClick(view: View)
    {
        val intent = Intent(this, JointTable::class.java)
        startActivity(intent)
    }
}