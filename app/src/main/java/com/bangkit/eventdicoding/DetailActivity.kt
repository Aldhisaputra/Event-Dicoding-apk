package com.bangkit.eventdicoding

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bangkit.eventdicoding.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val event = getEventFromIntent()

        with(binding) {
            Name.text = event.name
            tvBeginTime.text = event.beginTime

            Glide.with(this@DetailActivity)
                .load(event.mediaCover)
                .into(MediaCover)

            Kuota.text = ((event.quota ?: 0) - (event.registrants ?: 0)).toString()

            Deskripsi.text = event.description?.let {
                HtmlCompat.fromHtml(it, HtmlCompat.FROM_HTML_MODE_LEGACY)
            }

            btnRegister.setOnClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(event.link)))
            }
        }
    }

    private fun getEventFromIntent(): ListEventsItem {
        val defaultEvent = ListEventsItem(
            mediaCover = "image_url",
            registrants = 0,
            link = "example.com",
            description = "Description",
            quota = 0,
            name = "Unknown Event",
        )

        return if (Build.VERSION.SDK_INT >= 33) {
            intent.getParcelableExtra(EXTRA_EVENT, ListEventsItem::class.java) ?: defaultEvent
        } else {
            @Suppress("DEPRECATION")
            intent.getParcelableExtra(EXTRA_EVENT) ?: defaultEvent
        }
    }

    companion object {
        const val EXTRA_EVENT = "extra_event"
    }
}
