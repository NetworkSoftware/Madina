package pro.yalu.network.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import pro.yalu.network.product.ProductBean;

import java.util.ArrayList;


/**
 * Created by ravi on 15/03/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "Genius_db2";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create Mainbeans table
        db.execSQL(ProductBean.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        // db.execSQL("ALTER TABLE "+Mainbean.TABLE_NAME+" ADD COLUMN "+Mainbean.COLUMN_INCLUDE_GST+" TEXT");
        db.execSQL("DROP TABLE IF EXISTS " + ProductBean.TABLE_NAME);
        // Create tables again
        onCreate(db);
    }

    public boolean isInCart(String id, String userid) {
        if (userid.length() > 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            String qry = "Select *  from " + ProductBean.TABLE_NAME + " where " + ProductBean.COLUMN_PRO_ID + " = " + id + " AND " + ProductBean.USER_ID + " = " + userid;
            Cursor cursor = db.rawQuery(qry, null);
            cursor.moveToFirst();
            return cursor.getCount() > 0;
        } else {
            return false;
        }
    }

    public long insertMainbean(ProductBean mainbean, String quantity, String userId) {
        if (userId.length() > 0) {


            if (isInCart(mainbean.id, userId)) {
                mainbean.setQty("1");
                updateMainbean(mainbean, userId);
            } else {
                // get writable database as we want to write data
                SQLiteDatabase db = this.getWritableDatabase();

                ContentValues values = new ContentValues();
                // `id` and `timestamp` will be inserted automatically.
                // no need to add them
                values.put(ProductBean.COLUMN_NAME, mainbean.getName());
                values.put(ProductBean.USER_ID, userId);
                values.put(ProductBean.COLUMN_PRO_ID, mainbean.getId());
                values.put(ProductBean.COLUMN_DETAIL, mainbean.getDetail());
                values.put(ProductBean.COLUMN_IMAGES, mainbean.getImages());
                values.put(ProductBean.COLUMN_PRODUCTRUPEEFINAL, mainbean.getProductrupeefinal());
                values.put(ProductBean.COLUMN_PRODUCTRUPEEMRP, mainbean.getProductrupeemrp());
                values.put(ProductBean.COLUMN_CART, mainbean.getCart());
                values.put(ProductBean.COLUMN_SUBCATEGORY, mainbean.getSubCategory());
                values.put(ProductBean.COLUMN_SUB_CAT_STATUS, mainbean.getSub_cat_status());
                values.put(ProductBean.COLUMN_TAX, mainbean.getTax());
                values.put(ProductBean.COLUMN_QTY, quantity);
                values.put(ProductBean.COLUMN_P_NO, mainbean.getP_no());
                values.put(ProductBean.COLUMN_CURRENCY, mainbean.getCurrency());
                values.put(ProductBean.COLUMN_OFFER, mainbean.getOffer());
                values.put(ProductBean.COLUMN_IMG1, mainbean.getImg1());
                values.put(ProductBean.COLUMN_IMG2, mainbean.getImg2());
                values.put(ProductBean.COLUMN_IMG3, mainbean.getImg3());
                values.put(ProductBean.COLUMN_IMG4, mainbean.getImg4());
                values.put(ProductBean.COLUMN_IMG4, mainbean.getImg4());
                values.put(ProductBean.COLUMN_WEIGHT, mainbean.getWeight());
                values.put(ProductBean.COLUMN_IMG6, mainbean.getImg6());
                values.put(ProductBean.COLUMN_TAX_TYPE, mainbean.getTax_type());
                values.put(ProductBean.COLUMN_DESCRIPTION, mainbean.getDescription());
                values.put(ProductBean.COLUMN_VIDEO, mainbean.getVideo());


                // insert row
                long id = db.insert(ProductBean.TABLE_NAME, null, values);

                // close db connection
                db.close();
            }


            // return newly inserted row id
            return 1;
        } else {
            return 0;

        }
    }

    public ProductBean getproductbean(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(ProductBean.TABLE_NAME,
                new String[]{
                        ProductBean.COLUMN_ID,
                        ProductBean.COLUMN_NAME,
                        ProductBean.COLUMN_DETAIL,
                        ProductBean.COLUMN_IMAGES,
                        ProductBean.COLUMN_PRODUCTRUPEEFINAL,
                        ProductBean.COLUMN_PRODUCTRUPEEMRP,
                        ProductBean.COLUMN_CART,
                        ProductBean.COLUMN_SUBCATEGORY,
                        ProductBean.COLUMN_SUB_CAT_STATUS,
                        ProductBean.COLUMN_TAX,
                        ProductBean.COLUMN_QTY,
                        ProductBean.COLUMN_P_NO,
                        ProductBean.COLUMN_CURRENCY,
                        ProductBean.COLUMN_OFFER,
                        ProductBean.COLUMN_IMG1,
                        ProductBean.COLUMN_IMG2,
                        ProductBean.COLUMN_IMG3,
                        ProductBean.COLUMN_IMG4,
                        ProductBean.COLUMN_IMG5,
                        ProductBean.COLUMN_IMG6,
                        ProductBean.COLUMN_DESCRIPTION,
                        ProductBean.COLUMN_VIDEO,
                        ProductBean.COLUMN_WEIGHT,
                        ProductBean.COLUMN_TAX_TYPE},
                ProductBean.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare Mainbean object
        ProductBean productBean = new ProductBean(
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_NAME)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_DETAIL)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMAGES)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_PRODUCTRUPEEFINAL)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_PRODUCTRUPEEMRP)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_CART)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_SUBCATEGORY)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_SUB_CAT_STATUS)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_TAX)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_QTY)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_P_NO)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_CURRENCY)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_OFFER)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG1)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG2)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG3)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG4)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG5)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG6)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_TAX_TYPE)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_DESCRIPTION)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_VIDEO)),
                cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_WEIGHT))
        );

        // close the db connection
        cursor.close();

        return productBean;
    }

    public ArrayList<ProductBean> getAllMainbeans(String userid) {
        if (userid.length() <= 0) {
            return new ArrayList<>();
        }
        ArrayList<ProductBean> productBeans = new ArrayList<>();


        String selectQuery = "SELECT  * FROM " + ProductBean.TABLE_NAME + " WHERE " + ProductBean.USER_ID + " = " + userid;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ProductBean productBean = new ProductBean();
                productBean.setId(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_PRO_ID)));
                productBean.setName(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_NAME)));
                productBean.setDetail(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_DETAIL)));
                productBean.setImages(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMAGES)));
                productBean.setProductrupeefinal(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_PRODUCTRUPEEFINAL)));
                productBean.setProductrupeemrp(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_PRODUCTRUPEEMRP)));
                productBean.setCart(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_CART)));
                productBean.setSubCategory(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_SUBCATEGORY)));
                productBean.setSub_cat_status(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_SUB_CAT_STATUS)));
                productBean.setTax(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_TAX)));
                productBean.setQty(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_QTY)));
                productBean.setP_no(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_P_NO)));
                productBean.setCurrency(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_CURRENCY)));
                productBean.setOffer(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_OFFER)));
                productBean.setImg1(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG1)));
                productBean.setImg2(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG2)));
                productBean.setImg3(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG3)));
                productBean.setImg4(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG4)));
                productBean.setImg5(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG5)));
                productBean.setImg6(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_IMG6)));
                productBean.setTax_type(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_TAX_TYPE)));
                productBean.setDescription(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_DESCRIPTION)));
                productBean.setVideo(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_VIDEO)));
                productBean.setWeight(cursor.getString(cursor.getColumnIndex(ProductBean.COLUMN_WEIGHT)));

                productBeans.add(productBean);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return Mainbeans list
        return productBeans;
    }

    public int getMainbeansCount() {
        String countQuery = "SELECT  * FROM " + ProductBean.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();


        // return count
        return count;
    }

    public int updateMainbean(ProductBean productBean, String userid) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(ProductBean.COLUMN_NAME, productBean.getName());
        values.put(ProductBean.USER_ID, userid);
        values.put(ProductBean.COLUMN_DETAIL, productBean.getDetail());
        values.put(ProductBean.COLUMN_IMAGES, productBean.getImages());
        values.put(ProductBean.COLUMN_PRODUCTRUPEEFINAL, productBean.getProductrupeefinal());
        values.put(ProductBean.COLUMN_PRODUCTRUPEEMRP, productBean.getProductrupeemrp());
        values.put(ProductBean.COLUMN_CART, productBean.getCart());
        values.put(ProductBean.COLUMN_SUBCATEGORY, productBean.getSubCategory());
        values.put(ProductBean.COLUMN_SUB_CAT_STATUS, productBean.getSub_cat_status());
        values.put(ProductBean.COLUMN_TAX, productBean.getTax());
        values.put(ProductBean.COLUMN_QTY, productBean.getQty());
        values.put(ProductBean.COLUMN_P_NO, productBean.getP_no());
        values.put(ProductBean.COLUMN_CURRENCY, productBean.getCurrency());
        values.put(ProductBean.COLUMN_OFFER, productBean.getOffer());
        values.put(ProductBean.COLUMN_IMG1, productBean.getImg1());
        values.put(ProductBean.COLUMN_IMG2, productBean.getImg2());
        values.put(ProductBean.COLUMN_IMG3, productBean.getImg3());
        values.put(ProductBean.COLUMN_IMG4, productBean.getImg4());
        values.put(ProductBean.COLUMN_IMG5, productBean.getImg5());
        values.put(ProductBean.COLUMN_IMG6, productBean.getImg6());
        values.put(ProductBean.COLUMN_WEIGHT, productBean.getWeight());
        values.put(ProductBean.COLUMN_TAX_TYPE, productBean.getTax_type());
        values.put(ProductBean.COLUMN_DESCRIPTION, productBean.getDescription());
        values.put(ProductBean.COLUMN_VIDEO, productBean.getVideo());

        // updating row
        return db.update(ProductBean.TABLE_NAME, values, ProductBean.COLUMN_PRO_ID + " = ?",
                new String[]{String.valueOf(productBean.getId())});
    }

    public void deleteMainbean(ProductBean productBean) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductBean.TABLE_NAME, ProductBean.COLUMN_PRO_ID + " = ?",
                new String[]{String.valueOf(productBean.getId())});
        db.close();
    }

    public void deleteAll(String userid) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(ProductBean.TABLE_NAME, ProductBean.USER_ID + " = ?",
                new String[]{userid});
        db.close();
    }
}
