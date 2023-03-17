package com.jeepchief.velogviewer.view

import android.net.ConnectivityManager
import android.net.Network
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.jeepchief.velogviewer.R
import com.jeepchief.velogviewer.util.DialogHelper
import com.jeepchief.velogviewer.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

open class BaseActivity : AppCompatActivity() {
    companion object {
        const val TIMEOUT = 10L
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val progressDlg = DialogHelper.progressDialog(
            this@BaseActivity,
            getString(R.string.msg_network_on_lost)
        )
        val connectManager = getSystemService(ConnectivityManager::class.java)

        connectManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                Log.e("network onAvailable")
                try {
                    DialogHelper.progressDialog(this@BaseActivity).run {
                        if(isShowing) {
                            dismiss()
                            Toast.makeText(this@BaseActivity, getString(R.string.msg_network_on_available), Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    runOnUiThread {
                        AlertDialog.Builder(this@BaseActivity)
                            .setTitle(e.printStackTrace().toString())
                            .show()
                    }
                }
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                Log.e("network onLost")
                CoroutineScope(Dispatchers.Main).launch {
                    progressDlg.show()
                    delay(TIMEOUT)
                    progressDlg.dismiss()
                    Toast.makeText(this@BaseActivity, getString(R.string.msg_network_finally_lost), Toast.LENGTH_SHORT).show()
                    finishAffinity()
                }
            }
        })
    }
}