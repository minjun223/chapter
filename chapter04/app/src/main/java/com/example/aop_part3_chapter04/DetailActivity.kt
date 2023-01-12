package com.example.aop_part3_chapter04

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.bumptech.glide.Glide
import com.example.aop_part3_chapter04.databinding.ActivityDetailBinding
import com.example.aop_part3_chapter04.model.Book
import com.example.aop_part3_chapter04.model.Review

class DetailActivity: AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java,
            "BookSearchDB"
        ).build()

        val model = intent.getParcelableExtra<Book>("bookModel")
        binding.titleTextView.text = model?.title.orEmpty()
        binding.descriptionTextView.text = model?.description.orEmpty()

        Glide.with(binding.coverImageView.context)
            .load(model?.coverSmallUrl.orEmpty())
            .into(binding.coverImageView)

        Thread{
//            val review = db.ReviewDao().getOneReview(model?.id?.toInt() ?: 0)
//            runOnUiThread {
//                binding.reviewEditText.setText(review.review.orEmpty())
//            }
        }

        binding.saveButton.setOnClickListener{
            Thread{
                db.reviewDao().saveReview(
                    Review(model?.id?.toInt() ?: 0,
                    binding.reviewEditText.text.toString())
                )
            }
        }
    }
}