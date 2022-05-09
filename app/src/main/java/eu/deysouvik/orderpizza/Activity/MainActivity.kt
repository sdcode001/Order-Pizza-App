package eu.deysouvik.orderpizza.Activity

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import eu.deysouvik.orderpizza.DataBase.DBCart
import eu.deysouvik.orderpizza.Data.CartData
import eu.deysouvik.orderpizza.Data.PizzaData
import eu.deysouvik.orderpizza.Model.Pizza
import eu.deysouvik.orderpizza.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object{
        private var crust=""
        private var size=""
        lateinit var dbCart: DBCart
        lateinit var pref:SharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        dbCart = DBCart(this,null,null,1)


        //adding pizza values to the dbcart database only for one time when app starts for the first time

        pref =getSharedPreferences("prefs", MODE_PRIVATE)
        val firstStart= pref.getBoolean("firstStart",true)
        if(firstStart){
            val pizz1= Pizza(PizzaData.crust1_size1+"-"+ PizzaData.crust1+"  pizza",0, PizzaData.crust1_size1_price)
            val pizz2= Pizza(PizzaData.crust1_size2+"-"+ PizzaData.crust1+"  pizza",0, PizzaData.crust1_size2_price)
            val pizz3= Pizza(PizzaData.crust1_size3+"-"+ PizzaData.crust1+"  pizza",0, PizzaData.crust1_size3_price)
            val pizz4= Pizza(PizzaData.crust2_size1+"-"+ PizzaData.crust2+"  pizza",0, PizzaData.crust2_size1_price)
            val pizz5= Pizza(PizzaData.crust2_size2+"-"+ PizzaData.crust2+"  pizza",0, PizzaData.crust2_size2_price)
            dbCart.addPizza(pizz1)
            dbCart.addPizza(pizz2)
            dbCart.addPizza(pizz3)
            dbCart.addPizza(pizz4)
            dbCart.addPizza(pizz5)

            val pref=getSharedPreferences("prefs", MODE_PRIVATE)
            val editor=pref.edit()
            editor.putBoolean("firstStart",false)
            editor.apply()
        }


        btn_cart.setOnClickListener {
            val intent=Intent(this, CartActivity::class.java)
            startActivity(intent)

        }






        add_btn.setOnClickListener {
          val dialog= AlertDialog.Builder(this)
            val view=layoutInflater.inflate(R.layout.add_dialog,null)
            val crust_radio_group=view.findViewById<RadioGroup>(R.id.crust_radio_group)
            val size_radio_group=view.findViewById<RadioGroup>(R.id.size_radio_group)
            val tv_price=view.findViewById<TextView>(R.id.tv_price)
            val add_cart_btn=view.findViewById<Button>(R.id.add_cart_btn)
            val cancel_btn=view.findViewById<Button>(R.id.cancel_btn)
            val size1_btn=view.findViewById<RadioButton>(R.id.size1_btn)
            val size2_btn=view.findViewById<RadioButton>(R.id.size2_btn)
            val size3_btn=view.findViewById<RadioButton>(R.id.size3_btn)

            dialog.setView(view)
            val alertdialog=dialog.create()
            alertdialog.setCanceledOnTouchOutside(false)
            crust = PizzaData.crust1
            size = PizzaData.crust1_size2
            crust_radio_group.setOnCheckedChangeListener { radioGroup, checked_id ->
                try {
                    if(checked_id== R.id.crust1_btn){
                        tv_price.text= PizzaData.crust1_size2_price.toString()
                        crust = PizzaData.crust1
                        size2_btn.isChecked=true
                        size3_btn.visibility=View.VISIBLE
                        size1_btn.text= PizzaData.crust1_size1.toString()+" ₹"+ PizzaData.crust1_size1_price.toString()
                        size2_btn.text= PizzaData.crust1_size2.toString()+" ₹"+ PizzaData.crust1_size2_price.toString()
                        size3_btn.text= PizzaData.crust1_size3.toString()+" ₹"+ PizzaData.crust1_size3_price.toString()
                    }
                    else{
                        size1_btn.isChecked=true
                        crust = PizzaData.crust2
                        size = PizzaData.crust2_size1
                        size1_btn.text= PizzaData.crust2_size1.toString()+" ₹"+ PizzaData.crust2_size1_price.toString()
                        size2_btn.text= PizzaData.crust2_size2.toString()+" ₹"+ PizzaData.crust2_size2_price.toString()
                        size3_btn.visibility=View.GONE
                        tv_price.text= PizzaData.crust2_size1_price.toString()
                    }
                }catch (e:Exception){
                    Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
                }
            }

            size_radio_group.setOnCheckedChangeListener { radioGroup, checkedId ->

                if(crust == PizzaData.crust1){
                    if(checkedId== R.id.size1_btn){
                        size = PizzaData.crust1_size1
                        tv_price.text= PizzaData.crust1_size1_price.toString()
                    }
                    else if(checkedId== R.id.size2_btn){
                        size = PizzaData.crust1_size2
                        tv_price.text= PizzaData.crust1_size2_price.toString()
                    }
                    else{
                        size = PizzaData.crust1_size3
                        tv_price.text= PizzaData.crust1_size3_price.toString()
                    }
                }
                else{

                    if(checkedId== R.id.size1_btn){
                        size = PizzaData.crust2_size1
                        tv_price.text= PizzaData.crust2_size1_price.toString()
                        Toast.makeText(this, "yes", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        size = PizzaData.crust2_size2
                        tv_price.text= PizzaData.crust2_size2_price.toString()
                    }
                }
            }



            add_cart_btn.setOnClickListener {
                val id= CartData.getIndex(size +"-"+ crust +"  pizza")
                val pizzaData= dbCart.getPizzaDetail(id)
                var q=pizzaData.Qnt
                q=q+1
                val result= dbCart.updatePizza(id.toString(),pizzaData.name,q,pizzaData.price)
                if(result){
                    Toast.makeText(this, "Pizza added to Cart", Toast.LENGTH_LONG).show()
                }

                alertdialog.dismiss()
            }


            cancel_btn.setOnClickListener {
                alertdialog.dismiss()
            }

            alertdialog.show()
        }








    }


}