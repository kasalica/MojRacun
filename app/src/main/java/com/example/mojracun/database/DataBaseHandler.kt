package com.example.mojracun.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.mojracun.receipt.Item
import com.example.mojracun.receipt.Receipt
import com.example.mojracun.receipt.Seller

val DATABASE_NAME = "DB"
val DATABASE_VERSION = 1

val COL_ID = "id"

val RECEIPTS_TABLE_NAME = "receipts"
val COL_IIC = "iic"
val COL_PRICE = "price"
val COL_DATE = "dateTimeCreated"
val COL_SELLER_ID = "seller_id"

val SELLERS_TABLE_NAME = "sellers"
val COL_SELLER_PIB = "idNum"
val COL_SELLER_NAME = "name"
val COL_SELLER_ADDRESS = "address"
val COL_SELLER_TOWN = "town"
val COL_SELLER_RECEIPTS_SUM = "sellerReceiptsSum"

val ITEMS_TABLE_NAME = "items"
val COL_ITEM_NAME = "name"
val COL_ITEM_QUANTITY = "quantity"
val COL_ITEM_PRICE_BEFOREVAT = "price_beforeVat"
val COL_ITEM_PRICE_AFTERVAT = "price_afterVat"
val COL_ITEM_VATRATE = "vatrate"
val COL_RECEIPT_ID = "receipt_id"


class DataBaseHandler(context: Context?, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION) {

    var receipt = Receipt()

