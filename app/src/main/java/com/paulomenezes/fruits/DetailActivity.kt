package com.paulomenezes.fruits

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paulomenezes.fruits.databinding.ActivityDetailBinding
import com.paulomenezes.fruits.models.Fruit

class DetailActivity: AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDetailBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val fruit = intent.getParcelableExtra<Fruit>(MainActivity.ADD_FRUIT_EXTRA_NAME)
        val index = intent.getIntExtra(MainActivity.DETAIL_FRUIT_EXTRA_NAME, 0)

        if (fruit != null) {
            binding.imageView3.setImageBitmap(fruit.image)
            binding.textDetailName.text = fruit.name
            binding.textDetailBenefit.text = fruit.benefits

            binding.buttonDelete.setOnClickListener {
                val intent = Intent()
                intent.putExtra(MainActivity.DETAIL_FRUIT_EXTRA_NAME, index)

                setResult(RESULT_OK, intent)

                finish()
            }
        } else {
            Toast.makeText(this, R.string.error_no_fruit, Toast.LENGTH_SHORT).show()

            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        return true
    }
}