package com.example.startup

import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add_notes.*
import java.lang.Exception

class AddNotes : AppCompatActivity() {
    val dTable = "Notes"
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)

        try {
            var bundle:Bundle = intent.extras!!
            id = bundle.getInt("ID", 0)
            if (id > 0) {
                etText.setText(bundle.getString("name"))
                etDes.setText(bundle.getString("des"))
            }
        } catch (ex:Exception){

        }
    }
    fun buAddNotes (view: View) {
        var dbManager = DBManager(this)
        var values = ContentValues()

        values.put("Title", etText.text.toString())
        values.put("Description", etDes.text.toString())
       if (id ==0 ) {
           val ID =  dbManager.Insert(values)

           if (ID > 0) {
               Toast.makeText(this, "Note Sudah ditambahkan !", Toast.LENGTH_SHORT ).show()
               finish()
           } else {
               Toast.makeText(this, "Tidak bisa tambah :(", Toast.LENGTH_SHORT).show()
           }
       } else {
           var selectionArgs = arrayOf(id.toString())
           val ID =  dbManager.Update(values, "ID=?", selectionArgs)
           if (ID > 0) {
               Toast.makeText(this, "Note Sudah ditambahkan !", Toast.LENGTH_SHORT ).show()
               finish()
           } else {
               Toast.makeText(this, "Tidak bisa tambah :(", Toast.LENGTH_SHORT).show()
           }
       }
    }
}
