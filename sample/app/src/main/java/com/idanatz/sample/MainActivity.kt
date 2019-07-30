package com.idanatz.sample

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.idanatz.sample.advanced_example.view.AdvancedJavaExampleActivity
import com.idanatz.sample.advanced_example.view.AdvancedKotlinExampleActivity
import com.idanatz.sample.simple_example.SimpleJavaExampleActivity
import com.idanatz.oneadapter.sample.R

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById<Button>(R.id.simple_java_activity_button).setOnClickListener { startActivity(Intent(this@MainActivity, SimpleJavaExampleActivity::class.java)) }
        findViewById<Button>(R.id.advanced_java_activity_button).setOnClickListener { startActivity(Intent(this@MainActivity, AdvancedJavaExampleActivity::class.java)) }
        findViewById<Button>(R.id.advanced_kotlin_activity_button).setOnClickListener { startActivity(Intent(this@MainActivity, AdvancedKotlinExampleActivity::class.java)) }
    }
}