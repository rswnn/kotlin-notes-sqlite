package com.example.startup

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.*
import kotlinx.android.synthetic.main.ticket.view.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {

    var listNotes = ArrayList<Note>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        var dbManager = DBManager(this)



        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    fun LoadQuery (title:String) {
        var dbManager = DBManager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionArgs = arrayOf(title)
        val cursor = dbManager.Query(projections,"Title Like ?", selectionArgs,"Title")
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val ID = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val Des = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(Note(ID, Title, Des))
            } while (cursor.moveToNext())
        }

        var myNotesAdapter = MyNotesAdapter(this, listNotes)
        ivNotes.adapter = myNotesAdapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)

        val sv = menu!!.findItem(R.id.appSearch).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager

        sv.setSearchableInfo(sm.getSearchableInfo((componentName)))
        sv.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_LONG).show()
                LoadQuery("%" + query + "%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item != null) {
            when(item.itemId){
                R.id.addNote -> {
                   try {
                       var intent = Intent(this, AddNotes::class.java)
                       startActivity(intent)
                   } catch (ex:Exception) {
                       Toast.makeText(this, "somethink when wrong", Toast.LENGTH_SHORT).show()
                   }
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
    inner class MyNotesAdapter:BaseAdapter {
        var listNotesAdapter = ArrayList<Note>()
        var context:Context?=null
        constructor(context: Context, listNotesAdapter: ArrayList<Note>): super() {
            this.listNotesAdapter = listNotesAdapter
            this.context = context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            var noteView = layoutInflater.inflate(R.layout.ticket, null)
            var myNode = listNotesAdapter[position]

            noteView.tvTitle.text = myNode.nodeName
            noteView.tvDes.text = myNode.nodeDes


            noteView.buDelete.setOnClickListener (View.OnClickListener {
                var dbManager = DBManager(this.context!!)
                val selectionargs = arrayOf(myNode.nodeID.toString())
                dbManager.Delete("ID=?",selectionargs)

                LoadQuery("%")
            })

            noteView.ivEdit.setOnClickListener(View.OnClickListener {
                GoToUpdate(myNode)
            })

            return noteView
        }

        override fun getItem(position: Int): Any {
            return listNotesAdapter[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listNotesAdapter.size
        }
    }

    fun GoToUpdate (note: Note) {
        var intent = Intent(this, AddNotes::class.java)
        intent.putExtra("ID",note.nodeID )
        intent.putExtra("name", note.nodeName)
        intent.putExtra("des", note.nodeDes)

        startActivity(intent)
    }
}
