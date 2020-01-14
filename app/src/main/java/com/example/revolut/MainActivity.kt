package com.example.revolut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currency_title.text = "USD"
        currency_description.text = "US Dollar"
        country_flag.setBackgroundResource(R.drawable.us)
        currency_amount.setText("1183.06")
    }
}
