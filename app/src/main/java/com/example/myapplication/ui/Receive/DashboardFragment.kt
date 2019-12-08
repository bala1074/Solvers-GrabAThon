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
import android.R.attr.key
import java.security.MessageDigest
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec


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

        var prev :String="";
        submit.setOnClickListener {
            val amount = amountView.text;
            val account = accountView.text;
            val otp = otpView.text;
            //val details = encryptMthd(""+amount+" "+account+" "+otp);
            val details = ""+amount+" "+account+" "+otp
            if(!(amount.toString().length>0 && account.toString().length>0 && otp.toString().length>0)){
                Toast.makeText(activity, "Please fill the details properly", Toast.LENGTH_LONG).show()
            }
            else if(prev.equals(otp.toString()))
            {
                Toast.makeText(activity, "OTP alredy used!!", Toast.LENGTH_LONG).show()
            }
            else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                if (context?.let { it1 -> ActivityCompat.checkSelfPermission(it1,Manifest.permission.SEND_SMS) } == PackageManager.PERMISSION_DENIED) {

                    Log.d("permission", "permission denied to SEND_SMS - requesting it")
                    val permissions = arrayOf(Manifest.permission.SEND_SMS)

                    requestPermissions(permissions, 1)

                } else {
                    val smsManager = SmsManager.getDefault()
                    smsManager.sendTextMessage("919220592205", null, "PM6PF *"+details, null, null)
                    Toast.makeText(activity, "Payment triggered successfully!!", Toast.LENGTH_LONG).show()
                    prev = otp.toString();
                }
            }

        }
        return root
    }

    fun encryptMthd(msg: String): String {
        val encryptedMsg = encrypt(msg, "abcd")
        var str = ""
        var start = true
        for (`val` in encryptedMsg) {
            if (start) {
                str = "" + `val`
                start = false
            } else
                str = "$str,$`val`"
        }
        return str
    }


    @Throws(Exception::class)
    fun encrypt(plainText: String, key: String): ByteArray {
        val clean = plainText.toByteArray()

        // Generating IV.
        val ivSize = 16
        val iv = ByteArray(ivSize)
        val random = SecureRandom()
        random.nextBytes(iv)
        val ivParameterSpec = IvParameterSpec(iv)

        // Hashing key.
        val digest = MessageDigest.getInstance("SHA-256")
        digest.update(key.toByteArray(charset("UTF-8")))
        println(key.toByteArray(charset("UTF-8")))
        val keyBytes = ByteArray(16)
        System.arraycopy(digest.digest(), 0, keyBytes, 0, keyBytes.size)
        val secretKeySpec = SecretKeySpec(keyBytes, "AES")

        // Encrypt.
        val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec)
        val encrypted = cipher.doFinal(clean)

        // Combine IV and encrypted part.
        val encryptedIVAndText = ByteArray(ivSize + encrypted.size)
        System.arraycopy(iv, 0, encryptedIVAndText, 0, ivSize)
        System.arraycopy(encrypted, 0, encryptedIVAndText, ivSize, encrypted.size)

        return encryptedIVAndText
    }

}