package com.paulomenezes.fruits

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.paulomenezes.fruits.databinding.ActivityAddBinding
import com.paulomenezes.fruits.models.Fruit
import java.io.FileNotFoundException
import java.io.InputStream


class AddActivity : AppCompatActivity() {
    companion object {
        const val GET_IMAGE_REQUEST_CODE = 2
        const val SAVE_BITMAP = "bitmap"
    }

    private lateinit var binding: ActivityAddBinding
    private var imageData: Bitmap? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddBinding.inflate(layoutInflater)

        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.buttonAddImage.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"

            startActivityForResult(intent, GET_IMAGE_REQUEST_CODE)
        }

        binding.buttonAdd.setOnClickListener {
            if (!isNameValid(binding.inputName.text.toString())) {
                Toast.makeText(this, R.string.error_required_name, Toast.LENGTH_LONG).show()
            } else if (!isNameValid(binding.inputBenefits.text.toString())) {
                Toast.makeText(this, R.string.error_required_benefits, Toast.LENGTH_LONG).show()
            } else if (imageData == null) {
                Toast.makeText(this, R.string.error_required_photo, Toast.LENGTH_LONG).show()
            } else {
                val fruit = Fruit(
                        binding.inputName.text.toString(),
                        binding.inputBenefits.text.toString(),
                        imageData
                )

                val intent = Intent()
                intent.putExtra(MainActivity.ADD_FRUIT_EXTRA_NAME, fruit)

                setResult(RESULT_OK, intent)
                finish()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK && data != null) {
            try {
                val imageUri: Uri? = data.data
                val imageStream: InputStream? = contentResolver.openInputStream(imageUri!!)
                val selectedImage = BitmapFactory.decodeStream(imageStream)
                binding.imageView2.setImageBitmap(selectedImage)

                imageData = selectedImage
            } catch (e: FileNotFoundException) {
                Toast.makeText(this, R.string.load_image_error, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        outState.putParcelable(SAVE_BITMAP, imageData)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        val bitmap = savedInstanceState.getParcelable<Bitmap>(SAVE_BITMAP)

        if (bitmap != null) {
            imageData = bitmap

            binding.imageView2.setImageBitmap(imageData)
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

    private fun isNameValid(name: String): Boolean = name.isNotEmpty()
}