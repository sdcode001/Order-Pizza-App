package eu.deysouvik.orderpizza.DataBase

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import eu.deysouvik.orderpizza.Model.Pizza

class DBCart(context: Context, name:String?, factory: SQLiteDatabase.CursorFactory?, version:Int):
    SQLiteOpenHelper(context, DATABASE_NAME,factory, DATABASE_VERSION){



    companion object{
        private val DATABASE_NAME="CartData.db"
        private val DATABASE_VERSION=1

        val PIZZA_TABLE_NAME="Pizzas"
        val COLUMN_PIZZAID="Pizzaid"
        val COLUMN_PIZZANAME="Pizzaname"
        val COLUMN_PIZZAQNT="Pizzaquantity"
        val COLUMN_PIZZACOST="PIzzacost"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_CUSTOMERS_TABLE:String=("CREATE TABLE $PIZZA_TABLE_NAME ("+
                "$COLUMN_PIZZAID INTEGER PRIMARY KEY AUTOINCREMENT,"+
                "$COLUMN_PIZZANAME TEXT,"+
                "$COLUMN_PIZZAQNT INTEGER DEFAULT 0,"+
                "$COLUMN_PIZZACOST INTEGER DEFAULT 0)")
        db?.execSQL(CREATE_CUSTOMERS_TABLE)



    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun addPizza(pizza: Pizza){
        val values= ContentValues()
        values.put(COLUMN_PIZZANAME,pizza.name)
        values.put(COLUMN_PIZZAQNT,pizza.Qnt)
        values.put(COLUMN_PIZZACOST,pizza.price)
        val db=this.writableDatabase

        try{
            db.insert(PIZZA_TABLE_NAME,null,values)

        }catch (e:Exception){

        }
        db.close()

    }


    fun getPizzaDetail(id:Int): Pizza {
        val qry="Select * From $PIZZA_TABLE_NAME"
        val db=this.readableDatabase
        val cursor=db.rawQuery(qry,null)
        val p= Pizza()
        while(cursor.moveToNext()){
            if(cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZAID).toInt())==id){
                p.name=cursor.getString(cursor.getColumnIndex(COLUMN_PIZZANAME).toInt())
                p.Qnt=cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZAQNT).toInt())
                p.price=cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZACOST).toInt())
                break
            }
        }
        cursor.close()
        db.close()
        return p
    }


    fun getPizzas(mCtx:Context):ArrayList<Pizza>{
        val qry="Select * From $PIZZA_TABLE_NAME"
        val db=this.readableDatabase
        val cursor=db.rawQuery(qry,null)
        val pizzas=ArrayList<Pizza>()

        if(cursor.count==0){
            Toast.makeText(mCtx, "No Pizza in Cart!", Toast.LENGTH_SHORT).show()
        }
        else{
            while(cursor.moveToNext()){
                if(cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZAQNT).toInt())>0){
                    val p= Pizza()
                    p.name=cursor.getString(cursor.getColumnIndex(COLUMN_PIZZANAME).toInt())
                    p.Qnt=cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZAQNT).toInt())
                    p.price=cursor.getInt(cursor.getColumnIndex(COLUMN_PIZZACOST).toInt())
                    pizzas.add(p)
                }
            }
        }
        cursor.close()
        db.close()
        return pizzas
    }


    fun updatePizza(id:String,name:String,qnt:Int,price:Int):Boolean{
        var result=false
        val db=this.writableDatabase
        val contentValues=ContentValues()
        contentValues.put(COLUMN_PIZZANAME,name)
        contentValues.put(COLUMN_PIZZAQNT,qnt)
        contentValues.put(COLUMN_PIZZACOST,price)

        try {
            db.update(PIZZA_TABLE_NAME,contentValues,"$COLUMN_PIZZAID = ?", arrayOf(id))
            result=true
        }catch (e:Exception){
            Log.e(ContentValues.TAG,"Error Updating!")
        }
        return result
    }





}