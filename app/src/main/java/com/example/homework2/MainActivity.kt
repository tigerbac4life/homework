package com.example.homework2

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var studentAdapter: StudentAdapter
    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }



        studentAdapter = StudentAdapter(
            students,
            onEditClick = { student, position -> showEditDialog(student, position) },
            onRemoveClick = { student, position -> showDeleteDialog(student, position)
            }
        )

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null);
            val studentNameInput = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
            val studentIdInput = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

            val dialog = AlertDialog.Builder(this)
                .setTitle("Add new Student")
                .setView(dialogView)
                .setPositiveButton("Add") { _, _ ->
                    val studentName = studentNameInput.text.toString()
                    val studentId = studentIdInput.text.toString()

                    if (studentName.isNotEmpty() && studentId.isNotEmpty()) {
                        val newStudent = StudentModel(studentName, studentId)
                        students.add(newStudent)
                        studentAdapter.notifyItemInserted(students.size - 1)
                    }
                }
                .setNegativeButton("Cancel", null)
                .create();

            dialog.show();
        }
    }
    private fun showEditDialog(student: StudentModel, position: Int) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_add_student, null)
        val studentNameInput = dialogView.findViewById<EditText>(R.id.edit_text_student_name)
        val studentIdInput = dialogView.findViewById<EditText>(R.id.edit_text_student_id)

        studentNameInput.setText(student.studentName)
        studentIdInput.setText(student.studentId)

        val dialog = AlertDialog.Builder(this)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Update") { _, _ ->
                val updatedName = studentNameInput.text.toString()
                val updatedId = studentIdInput.text.toString()

                if (updatedName.isNotEmpty() && updatedId.isNotEmpty()) {
                    student.studentName = updatedName
                    student.studentId = updatedId
                    studentAdapter.notifyItemChanged(position) // Now accessible
                }
            }
            .setNegativeButton("Cancel", null)
            .create()
        dialog.show()
    }

    private fun showDeleteDialog(student: StudentModel, position: Int) {
        AlertDialog.Builder(this)
            .setTitle("Delete Student")
            .setMessage("Are you sure you want to delete ${student.studentName}?")
            .setPositiveButton("Yes") { _, _ ->
                students.removeAt(position) // Remove the student only if user confirms
                studentAdapter.notifyItemRemoved(position)
                studentAdapter.notifyItemRangeChanged(position, students.size) // Adjust the range


                val mainView = findViewById<RecyclerView>(R.id.recycler_view_students)
                Snackbar.make(mainView, "${student.studentName} deleted", Snackbar.LENGTH_LONG)
                    .setAction("Undo") {
                        // Add the student back to the original position
                        students.add(position, student)
                        studentAdapter.notifyItemInserted(position)
                    }
                    .show()

            }
            .setNegativeButton("No", null)
            .create()
            .show()
    }
}