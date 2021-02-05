package com.paulomenezes.fruits

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.widget.SwitchCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.paulomenezes.fruits.adapters.FruitsAdapter
import com.paulomenezes.fruits.databinding.ActivityMainBinding
import com.paulomenezes.fruits.models.Fruit
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    companion object {
        const val ADD_FRUIT_REQUEST_CODE = 1
        const val DETAIL_FRUIT_REQUEST_CODE = 2
        const val ADD_FRUIT_EXTRA_NAME = "fruit"
        const val DETAIL_FRUIT_EXTRA_NAME = "index"
        const val SAVED_INSTANCE_EXTRA_ID = "list"

        const val FILTER_REMOVE_DUPLICATES = "R"
        const val FILTER_SHOW_ALL = ""

        const val ORDER_BY_ADDED = "A"
        const val ORDER_BY_LETTER = "L"
    }

    private lateinit var binding: ActivityMainBinding
    private var fruitList = mutableListOf<Fruit>()

    private var SHOW_DUPLICATES = true
    private var ORDER_BY = ORDER_BY_ADDED

    private val fruitsAdapter = FruitsAdapter(this, fruitList) { index ->
        val intent = Intent(this, DetailActivity::class.java)
        if (index >= 0 && index <= fruitList.lastIndex) {
            intent.putExtra(ADD_FRUIT_EXTRA_NAME, fruitList[index])
            intent.putExtra(DETAIL_FRUIT_EXTRA_NAME, index)

            startActivityForResult(intent, DETAIL_FRUIT_REQUEST_CODE)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setSupportActionBar(binding.mainToolbar)

        binding.recyclerView.adapter = fruitsAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        binding.addButton.setOnClickListener {
            val intent = Intent(this, AddActivity::class.java)

            startActivityForResult(intent, ADD_FRUIT_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == ADD_FRUIT_REQUEST_CODE) {
                val fruit = data.getParcelableExtra<Fruit>(ADD_FRUIT_EXTRA_NAME)

                if (fruit != null) {
                    fruitList.add(fruit)
                    fruitsAdapter.add(fruit)

                    fruitsAdapter.notifyDataSetChanged()

                    Toast.makeText(this, R.string.message_add, Toast.LENGTH_SHORT).show()
                }
            } else if (requestCode == DETAIL_FRUIT_REQUEST_CODE) {
                val index = data.getIntExtra(DETAIL_FRUIT_EXTRA_NAME, -1)

                if (index >= 0) {
                    fruitList.removeAt(index)

                    fruitsAdapter.notifyDataSetChanged()

                    Toast.makeText(this, R.string.message_delete, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(SAVED_INSTANCE_EXTRA_ID, ArrayList(fruitList))
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val list = savedInstanceState.getParcelableArrayList<Fruit>(SAVED_INSTANCE_EXTRA_ID)

        if (list != null) {
            fruitList.addAll(list)
            fruitsAdapter.notifyDataSetChanged()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_filter -> {
            val view = layoutInflater.inflate(R.layout.dialog_filter, null);

            val builder = AlertDialog.Builder(this);
            builder.setView(view);

            val modal = builder.create();
            modal.show()

            val switch = view.findViewById<SwitchCompat>(R.id.showDuplicatesSwitch)
            switch.isChecked = SHOW_DUPLICATES

            val insertRadioButton = view.findViewById<RadioButton>(R.id.insertRadioButton)
            val letterRadioButton = view.findViewById<RadioButton>(R.id.letterRadioButton)

            if (ORDER_BY == ORDER_BY_ADDED) {
                insertRadioButton.isChecked = true
            } else {
                letterRadioButton.isChecked = true
            }

            view.findViewById<Button>(R.id.okButton).setOnClickListener {
                SHOW_DUPLICATES = switch.isChecked

                if (insertRadioButton.isChecked) {
                    ORDER_BY = ORDER_BY_ADDED

                    fruitList.sortWith(compareBy { it.id })
                } else {
                    ORDER_BY = ORDER_BY_LETTER

                    fruitList.sortWith(compareBy { it.name })
                }

                fruitsAdapter.originalList.clear()
                fruitsAdapter.originalList.addAll(fruitList)

                if (switch.isChecked) {
                    fruitsAdapter.filter.filter(FILTER_SHOW_ALL)
                } else {
                    fruitsAdapter.filter.filter(FILTER_REMOVE_DUPLICATES)
                }

                fruitsAdapter.notifyDataSetChanged()

                modal.dismiss()
            }

            true
        }

        else -> {
            // If we got here, the user's action was not recognized.
            // Invoke the superclass to handle it.
            super.onOptionsItemSelected(item)
        }
    }
}