    override fun onCreate(db: SQLiteDatabase?) {

        val createSellersTable = "CREATE TABLE " + SELLERS_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_SELLER_PIB + " INTEGER NOT NULL UNIQUE," +
                COL_SELLER_NAME + " VARCHAR(256)," +
                COL_SELLER_ADDRESS + " VARCHAR(256)," +
                COL_SELLER_TOWN + " VARCHAR(256)," +
                COL_SELLER_RECEIPTS_SUM + " DOUBLE)"
        db?.execSQL(createSellersTable)

        val createReceiptsTable = "CREATE TABLE " + RECEIPTS_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_IIC + " VARCHAR(256) NOT NULL UNIQUE," +
                COL_DATE + " VARCHAR(256)," +
                COL_PRICE + " VARCHAR(256)," +
                COL_SELLER_ID + " INTEGER," +
                " FOREIGN KEY ("+ COL_SELLER_ID+") REFERENCES "+ SELLERS_TABLE_NAME+"("+COL_ID+") ON DELETE CASCADE)";
        db?.execSQL(createReceiptsTable)

        val createItemsTable = "CREATE TABLE " + ITEMS_TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_ITEM_NAME + " VARCHAR(256)," +
                COL_ITEM_QUANTITY + " VARCHAR(256)," +
                COL_ITEM_PRICE_AFTERVAT + " VARCHAR(256)," +
                COL_ITEM_PRICE_BEFOREVAT + " VARCHAR(256)," +
                COL_ITEM_VATRATE + " VARCHAR(256)," +
                COL_RECEIPT_ID + " INTEGER," +
                " FOREIGN KEY ("+ COL_RECEIPT_ID+") REFERENCES "+ RECEIPTS_TABLE_NAME+"("+COL_ID+") ON DELETE CASCADE)";
        db?.execSQL(createItemsTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun insertReceipt(receipt: Receipt) {

        val db = this.writableDatabase

        ////INSERTING ///
        val receipt_iic = receipt.iic
        val queryReceipt = "Select * from " + RECEIPTS_TABLE_NAME + " WHERE " + COL_IIC + "='$receipt_iic'"
        val resultReceipt = db.rawQuery(queryReceipt, null)
        if (resultReceipt.moveToFirst()) {
            do {
                break
            } while (resultReceipt.moveToNext())
        } else {

            ///inserting SELLER data//////
            var seller_idNum = receipt.seller.idNum

            val querySeller = "Select * from " + SELLERS_TABLE_NAME + " WHERE " + COL_SELLER_PIB + "='$seller_idNum'"
            val resultSeller = db.rawQuery(querySeller, null)
            if (resultSeller.moveToFirst()) {
                do {
                    var receiptsSum = receipt.totalPrice.toDouble() + resultSeller.getDouble(resultSeller.getColumnIndex(COL_SELLER_RECEIPTS_SUM))
                    db.execSQL("UPDATE " + SELLERS_TABLE_NAME +
                            " SET " + COL_SELLER_RECEIPTS_SUM + " = $receiptsSum" +
                            " WHERE " + COL_SELLER_PIB + "='$seller_idNum'")
                    break
                } while (resultSeller.moveToNext())
            } else {
                db.execSQL("INSERT INTO " + SELLERS_TABLE_NAME +
                        "(" + COL_SELLER_PIB + "," +
                        COL_SELLER_NAME + "," +
                        COL_SELLER_ADDRESS + "," +
                        COL_SELLER_RECEIPTS_SUM + "," +
                        COL_SELLER_TOWN + ")" +
                        " VALUES('" +
                        receipt.seller.idNum + "','" +
                        receipt.seller.name + "','" +
                        receipt.seller.address + "','" +
                        receipt.totalPrice.toDouble() + "','" +
                        receipt.seller.town + "')"
                )
            }

            //inserting RECEIPT data
            db.execSQL("INSERT INTO " + RECEIPTS_TABLE_NAME +
                    "(" + COL_IIC + "," +
                    COL_DATE + "," +
                    COL_PRICE + "," +
                    COL_SELLER_ID + ")" +
                    "VALUES('" +
                    receipt.iic + "','" +
                    receipt.dateTimeCreated + "','" +
                    receipt.totalPrice + "'," +
                    "(SELECT " + COL_ID +
                    " FROM " + SELLERS_TABLE_NAME +
                    " WHERE " + COL_SELLER_NAME + "=?))", arrayOf(receipt.seller.name))

            //insertin ITEMS data
            for (item in receipt.items ){
                db.execSQL("INSERT INTO " + ITEMS_TABLE_NAME +
                        "(" + COL_ITEM_NAME +"," +
                        COL_ITEM_QUANTITY +"," +
                        COL_ITEM_PRICE_AFTERVAT +"," +
                        COL_ITEM_PRICE_BEFOREVAT +"," +
                        COL_ITEM_VATRATE +"," +
                        COL_RECEIPT_ID +")" +
                        "VALUES('" +
                        item.name + "','" +
                        item.quantity + "','" +
                        item.priceAfterVat + "','" +
                        item.priceBeforeVat + "','" +
                        item.vatRate + "'," +
                        "(SELECT " + COL_ID +
                        " FROM " + RECEIPTS_TABLE_NAME +
                        " WHERE " + COL_DATE+ "=?))", arrayOf(receipt.dateTimeCreated))
            }
        }

        db.close()
    }

    fun readReceipt(iic: String): Receipt{

        var receipt = Receipt()

        val db = this.readableDatabase

        val query1 = "Select * from " + RECEIPTS_TABLE_NAME + " WHERE " + COL_IIC + "='$iic'"
        val result1 = db.rawQuery(query1, null)
        if (result1.moveToFirst()) {
            receipt.iic = result1.getString(result1.getColumnIndex(COL_IIC))
            receipt.totalPrice = result1.getString(result1.getColumnIndex(COL_PRICE))
            receipt.dateTimeCreated = result1.getString(result1.getColumnIndex(COL_DATE))
        }
        val seller_id = result1.getInt(result1.getColumnIndex(COL_SELLER_ID))
        val receipt_id = result1.getInt(result1.getColumnIndex(COL_ID))
        result1.close()

        val query2 = "Select * from " + SELLERS_TABLE_NAME + " WHERE " + COL_ID + "=$seller_id"
        val result2 = db.rawQuery(query2, null)
        if (result2.moveToFirst()) {
            receipt.seller = Seller()
            receipt.seller.idNum = result2.getInt(result2.getColumnIndex(COL_SELLER_PIB))
            receipt.seller.name = result2.getString(result2.getColumnIndex(COL_SELLER_NAME))
            receipt.seller.address = result2.getString(result2.getColumnIndex(COL_SELLER_ADDRESS))
            receipt.seller.town = result2.getString(result2.getColumnIndex(COL_SELLER_TOWN))
            receipt.seller.receiptsSum = result2.getFloat(result2.getColumnIndex(COL_SELLER_RECEIPTS_SUM))
        }
        result2.close()


        val query3 = "Select * from " + ITEMS_TABLE_NAME + " WHERE " + COL_RECEIPT_ID + "=$receipt_id"
        val result3 = db.rawQuery(query3, null)
        var i = 0
        var items: MutableList<Item> = ArrayList()
        var item = Item()
        if (result3.moveToFirst()) {
            do {
                item = Item()
                item.name = result3.getString(result3.getColumnIndex(COL_ITEM_NAME))
                item.quantity = result3.getString(result3.getColumnIndex(COL_ITEM_QUANTITY))
                item.priceAfterVat = result3.getString(result3.getColumnIndex(COL_ITEM_PRICE_AFTERVAT))
                item.priceBeforeVat = result3.getString(result3.getColumnIndex(COL_ITEM_PRICE_BEFOREVAT))
                item.vatRate = result3.getString(result3.getColumnIndex(COL_ITEM_VATRATE))
                items.add(item)
            }
            while (result3.moveToNext())
        }
        receipt.items = items.toTypedArray()
        result3.close()
        db.close()
        return receipt
    }

    fun readSellersData(): MutableList<Seller>{
        var sellers: MutableList<Seller> = ArrayList()

        val db = this.readableDatabase

        val query =
                "Select * from " + SELLERS_TABLE_NAME + " ORDER BY " + COL_SELLER_RECEIPTS_SUM + " DESC"
        val result = db.rawQuery(query, null)
        if (result.moveToFirst()){
            do{
                var row = Seller();
                row.idNum = result.getInt(result.getColumnIndex(COL_SELLER_PIB))
                row.name = result.getString(result.getColumnIndex(COL_SELLER_NAME))
                row.town = result.getString(result.getColumnIndex(COL_SELLER_TOWN))
                row.address = result.getString(result.getColumnIndex(COL_SELLER_ADDRESS))
                row.receiptsSum = result.getFloat(result.getColumnIndex(COL_SELLER_RECEIPTS_SUM))

                sellers.add(row)

            } while (result.moveToNext())
        }
        result.close()
        db.close()
        return sellers
    }

    fun readReceiptsData(TableName: String): MutableList<Receipt>{

        var receipts: MutableList<Receipt> = ArrayList()

        val db = this.readableDatabase

        val query1 =
                "Select * from " + RECEIPTS_TABLE_NAME +
                        " INNER JOIN " + SELLERS_TABLE_NAME +
                        " ON " + RECEIPTS_TABLE_NAME+"."+COL_SELLER_ID + "=" + SELLERS_TABLE_NAME+"."+COL_ID
        val result1 = db.rawQuery(query1, null)
        if (result1.moveToFirst()) {
            do {
                var row = Receipt()
                row.totalPrice = result1.getString(result1.getColumnIndex(COL_PRICE))
                row.dateTimeCreated = result1.getString(result1.getColumnIndex(COL_DATE))
                row.iic = result1.getString(result1.getColumnIndex(COL_IIC))

                row.seller = Seller()
                row.seller.name = result1.getString(result1.getColumnIndex(COL_SELLER_NAME))
                row.seller.receiptsSum = result1.getFloat(result1.getColumnIndex(COL_SELLER_RECEIPTS_SUM))


                receipts.add(row)
            } while (result1.moveToNext())
        }

        result1.close()
        db.close()
        return receipts
    }

    fun deleteData() {
        val db = this.writableDatabase
        db.delete(RECEIPTS_TABLE_NAME, null, null)
        db.close()
    }



}