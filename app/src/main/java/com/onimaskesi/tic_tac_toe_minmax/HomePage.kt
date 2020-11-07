package com.onimaskesi.tic_tac_toe_minmax

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import kotlinx.android.synthetic.main.activity_home_page.*

class HomePage : AppCompatActivity() {

    lateinit var _userName : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
    }

    fun buttonXclick(view : View){
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("user",'X')
        intent.putExtra("userName",_userName)
        startActivity(intent)
        finish()
    }

    fun buttonOclick(view : View){
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.putExtra("user",'O')
        intent.putExtra("userName",_userName)
        startActivity(intent)
        finish()
    }

    fun buttonGoClick(view: View){


        if(userName.text.toString() == null || userName.text.toString() == ""){
            userName.setText("USER")
        }
        _userName = userName.text.toString()


        userName.isEnabled = false
        userName.textSize = 25f
        userName.setTextColor(Color.parseColor("#ffffff"))
        goButton.visibility = View.GONE
        chooseYourSide.visibility = View.VISIBLE

    }
}
