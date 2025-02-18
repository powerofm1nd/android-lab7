package com.example.lab7

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.asLiveData
import com.example.lab7.data.MainDb
import com.example.lab7.data.MainDbDao
import com.example.lab7.databinding.ActivityJointTableBinding
import com.example.lab7.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class JointTable : AppCompatActivity() {

    lateinit var binding: ActivityJointTableBinding
    lateinit var db: MainDb
    lateinit var dao: MainDbDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJointTableBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)
        db = MainDb.getDb(this)
        dao = db.getDao()
    }

    fun buttonBackClick(view: View) {
        finish()
    }

    fun buttonGetStudentsWithGroupsClick(view: View) {
        GlobalScope.launch {
            val studentsWithGroups = withContext(Dispatchers.IO) {
                dao.getAllStudentsWithGroups()
            }

            withContext(Dispatchers.Main) {
                binding.textViewStudentsWithGroups.text = ""
                binding.textViewStudentsWithGroups.append("group_id\tgroup_number\tstudent_id\tstudent_name\n")
                studentsWithGroups.forEach { item ->
                    binding.textViewStudentsWithGroups.append("${item.group_id}\t${item.group_number}\t${item.student_id}\t${item.student_name}\n")
                }
            }
        }
    }

    fun buttonGetStudentsByGroupClick(view: View) {
        GlobalScope.launch {
            val studentsByGroup = withContext(Dispatchers.IO)
            {
                dao.getStudnentsByGroupNumber(binding.editTextNumberOfGroupToFind.text.toString())
            }

            withContext(Dispatchers.Main) {
                binding.textViewStudentsWithGroups.text = ""
                binding.textViewStudentsWithGroups.append("student_id\titem.student_name\n")
                studentsByGroup.forEach {
                    item ->
                    binding.textViewStudentsWithGroups.append("${item.id}\t${item.name}\n")
                }
            }
        }
    }
}