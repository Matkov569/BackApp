package com.example.backapp

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import com.example.backapp.databinding.ActivityMainBinding
import android.database.Cursor
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        setupPermissions()

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    fun getSMS(): List<String> {
        val sms: MutableList<String> = ArrayList()
        val uriSMSURIin = Uri.parse("content://sms/inbox")
        val cur: Cursor? = contentResolver.query(uriSMSURIin, null, null, null, null)
        while (cur != null && cur.moveToNext()) {
            val address = cur.getString(cur.getColumnIndexOrThrow("address"))
            val body = cur.getString(cur.getColumnIndexOrThrow("body"))
            sms.add("Number: $address .Message: $body")
        }
        if (cur != null) {
            cur.close()
        }
        val uriSMSURIout = Uri.parse("content://sms/inbox")
        val cur2: Cursor? = contentResolver.query(uriSMSURIin, null, null, null, null)
        while (cur2 != null && cur2.moveToNext()) {
            val address = cur2.getString(cur2.getColumnIndexOrThrow("address"))
            val body = cur2.getString(cur2.getColumnIndexOrThrow("body"))
            sms.add("Number: $address .Message: $body")
        }
        if (cur2 != null) {
            cur2.close()
        }



        return sms
    }

    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_SMS)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("PermissionDemo", "Permission to record denied")
        }
        makeRequest()
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.READ_SMS),
                101)
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                             permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            101 -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("PermissionDemo", "Permission has been denied by user")
                } else {
                    Log.i("PermissionDemo", "Permission has been granted by user")
                }
            }
        }
    }

}