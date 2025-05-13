package com.example.viidoo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import androidx.appcompat.app.AppCompatActivity
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var currentuser: TextView
    private lateinit var sgnout: MaterialButton
    private lateinit var targetuser: TextInputEditText
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

        // Get saved username or default to Guest
        val username = sharedPref.getString("username", "Guest")
        currentuser.text = username

        // Enable/Disable call buttons based on input
        targetuser.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val targetUsername = s.toString().trim()
                val isValid = targetUsername.isNotEmpty()
                voicecall.isEnabled = isValid
                videocall.isEnabled = isValid

                if (isValid) {
                    setupVoice(targetUsername)
                    setupVideo(targetUsername)
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        sgnout.setOnClickListener {
            sharedPref.edit().apply {
                putBoolean("checked", false)
                remove("username")
                apply()
            }
            startActivity(Intent(this, Login::class.java))
            finish()
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