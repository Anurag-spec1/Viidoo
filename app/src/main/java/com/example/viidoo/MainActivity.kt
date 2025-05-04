package com.example.viidoo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.addTextChangedListener
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.Collections

class MainActivity : AppCompatActivity() {
    private lateinit var currentuser: TextView
    private lateinit var sgnout: CardView
    private lateinit var targetuser: EditText
    private lateinit var voicecall: ZegoSendCallInvitationButton
    private lateinit var videocall: ZegoSendCallInvitationButton
    private val sharedPref by lazy { getSharedPreferences("UserPrefs", MODE_PRIVATE) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        currentuser = findViewById(R.id.currentuser)
        targetuser = findViewById(R.id.target_user)
        voicecall = findViewById(R.id.voice_call_btn)
        videocall = findViewById(R.id.video_call_btn)
        sgnout = findViewById(R.id.sgnOut)

        // ✅ Retrieve username (fallback to "Guest" if null)
        val username = sharedPref.getString("username", "Guest") 

        // ✅ Display current user
        currentuser.text = username

        // ✅ Update call setup when target user text changes
        targetuser.addTextChangedListener {
            val targetUsername = targetuser.text.toString().trim() // Trim spaces
            if (targetUsername.isNotEmpty()) {
                setupVideo(targetUsername)
                setupVoice(targetUsername)
            }

        }

        // ✅ Sign Out Logic
        sgnout.setOnClickListener {
            sharedPref.edit().apply {
                putBoolean("checked", false) // Clear Remember Me
                remove("username") // Clear stored username
                apply()
            }

            // Redirect to LoginActivity
            startActivity(Intent(this, Login::class.java))
            finish() // Close MainActivity
        }
    }

    private fun setupVoice(username: String) {
        voicecall.setIsVideoCall(false)
        voicecall.resourceID = "zego_uikit_call"
        voicecall.setInvitees(Collections.singletonList(ZegoUIKitUser(username, username)))
    }

    private fun setupVideo(username: String) {
        videocall.setIsVideoCall(true)
        videocall.resourceID = "zego_uikit_call"
        videocall.setInvitees(Collections.singletonList(ZegoUIKitUser(username, username)))
    }
}