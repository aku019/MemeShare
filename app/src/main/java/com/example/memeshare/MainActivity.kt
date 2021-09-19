package com.example.memeshare

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target

class MainActivity : AppCompatActivity() {

    var currentImageUrl:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadMeme()
    }

    @SuppressLint("CheckResult")
    private fun loadMeme()
    {
        val pb :View =  findViewById(R.id.progressBar)
        pb.visibility = View.VISIBLE
        val textView = findViewById<TextView>(R.id.text)
        val myImageView:ImageView =  findViewById (R.id.memeImageView)
// ...

// Instantiate the RequestQueue.
        val url = "https://meme-api.herokuapp.com/gimme"

// Request a string response from the provided URL.
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                val url = response.getString("url")
                currentImageUrl = response.getString("url")
                //Glide.with(this).load(url).into(myImageView)
                Glide.with(this).load(url).listener(object :
                    RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pb.visibility=View.GONE
                        return false
                            }

                    @SuppressLint("CheckResult")
                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        pb.visibility=View.GONE
                        return false

                    }
                    }).into(myImageView)


            },
            {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            },
        )

// Add the request to the RequestQueue.
        //queue.add(stringRequest)
        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
    }

    fun shareMeme(view: View) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type="text/plain"
        intent.putExtra(Intent.EXTRA_TEXT,"Hey Checkout this cool meme I got from Reddit $currentImageUrl")
        val chooser = Intent.createChooser(intent,"Share this meme using...")
        startActivity(chooser)
    }
    fun nextMeme(view: View) {
        loadMeme()
    }
}