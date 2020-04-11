package com.example.td_4_exo_3

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import kotlinx.android.synthetic.main.activity_main.*
import java.time.LocalDate
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity() {

    var taskToday = arrayListOf<Task>()
    var adapter = TaskAdapter(this, taskToday)
    lateinit var positionSpinner: Spinner
    lateinit var editText: EditText
    var tasks = arrayListOf<Task>(
        Task("doing some ", 11, 4, 2020)
        , Task("doing some tasks", 3, 12, 2020),
        Task("doing  tasks", 3, 12, 2020),
        Task("doing some tasks", 3, 12, 2020),
        Task(" some tasks", 11, 4, 2020),
        Task("doing some ", 3, 12, 2020)
    )

    private lateinit var listView: ListView
    @SuppressLint("NewApi")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter1 = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            listOf("Todat", "This week", "All")
        )

        //tablette en mode Portrait
        if (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE and Configuration.ORIENTATION_PORTRAIT) {


            adapter1.setDropDownViewResource(android.R.layout.simple_list_item_1)
            positionSpinner.adapter = adapter1
            positionSpinner = findViewById<Spinner>(R.id.positionSpinner) as Spinner

            positionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onNothingSelected(parent: AdapterView<*>?) {

                }

                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?,
                    position: Int,
                    id: Long
                ) {

                    val item = adapter1.getItem(position)

                    when (position) {
                        0 -> taskToday =
                            tasks.filter { task -> task.day == LocalDate.now().dayOfMonth && task.year == LocalDate.now().year && task.month == LocalDate.now().monthValue } as ArrayList<Task>
                        1 -> taskToday =
                            tasks.filter { task -> task.month * 30 + task.day >= LocalDate.now().dayOfYear } as ArrayList<Task>
                        2 -> taskToday = tasks
                    }
                    adapter= TaskAdapter(MainActivity(), taskToday)
                    listView.adapter = adapter

                }
            }
        }
        //tablette en mode Portrait




        //tablette en mode paysage


        if (resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK === Configuration.SCREENLAYOUT_SIZE_LARGE and Configuration.ORIENTATION_LANDSCAPE) {
            run {

                fun onCreateOptionsMenu(menu: Menu?): Boolean {
                    super.onCreateOptionsMenu(menu)
                    menuInflater.inflate(R.menu.menu, menu)
                    return true
                }

                fun onOptionsItemSelected(item: MenuItem): Boolean {
                    var selectedOption = ""
                    when (item?.itemId) {

                        R.id.this_day -> taskToday =
                            tasks.filter { task -> task.day == LocalDate.now().dayOfMonth && task.year == LocalDate.now().year && task.month == LocalDate.now().monthValue } as ArrayList<Task>
                        R.id.this_week -> taskToday =
                            tasks.filter { task -> task.month * 30 + task.day >= LocalDate.now().dayOfYear } as ArrayList<Task>
                        R.id.all -> taskToday = tasks


                    }
                    adapter = TaskAdapter(this, taskToday)
                    listView.adapter = adapter
                    return super.onOptionsItemSelected(item)
                }


            }
        }
            //tablette en mode paysage


        editText = findViewById(R.id.editText) as EditText
        //    val adapter = ArrayAdapter(this,
        //    R.layout.listitem, array)
        val listView: ListView = findViewById(R.id.listview_1)

        // val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, array)
        taskToday =
            tasks.filter { task -> task.day == 11 && task.year == LocalDate.now().year && task.month == LocalDate.now().monthValue } as ArrayList<Task>
         adapter = TaskAdapter(this, taskToday)
        listView.adapter = adapter


        val dateTimePickerPicker = DatePicker(this)
        val button = Button(this)
        button.background.setTint(Color.parseColor("#2196F3"))
        button.setTextColor(Color.parseColor("#FFFFFF"))
        button.text = "add"


        button.gravity = Gravity.CENTER
        button.setOnClickListener {
            val task = Task(
                editText.text.toString(),
                dateTimePickerPicker.dayOfMonth,
                dateTimePickerPicker.month,
                dateTimePickerPicker.year
            )
            tasks.add(task)
            taskToday.add(task)
            linear_layout.visibility = View.GONE
            editText.clearFocus()
            editText.setText("")
            adapter.notifyDataSetChanged()
        }
        val linearLayout = findViewById<LinearLayout>(R.id.linear_layout)
        // add datePicker in LinearLayout
        linearLayout?.addView(dateTimePickerPicker)
        linearLayout?.addView(button)
        linear_layout.visibility = View.GONE




        tv_button.setOnClickListener {
            editText.clearFocus()
            linear_layout.visibility = View.VISIBLE
            val imm = this.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
        }




        listView.onItemClickListener = object : AdapterView.OnItemClickListener {

            override fun onItemClick(
                parent: AdapterView<*>, view: View,
                position: Int, id: Long
            ) {

                // value of item that is clicked
                taskToday.remove(taskToday[position])
                adapter.notifyDataSetChanged()
            }
        }


    }


    class TaskAdapter(context: MainActivity, array: ArrayList<Task>) : BaseAdapter() {
        private val ncontext: Context
        private var tasks = arrayListOf<Task>()

        init {
            ncontext = context
            tasks = array
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val layoutInflater = LayoutInflater.from(ncontext)

            val row = layoutInflater.inflate(R.layout.listitem, parent, false)
            val taskTextView = row.findViewById<TextView>(R.id.text_view_1)
            taskTextView.text = tasks[position].taskDescription
            val timeTextView = row.findViewById<TextView>(R.id.text_view_2)
            val item = tasks[position]
            timeTextView.text = "${item.day}/${item.month}/${item.year}"
            return row
        }

        override fun getItem(position: Int): Any {
            return "TESTsTRING"
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return tasks.size
        }


    }


    class Task(taskDescription: String, day: Int, month: Int, year: Int) {
        val taskDescription = taskDescription
        val day = day
        val month = month
        val year = year

    }
}
