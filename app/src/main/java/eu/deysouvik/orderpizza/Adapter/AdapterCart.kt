package eu.deysouvik.orderpizza.Adapter

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import eu.deysouvik.orderpizza.Activity.CartActivity
import eu.deysouvik.orderpizza.Activity.MainActivity
import eu.deysouvik.orderpizza.Data.CartData
import eu.deysouvik.orderpizza.Model.Pizza
import eu.deysouvik.orderpizza.R
import kotlinx.android.synthetic.main.pizza_detail.view.*

class AdapterCart(val mCtx: Context, val pizzas:ArrayList<Pizza>): RecyclerView.Adapter<AdapterCart.ViewHolder>() {

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        val tv_name=itemView.pizza_name
        val tv_qnt=itemView.pizza_qnt
        val btn_delete=itemView.delete_btn
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val v = LayoutInflater.from(p0.context).inflate(R.layout.pizza_detail,p0,false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
          val pizza=pizzas[p1]
          p0.tv_name.text=pizza.name
          p0.tv_qnt.text=pizza.Qnt.toString()


          p0.btn_delete.setOnClickListener {

              val alertDialog= AlertDialog.Builder(mCtx)
              alertDialog.setTitle("Are you sure to remove this from Cart")
              alertDialog.setIcon(R.drawable.warning_icon)
              alertDialog.setMessage("your current Cart list is:"+"\n"+getPizzaList(mCtx))
              alertDialog.setPositiveButton("Yes", DialogInterface.OnClickListener { dialog, which->
                  var q=pizza.Qnt
                  if(q>1){
                      q=q-1
                      val id= CartData.getIndex(pizza.name)
                      val res= MainActivity.dbCart.updatePizza(id.toString(),pizza.name,q,pizza.price)
                      if(res){
                          pizzas[p1].Qnt=q
                          pizzas[p1].name=pizza.name
                          pizzas[p1].price=pizza.price
                          notifyDataSetChanged()
                          CartActivity.tv_total.text=totalPrice(mCtx).toString()
                          CartActivity.tv_pizzas.text=totalPizza(mCtx).toString()
                      }

                  }
                  else{
                      q=q-1
                      val id= CartData.getIndex(pizza.name)
                      val res= MainActivity.dbCart.updatePizza(id.toString(),pizza.name,q,pizza.price)
                      if(res){
                          pizzas.removeAt(p1)
                          notifyItemRemoved(p1)
                          notifyDataSetChanged()
                          CartActivity.tv_total.text=totalPrice(mCtx).toString()
                          CartActivity.tv_pizzas.text=totalPizza(mCtx).toString()
                      }
                  }

              })
              alertDialog.setNegativeButton("No", DialogInterface.OnClickListener { dialog, which ->   })
              alertDialog.show()


          }
    }

    override fun getItemCount(): Int {
        return pizzas.size
    }



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

    fun getPizzaList(ctx:Context):String{
        var s=""
        val plist= MainActivity.dbCart.getPizzas(ctx)
        for(i in plist){
            s+="${i.Qnt}  ${i.name}"+"\n"
        }
        return s
    }
}