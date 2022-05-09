package eu.deysouvik.orderpizza.Data

object CartData {
   fun getIndex(name:String):Int{
       if(name== PizzaData.crust1_size1 +"-"+ PizzaData.crust1 +"  pizza"){return 1}
       else if(name== PizzaData.crust1_size2 +"-"+ PizzaData.crust1 +"  pizza"){return 2}
       else if(name== PizzaData.crust1_size3 +"-"+ PizzaData.crust1 +"  pizza"){return 3}
       else if(name== PizzaData.crust2_size1 +"-"+ PizzaData.crust2 +"  pizza"){return 4}
       return 5
   }

}