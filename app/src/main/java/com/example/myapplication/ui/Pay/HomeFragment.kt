package com.example.myapplication.ui.Pay

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.myapplication.R
import android.os.CountDownTimer




class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val textView: TextView = root.findViewById(R.id.text_home)
        homeViewModel.text.observe(this, Observer {
            textView.text = it
        })
        val textViewTimer: TextView = root.findViewById(R.id.text_home2)
        homeViewModel.text.observe(this, Observer {
            textViewTimer.text = it
        })

        val twoFactorAuthUtil = TwoFactorAuthUtil()

        // String base32Secret = twoFactorAuthUtil.generateBase32Secret();
        val base32Secret = "NY4A5CPJZ46LXZCP"

        println("secret = $base32Secret")

        // this is the name of the key which can be displayed by the authenticator
        // program
        val keyId = "user@j256.com"
        // generate the QR code
        //System.out.println("Image url = " + twoFactorAuthUtil.qrImageUrl(keyId, base32Secret))
        // we can display this image to the user to let them load it into their auth
        // program

        // we can use the code here and compare it against user input
        var code = twoFactorAuthUtil.generateCurrentNumber(base32Secret)

        /*
		 * this loop shows how the number changes over time
		 */

        object : CountDownTimer(300000000000000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val diff =
                    TwoFactorAuthUtil.TIME_STEP_SECONDS - System.currentTimeMillis() / 1000 % TwoFactorAuthUtil.TIME_STEP_SECONDS
                code = twoFactorAuthUtil.generateCurrentNumber(base32Secret)
                textView.setText("OTP: "+code)
                textViewTimer.setText("OTP expires in "+diff);
            }

            override fun onFinish() {
                textView.setText("done!")
            }
        }.start()

        return root
    }
}