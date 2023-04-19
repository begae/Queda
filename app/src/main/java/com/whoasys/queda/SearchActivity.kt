package com.whoasys.queda

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient

class SearchActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val webView = findViewById<WebView>(R.id.webView)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(BridgeInterface(), "Android")
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                //Android 에서 Javascript 호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();")
            }
        }

        //최초 웹뷰 로드
        webView.loadUrl("http://118.67.129.26:8080/address")
    }


    inner class BridgeInterface{
        @JavascriptInterface
        fun processDATA(data: String) {
            //카카오 주소 검색 API의 결과 값이 브릿지 통로를 통해 전달 받는다. (from javascript)
            val intent = Intent()
            intent.putExtra("data", data)
            setResult(RESULT_OK, intent)
            finish()
        }
    }

}