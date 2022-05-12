package eu.deysouvik.orderpizza.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import eu.deysouvik.orderpizza.Adapter.AdapterCart
import eu.deysouvik.orderpizza.R

class CartActivity : AppCompatActivity() {

    companion object{
        lateinit var tv_total:TextView
        lateinit var tv_pizzas:TextView
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tv_total =findViewById(R.id.tv_total_price)
        tv_pizzas =findViewById(R.id.tv_total_pizza)
        viewPizzas()

        tv_total.text=totalPrice(this).toString()
        tv_pizzas.text=totalPizza(this).toString()
    }


    override fun onResume() {
        super.onResume()
        viewPizzas()
        tv_total.text=totalPrice(this).toString()
        tv_pizzas.text=totalPizza(this).toString()
    }


    //connecting database with the recyclerview
    private fun viewPizzas(){
        val pizzalist= MainActivity.dbCart.getPizzas(this)
        val adapter= AdapterCart(this,pizzalist)
        val rv: RecyclerView =findViewById(R.id.rv_cart)
        rv.layoutManager= LinearLayoutManager(this, RecyclerView.VERTICAL,false) as RecyclerView.LayoutManager
        rv.adapter=adapter

    }

    //getting the total price of pizzas in cart

    fun totalPrice(ctx:Context):Int{
        val plist= MainActivity.dbCart.getPizzas(ctx)
        var total=0
        for(i in plist){
            total+=i.Qnt*i.price
        }
        return total
    }

    fun totalPizza(ctx:Context):Int{
        val plist= MainActivity.dbCart.getPizzas(ctx)
        var total=0
        for(i in plist){
            total+=i.Qnt
        }
        return total
    }


}