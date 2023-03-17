package com.jeepchief.velogviewer.view

import android.os.Bundle
import android.webkit.WebSettings
import com.jeepchief.velogviewer.Constants
import com.jeepchief.velogviewer.databinding.ActivityMainBinding
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
//        checkPreference()

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
                loadUrl(Constants.BASE_VELOG_URL.plus("jeep_chief_14"))
            }
        }
    }

    private fun checkPreference() {
        Pref.getInstance(this)?.getString(Pref.USER_ID)?.let { id ->
            if(id.isEmpty()) {
                // input user id
            }
            else {
                userUrl = Constants.BASE_VELOG_URL.plus(id)
            }
        }
    }
}