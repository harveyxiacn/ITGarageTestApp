package itgarage.itgaragetestapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harvey Xia on 2014/12/5.
 */
public class DBHandler extends SQLiteOpenHelper{

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "memberManager",
                                   TABLE_CONTACTS = "members",
                                   KEY_ID = "id",
                                   KEY_NAME = "name",
                                   KEY_PHONE = "phone",
                                   KEY_EAMIL = "email",
                                   KEY_ADDRESS = "address";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_CONTACTS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_PHONE + " TEXT," + KEY_EAMIL + " TEXT," + KEY_ADDRESS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CONTACTS);

        onCreate(db);
    }

    public void createMember(Member member){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, member.get_name());
        values.put(KEY_PHONE, member.get_phone());
        values.put(KEY_EAMIL, member.get_email());
        values.put(KEY_ADDRESS, member.get_address());

        db.insert(TABLE_CONTACTS, null, values);
        db.close();
    }

    public Member getMember(int id){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTACTS, new String[] { KEY_ID, KEY_NAME, KEY_PHONE, KEY_EAMIL, KEY_ADDRESS}, KEY_ID + "=?", new String[] { String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Member member = new Member(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4));
        cursor.close();
        db.close();
        return member;
    }

    public void deleteMember(Member member){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + "=?", new String[] { String.valueOf(member.get_id())});
        db.close();
    }

    public int getMembersCount(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM "+ TABLE_CONTACTS, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        return count;
    }

    public int updateMember(Member member){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, member.get_name());
        values.put(KEY_PHONE, member.get_phone());
        values.put(KEY_EAMIL, member.get_email());
        values.put(KEY_ADDRESS, member.get_address());
        int rowAffected = db.update(TABLE_CONTACTS, values, KEY_ID + "=?", new String[]{String.valueOf(member.get_id())});
        db.close();

        return rowAffected;
    }

    public List<Member> getAllMembers(){
        List<Member> members = new ArrayList<Member>();

        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_CONTACTS, null);

        if (cursor.moveToFirst()){
            do
                members.add(new Member(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
            while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return members;
    }
}
