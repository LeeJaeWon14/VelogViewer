package com.jeepchief.velogviewer.view

import android.content.Intent
import android.os.Bundle
import android.webkit.WebSettings
import android.widget.Toast
import com.jeepchief.velogviewer.Constants
import com.jeepchief.velogviewer.R
import com.jeepchief.velogviewer.databinding.ActivityMainBinding
import com.jeepchief.velogviewer.databinding.LayoutInputIdDialogBinding
import com.jeepchief.velogviewer.util.DialogHelper
import com.jeepchief.velogviewer.util.Log
import com.jeepchief.velogviewer.util.Pref
import com.jeepchief.velogviewer.web.VVChromeClient
import com.jeepchief.velogviewer.web.VVWebViewClient

class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // todo: 공유로 들어오는 다른 velog 외의 url들 막기
        checkPreference()

        binding.apply {
            webview.apply {
                settings.apply {
                    javaScriptEnabled = true
                    loadWithOverviewMode = true
                    useWideViewPort = true
                    cacheMode = WebSettings.LOAD_DEFAULT
                    textZoom = 95 // Set text size of WebView, Default is 100.
                }
                webViewClient = VVWebViewClient()
                webChromeClient = VVChromeClient()
//                loadUrl(Constants.BASE_VELOG_URL)
            }
        }
    }
//    private fun handleSendImage(intent: Intent) {
//        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            intent.getParcelableExtra(Intent.EXTRA_STREAM, Uri::class.java)?.let {
//                // Update UI to reflect image being shared
//
//            }
//        }
//        else {
//            intent.getParcelableExtra<Parcelable>(Intent.EXTRA_STREAM) as? Uri?.let {
//                // Update UI to reflect image being shared
//            }
//        }
//    }
    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)

        when(intent?.action) {
            Intent.ACTION_SEND -> {
                if(intent.type == "text/plain") {
                    val url = intent.getStringExtra(Intent.EXTRA_TEXT)
                    if(url?.startsWith("https://velog.io/email-login") == true) {
                        binding.webview.loadUrl(url)
                    } else {
                        Log.e(getString(R.string.msg_not_allowed_url))
                        Toast.makeText(this, getString(R.string.msg_not_allowed_url), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun checkPreference() {
        Pref.getInstance(this)?.getString(Pref.USER_ID)?.let { id ->
            if(id.isEmpty()) {
                // input user id
                val binding = LayoutInputIdDialogBinding.inflate(layoutInflater)
                DialogHelper.customDialog(this, R.layout.layout_input_id_dialog) { dlg ->
                    binding.apply {
                        btnOk.setOnClickListener {
                            edtInputId.text.toString().also { inputId ->
                                Pref.getInstance(this@MainActivity)?.setValue(Pref.USER_ID, inputId)
                                userUrl = Constants.BASE_VELOG_URL.plus(inputId)
                            }
                        }
                        btnCancel.setOnClickListener { dlg.dismiss() }
                    }
                }
            }
            else {
                userUrl = Constants.BASE_VELOG_URL.plus(id)
            }
        }
    }
}