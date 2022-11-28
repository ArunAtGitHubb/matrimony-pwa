package com.jellysoft.matrimony

import android.content.DialogInterface;
import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View
import android.webkit.WebView;
import android.webkit.WebViewClient;
import androidx.appcompat.app.AlertDialog.Builder

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;


class MainActivity : AppCompatActivity() {
    var websiteURL = "https://jellysofthub.com/mano/matrimony/" // sets web url
    private lateinit var webview: WebView
    var mySwipeRefreshLayout: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (!CheckNetwork.isInternetAvailable(this)) //returns true if internet available
        {
            //if there is no internet do this
            setContentView(R.layout.activity_main)
            //Toast.makeText(this,"No Internet Connection, Chris",Toast.LENGTH_LONG).show();
            Builder(this) //alert the person knowing they are about to close
                .setTitle("No internet connection available")
                .setMessage("Please Check you're Mobile data or Wifi network.")
                .setPositiveButton("Ok",
                    DialogInterface.OnClickListener { dialog, which -> finish() }) //.setNegativeButton("No", null)
                .show()
        } else {
            webview = findViewById(R.id.webView)
            webview.settings.javaScriptEnabled = true
            webview.settings.domStorageEnabled = true
            webview.overScrollMode = WebView.OVER_SCROLL_NEVER
            webview.loadUrl(websiteURL)
            webview.webViewClient = WebViewClientDemo()
        }

        //Swipe to refresh functionality
        mySwipeRefreshLayout = findViewById<View>(R.id.swipeContainer) as SwipeRefreshLayout
        mySwipeRefreshLayout!!.setOnRefreshListener { webview!!.reload() }
    }

    private inner class WebViewClientDemo : WebViewClient() {
        //Keep webview in app when clicking links
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            view.loadUrl(url)
            return true
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            mySwipeRefreshLayout!!.isRefreshing = false
        }
    }

    //set back button functionality
    override fun onBackPressed() { //if user presses the back button do this
        if (webview.isFocused && webview.canGoBack()) { //check if in webview and the user can go back
            webview.goBack() //go back in webview
        } else { //do this if the webview cannot go back any further
            Builder(this) //alert the person knowing they are about to close
                .setTitle("EXIT")
                .setMessage("Are you sure. You want to close this app?")
                .setPositiveButton("Yes",
                    DialogInterface.OnClickListener { dialog, which -> finish() })
                .setNegativeButton("No", null)
                .show()
        }
    }
}