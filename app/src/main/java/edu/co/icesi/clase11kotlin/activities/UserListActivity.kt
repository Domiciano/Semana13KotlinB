package edu.co.icesi.clase11kotlin.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

import co.domi.clase10.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import edu.co.icesi.clase11kotlin.R
import kotlinx.android.synthetic.main.activity_user_list.*
import java.util.*

class UserListActivity : AppCompatActivity() {

    private var db = FirebaseFirestore.getInstance()

    private lateinit var myUser: User
    private lateinit var userArrayAdapter: ArrayAdapter<User>
    private lateinit var users: ArrayList<User>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        //Configuracion de la lista
        users = ArrayList()
        userArrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, users)
        userList.adapter = userArrayAdapter

        //Recuperar el user de la actividad pasada
        myUser = intent.extras?.getSerializable("myUser") as User

        //Listar usuarios
        db = FirebaseFirestore.getInstance()
        val userReference = db.collection("users").orderBy("username").limit(10)
        userReference.get().addOnCompleteListener { task: Task<QuerySnapshot> ->
            task.result?.let {
                users.clear()
                for (doc in task.result!!) {
                    val user = doc.toObject(User::class.java)
                    users.add(user)
                }
                userArrayAdapter.notifyDataSetChanged()
            }
        }
        //Habilitar los clicks a items de la lista
        userList.setOnItemClickListener(::onItemClick)
    }

    //Se activa cuando damos click a un item de la lista
    fun onItemClick(parent: AdapterView<*>?, view: View, position: Int, id: Long) {
        val userClicked = users[position]
        val intent = Intent(this, ChatActivity::class.java)
        intent.putExtra("myUser", myUser)
        intent.putExtra("userClicked", userClicked)
        startActivity(intent)
    }
}