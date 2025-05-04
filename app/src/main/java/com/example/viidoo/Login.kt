package com.example.viidoo

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import kotlin.math.log

class Login : AppCompatActivity() {
    private lateinit var usernameEditText: EditText
    private lateinit var loginBtn: CardView
    private lateinit var remme: CheckBox
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE)

        usernameEditText = findViewById(R.id.userId)
        loginBtn = findViewById(R.id.Login)
        remme = findViewById(R.id.remme)



        if (sharedPref.getBoolean("checked", false)) {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        loginBtn.setOnClickListener {
            val username = usernameEditText.text.toString().trim()

            if (username.isEmpty()) {
                Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }


            sharedPref.edit().apply {
                if (remme.isChecked) {
                    putBoolean("checked", true) // Remember Me enabled
                    putString("username", username) // Save username
                } else {
                    putBoolean("checked", false) // Ensure it's reset if unchecked
                    remove("username") // Remove stored username
                }
                     apply()
            }


            val config = ZegoUIKitPrebuiltCallInvitationConfig()
            ZegoUIKitPrebuiltCallService.init(
                application,
                AppConstants.appId,
                AppConstants.appSign,
                username,
                username,
                config
            )


            startActivity(Intent(this, MainActivity::class.java).apply {
                putExtra("username", username)
            })
            finish()
        }
    }
}

