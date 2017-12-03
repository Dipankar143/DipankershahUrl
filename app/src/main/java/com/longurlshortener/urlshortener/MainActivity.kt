package com.longurlshortener.urlshortener

import android.app.DownloadManager
import android.app.ProgressDialog
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.widget.Toolbar
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.URLUtil
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import java.net.*
import org.json.JSONException
import org.json.JSONObject
import java.io.*
import java.util.*


class MainActivity : AppCompatActivity() {
    internal var url = "http://gourl.gq/redirect.php"
    internal var drawerButton: ActionBarDrawerToggle? = null
    internal var nav: NavigationView? = null
    internal var dialog: ProgressDialog? = null
    internal var relative: RelativeLayout? = null
    internal var admob: AdView? = null
    internal var adreq: AdRequest? = null
    internal var admob2: AdView? = null
    internal var newAd:InterstitialAd?=null
    internal var adreq2: AdRequest? = null
    var i=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        newAd=InterstitialAd(this);
        newAd!!.setAdUnitId("ca-app-pub-4787614537130897/9408872112")
        newAd!!.loadAd(AdRequest.Builder().build())



        newAd!!.setAdListener(object : AdListener() {
            override fun onAdLoaded() {
                displayAd()
                i++
            }

            override fun onAdClosed() {
                // Load the next interstitial.
                if (i < 2) {
                    newAd!!.loadAd(AdRequest.Builder().build())
                }
            }
        })

        admob = findViewById(R.id.adview) as AdView?
        adreq = AdRequest.Builder().build()
        admob?.loadAd(adreq)
        admob2 = findViewById(R.id.adview2) as AdView?
        adreq2 = AdRequest.Builder().build()
        admob2?.loadAd(adreq)




        relative = findViewById(R.id.relative) as RelativeLayout?


        dialog = ProgressDialog(this) // this = YourActivity
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog!!.setMessage("Loading. Please wait...")
        dialog!!.setIndeterminate(true)
        dialog!!.setCanceledOnTouchOutside(false)

        var toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        var draw = findViewById(R.id.drawer) as DrawerLayout
        drawerButton = ActionBarDrawerToggle(this, draw, R.string.open, R.string.close)
        draw.addDrawerListener(drawerButton!!)
        drawerButton!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        nav = findViewById(R.id.navigation) as NavigationView?
        nav!!.setNavigationItemSelectedListener(object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                var id = item.itemId
                find(id)
                return true

            }
        })


        var edittxt = findViewById(R.id.edit2) as EditText
        edittxt.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, event: MotionEvent): Boolean {
                var newtxt = findViewById(R.id.newtext) as TextView
                var move = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.move)
                edittxt.setAnimation(move)
                edittxt.setGravity(Gravity.CENTER_VERTICAL)
                var fade = AnimationUtils.loadAnimation(applicationContext, R.anim.fade)
                newtxt.setAnimation(fade)
                newtxt.setText("gourl.gq/")
                return false
            }
        })
    }




    private fun displayAd() {
        if (newAd!!.isLoaded()) {
            newAd!!.show()
        }
    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (drawerButton!!.onOptionsItemSelected(item)) {
            return true
        }



        return super.onOptionsItemSelected(item)
    }


    fun find(id: Int) {
        if (id == R.id.create) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        if (id == R.id.track) {

            val intent = Intent(this, track::class.java)
            startActivity(intent)


        }

    }


    fun submit(v: View) {
        var website = findViewById(R.id.edit) as EditText
        var shorted = findViewById(R.id.edit2) as EditText
        var string: String = website.text.toString()
        var string_shoeted = shorted.text.toString()
        var newstring: String
        dialog!!.show()

        if (string.equals("") || string_shoeted.equals("")) {
            var text = findViewById(R.id.website) as TextView
            text.setText("Please Input valid values")
            text.setVisibility(TextView.VISIBLE)
            var img = findViewById(R.id.cpyimg) as ImageView
            img.setVisibility(ImageView.GONE)
            var webShort = findViewById(R.id.mywebsite) as WebView
            webShort.setVisibility(WebView.GONE)
            dialog!!.dismiss()
        } else {

            val stringRequest = object : StringRequest(Request.Method.POST, url, Response.Listener<String> { }, Response.ErrorListener { }

            ) {
                @Throws(AuthFailureError::class)
                override fun getParams(): Map<String, String> {
                    val params = HashMap<String, String>()

                    if (Patterns.WEB_URL.matcher(string).matches() || URLUtil.isValidUrl(string)) {

                        if (string.startsWith("h")) {

                            if (string.substring(string.indexOf("h"), string.indexOf("://")) == "http" || string.substring(string.indexOf("h"), string.indexOf("://")) == "https") {
                                newstring = string.substring(string.indexOf("//") + 2)
                                params.put("website", newstring)
                            }
                        } else {
                            params.put("website", string)
                        }


                        params.put("short", string_shoeted)
                        dialog!!.dismiss()

                    } else {
                        var text = findViewById(R.id.website) as TextView
                        text.setText("Please Input valid url")
                        newAd!!.loadAd(AdRequest.Builder().build())
                        text.setVisibility(TextView.VISIBLE)
                        dialog!!.dismiss()


                    }
                    return params
                }
            }
            val requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(stringRequest)


            var text = findViewById(R.id.website) as TextView
            var anim = AnimationUtils.loadAnimation(this, R.anim.fade)
            relative!!.setVisibility(RelativeLayout.GONE)
            var webShort = findViewById(R.id.mywebsite) as WebView
            webShort.setAnimation(anim)
            webShort.setWebViewClient(WebViewClient())
            if (string.startsWith("h")) {

                if (string.substring(string.indexOf("h"), string.indexOf("://")) == "http" || string.substring(string.indexOf("h"), string.indexOf("://")) == "https") {
                    webShort.loadUrl(string)
                    webShort.setVisibility(WebView.VISIBLE)
                }
            }else {
                    webShort.loadUrl("http://" + string)
                    webShort.setVisibility(WebView.VISIBLE)
                }
            if (Patterns.WEB_URL.matcher(string).matches() || URLUtil.isValidUrl(string)){

            }
            else{
                webShort.setVisibility(WebView.GONE)
            }

                admob2?.loadAd(adreq)
                var face = Typeface.createFromAsset(getAssets(), "Exo-ThinItalic.otf");
                text.setTypeface(face);
                text.setText("http://gourl.gq/" + string_shoeted)
                text.setVisibility(TextView.VISIBLE)
                var img = findViewById(R.id.cpyimg) as ImageView
                img.setVisibility(ImageView.VISIBLE)
            }
        }


        fun copy(v: View) {
            var c = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            var shorted = findViewById(R.id.edit2) as EditText
            var shorted_string = shorted.text.toString()
            var s: String = "http://gourl.gq/" + shorted_string
            var clip = ClipData.newPlainText("copy", s)
            c.setPrimaryClip(clip)
            Toast.makeText(this, "Hidden Website copied to clipboard", Toast.LENGTH_LONG).show()
            newAd!!.loadAd(AdRequest.Builder().build())


        }

    }



