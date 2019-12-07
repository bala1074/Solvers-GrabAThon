package com.example.myapplication.ui.Receive

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import android.widget.Toast
import androidx.core.app.ActivityCompat

class DashboardFragment : Fragment()  {

    private lateinit var dashboardViewModel: DashboardViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardViewModel =
            ViewModelProviders.of(this).get(DashboardViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_dashboard, container, false)

        val amountView: EditText = root.findViewById(R.id.amount);
        val accountView: EditText = root.findViewById(R.id.account);
        val otpView: EditText = root.findViewById(R.id.otp);

        val submit: TextView = root.findViewById(R.id.Submit);

        submit.setOnClickListener {
            val amount = amountView.text;
            val account = accountView.text;
            val otp = otpView.text;
            Toast.makeText(activity, " "+amount+" "+account+" "+otp, Toast.LENGTH_SHORT).show()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (context?.let { it1 -> ActivityCompat.checkSelfPermission(it1,Manifest.permission.SEND_SMS) } == PackageManager.PERMISSION_DENIED) {

                    Log.d("permission", "permission denied to SEND_SMS - requesting it")
                    val permissions = arrayOf(Manifest.permission.SEND_SMS)

                    requestPermissions(permissions, 1)

                } else {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage("919220592205", null, "PM6PF hello working", null, null)
                }
            }
        }
        return root
    }
}