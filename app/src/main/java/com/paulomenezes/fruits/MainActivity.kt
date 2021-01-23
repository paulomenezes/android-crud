package com.paulomenezes.fruits

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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
    }

    private lateinit var binding: ActivityMainBinding
    private var fruitList = mutableListOf<Fruit>()

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

                    fruitsAdapter.notifyItemInserted(fruitList.lastIndex)

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
}