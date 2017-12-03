package com.longurlshortener.urlshortener

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.widget.DrawerLayout
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.InterstitialAd
import org.json.JSONArray
import org.json.JSONException
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class track : AppCompatActivity() {

    internal var web: WebView?=null
    internal var drawerButton: ActionBarDrawerToggle? = null
    internal var nav: NavigationView?=null
    internal var recy:RecyclerView?=null
    internal var layoutman:RecyclerView.LayoutManager?=null
    internal var adeptor:myadeptr?=null
    internal var arraylist:ArrayList<stringItem>?=null
    internal var dialog:ProgressDialog?=null
    internal var iptxt:TextView?=null
    internal var adreq2: AdRequest? = null
    internal var newAd:InterstitialAd?=null
    var i=0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_track)


        newAd= InterstitialAd(this);
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

        recy= findViewById(R.id.rec) as RecyclerView?
        layoutman= LinearLayoutManager(this) as RecyclerView.LayoutManager?
        recy!!.setHasFixedSize(true)
        recy!!.setLayoutManager(layoutman)
        arraylist= ArrayList()
        adeptor= myadeptr(arraylist,applicationContext)
        recy!!.setAdapter(adeptor)
        iptxt= findViewById(R.id.iptxt) as TextView?
        var admob = findViewById(R.id.adview_track) as AdView?
        var adreq = AdRequest.Builder().build()
        admob?.loadAd(adreq)


        dialog = ProgressDialog(this) // this = YourActivity
        dialog!!.setProgressStyle(ProgressDialog.STYLE_SPINNER)
        dialog!!.setMessage("Loading. Please wait...")
        dialog!!.setIndeterminate(true)
        dialog!!.setCanceledOnTouchOutside(false)





        var edittxt_id=findViewById(R.id.id) as EditText
        edittxt_id.setOnTouchListener(object: View.OnTouchListener{
            override fun onTouch(v: View, event: MotionEvent):Boolean {
                var tracking=findViewById(R.id.tracking) as TextView
                tracking.setText("gourl.gq/")
                return false
            }
        })




        var toolbar= findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        var draw=findViewById(R.id.drawer) as DrawerLayout
        drawerButton= ActionBarDrawerToggle(this,draw,R.string.open,R.string.close)
        draw.addDrawerListener(drawerButton!!)
        drawerButton!!.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        nav= findViewById(R.id.navigation) as NavigationView?
        nav!!.setNavigationItemSelectedListener ( object : NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                var id=item.itemId
                find(id)
                return true

            }
        })


    }



    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        if (drawerButton!!.onOptionsItemSelected(item)){
            return true
        }



        return super.onOptionsItemSelected(item)
    }
    fun find(id:Int){
        if (id==R.id.create)
        {
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        if (id==R.id.track){

            val intent = Intent(this,track::class.java)
            startActivity(intent)


        }

    }



    fun track(v: View) {
        dialog!!.show()
        var text = findViewById(R.id.id) as EditText
        var string = text.text.toString()
        var st = "http://gourl.gq/" + string + "/track"

        if (string.equals("")) {
            iptxt!!.setText("Please Input Tracking Id")
            iptxt!!.setVisibility(TextView.VISIBLE)
            recy!!.setVisibility(RecyclerView.GONE)
            dialog!!.dismiss()
            newAd!!.loadAd(AdRequest.Builder().build())
        } else {
            val jsonArrayRequest = JsonArrayRequest(Request.Method.POST, st, null as String?, Response.Listener<JSONArray> { response ->
                var count = 0
                var i = response.length()
                arraylist!!.clear()

                try {
                    while (count < response.length()) {


                        val jsonObject = response.getJSONObject(i - 1)
                         var Ip = stringItem(jsonObject.getString("ip"))
                        arraylist!!.add(Ip)
                        i--
                        count++
                    }
                    adeptor!!.notifyDataSetChanged()
                    dialog!!.dismiss()
                    if (count == 0 && i == 0) {
                        iptxt!!.setText("no one visit your site!!")
                    } else {
                        iptxt!!.setText("IP who visit your website!!")
                        newAd!!.loadAd(AdRequest.Builder().build())
                        recy!!.setVisibility(RecyclerView.VISIBLE)
                    }

                    iptxt!!.setVisibility(TextView.VISIBLE)
                } catch (e: JSONException) {
                    Toast.makeText(applicationContext, "error", Toast.LENGTH_LONG)
                    e.printStackTrace()
                }
            },
                    Response.ErrorListener {


                        iptxt!!.setText("Tracking ID not Found\n \t\tOr\n no one visit your site")
                        iptxt!!.setVisibility(TextView.VISIBLE)
                        recy!!.setVisibility(RecyclerView.GONE)
                        dialog!!.dismiss()
                        newAd!!.loadAd(AdRequest.Builder().build())

                    })

            var requestQueue = Volley.newRequestQueue(this)
            requestQueue.add(jsonArrayRequest)


        }
    }

    private fun displayAd() {
        if (newAd!!.isLoaded()) {
            newAd!!.show()
        }
    }





}






