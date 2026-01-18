package com.awab.ai

import android.content.Intent
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var chatContainer: LinearLayout
    private lateinit var inputField: EditText
    private lateinit var scrollView: ScrollView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        val mainLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(0xFFF0F2F5.toInt())
        }

        // Chat area (scrollable)
        scrollView = ScrollView(this).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
        }

        chatContainer = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
        }

        scrollView.addView(chatContainer)
        mainLayout.addView(scrollView)

        // Input area
        val inputArea = createInputArea()
        mainLayout.addView(inputArea)

        setContentView(mainLayout)
        
        // Auto-scroll when layout changes (keyboard opens/closes)
        mainLayout.viewTreeObserver.addOnGlobalLayoutListener {
            scrollToBottom()
        }

        // Welcome message
        addBotMessage("مرحباً! أنا أواب AI 🤖\n\nكيف يمكني مساعدتك اليوم؟")
    }
        mainLayout.addView(inputArea)

        setContentView(mainLayout)

        // Welcome message
        addBotMessage("مرحباً! أنا أواب AI 🤖\n\nكيف يمكني مساعدتك اليوم؟")
    }

    private fun createInputArea(): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setBackgroundColor(0xFFFFFFFF.toInt())
            setPadding(16, 16, 16, 16)
            gravity = Gravity.CENTER_VERTICAL

            inputField = EditText(this@MainActivity).apply {
                hint = "اكتب رسالتك هنا..."
                textSize = 16f
                setPadding(20, 16, 20, 16)
                layoutParams = LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1f
                )
                background = createRoundedBackground(0xFFF0F2F5.toInt(), 24f)
                
                // Scroll to bottom when user focuses on input
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        android.os.Handler(mainLooper).postDelayed({
                            scrollToBottom()
                        }, 300)
                    }
                }
            }
            addView(inputField)

            val sendText = TextView(this@MainActivity).apply {
                text = "➤"
                textSize = 28f
                setTextColor(0xFF075E54.toInt())
                setPadding(16, 0, 0, 0)
                setOnClickListener {
                    sendMessage()
                }
            }
            addView(sendText)
            
            // Settings icon in input area
            val settingsIcon = TextView(this@MainActivity).apply {
                text = "⚙️"
                textSize = 24f
                setTextColor(0xFF075E54.toInt())
                setPadding(16, 0, 0, 0)
                setOnClickListener {
                    openSettings()
                }
            }
            addView(settingsIcon)
        }
    }

    private fun sendMessage() {
        val message = inputField.text.toString().trim()
        if (message.isEmpty()) return

        addUserMessage(message)
        inputField.text.clear()

        // Simulate bot response
        android.os.Handler(mainLooper).postDelayed({
            handleBotResponse(message)
        }, 500)
    }

    private fun handleBotResponse(userMessage: String) {
        val response = when {
            userMessage.contains("مرحبا", ignoreCase = true) || 
            userMessage.contains("السلام", ignoreCase = true) ||
            userMessage.contains("هلا", ignoreCase = true) -> {
                "مرحباً بك! 👋\n\nأنا مساعدك الذكي. يمكنني مساعدتك في:\n• إدارة الأذونات\n• الإعدادات\n• وأكثر..."
            }
            userMessage.contains("أذونات", ignoreCase = true) || 
            userMessage.contains("صلاحيات", ignoreCase = true) ||
            userMessage.contains("permission", ignoreCase = true) -> {
                "لإدارة الأذونات، اضغط على زر الإعدادات ⚙️ في الأعلى.\n\nهناك يمكنك:\n✓ طلب الأذونات العادية\n✓ الأذونات الخاصة\n✓ إمكانية الوصول"
            }
            userMessage.contains("كيف", ignoreCase = true) || 
            userMessage.contains("ساعد", ignoreCase = true) ||
            userMessage.contains("help", ignoreCase = true) -> {
                "يمكنني مساعدتك! 🤝\n\nجرب:\n• \"الأذونات\" - لمعرفة عن الأذونات\n• \"الإعدادات\" - للذهاب للإعدادات\n• أو اسألني أي سؤال"
            }
            userMessage.contains("إعدادات", ignoreCase = true) || 
            userMessage.contains("settings", ignoreCase = true) -> {
                "سأفتح لك صفحة الإعدادات..."
                openSettings()
                return
            }
            else -> {
                "شكراً على رسالتك! 😊\n\nأنا ما زلت قيد التطوير. للوصول للإعدادات، اضغط على ⚙️ في الأعلى."
            }
        }

        addBotMessage(response)
    }

    private fun addUserMessage(message: String) {
        val messageView = createMessageBubble(message, isUser = true)
        chatContainer.addView(messageView)
        scrollToBottom()
    }

    private fun addBotMessage(message: String) {
        val messageView = createMessageBubble(message, isUser = false)
        chatContainer.addView(messageView)
        scrollToBottom()
    }

    private fun createMessageBubble(message: String, isUser: Boolean): LinearLayout {
        return LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
            gravity = if (isUser) Gravity.END else Gravity.START

            val bubble = TextView(this@MainActivity).apply {
                text = message
                textSize = 16f
                setPadding(20, 16, 20, 16)
                setTextColor(if (isUser) 0xFFFFFFFF.toInt() else 0xFF000000.toInt())
                background = createRoundedBackground(
                    if (isUser) 0xFF075E54.toInt() else 0xFFFFFFFF.toInt(),
                    16f
                )
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    val maxWidth = (resources.displayMetrics.widthPixels * 0.75).toInt()
                    this.width = ViewGroup.LayoutParams.WRAP_CONTENT
                }
                maxWidth = (resources.displayMetrics.widthPixels * 0.75).toInt()
            }
            addView(bubble)
        }
    }

    private fun createRoundedBackground(color: Int, radius: Float): GradientDrawable {
        return GradientDrawable().apply {
            setColor(color)
            cornerRadius = radius
        }
    }

    private fun scrollToBottom() {
        scrollView.post {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN)
        }
    }

    private fun openSettings() {
        val intent = Intent(this, SettingsActivity::class.java)
        startActivity(intent)
    }
}
