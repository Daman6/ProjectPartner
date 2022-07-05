package com.example.projectpartner

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.projectpartner.databinding.RegisterFragmentBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class RegisterFragment : Fragment(R.layout.register_fragment) {

    private lateinit var binding : RegisterFragmentBinding
    private lateinit var database : DatabaseReference
    private lateinit var auth : FirebaseAuth
    private lateinit var storage : FirebaseStorage


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = RegisterFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.registerBtn.setOnClickListener {
            binding.progress.visibility = View.VISIBLE
            register()
        }

        binding.selectImagebtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type ="image/*"
            startActivityForResult(intent,0)
        }


    }
    var selectedUri : Uri? = null
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){

            selectedUri = data.data
//            val bitmap = MediaStore.Images.Media.getBitmap(context?.contentResolver,selectedUri)
//            val bitmapDrawable = BitmapDrawable(bitmap)
            Glide.with(this).load(selectedUri).into(binding.selectImagebtn)
        }
    }
        fun register(){
            auth = FirebaseAuth.getInstance()

            val email = binding.emaileditlayout.text.toString()
            val password = binding.passwordedittext.text.toString()


            if(binding.emaileditlayout.text.toString().trim().isEmpty()){
                binding.emaileditlayout.error = "Required"
                binding.progress.visibility = View.INVISIBLE
                Toast.makeText(activity,"Email Required",Toast.LENGTH_LONG).show()
            }else if(binding.passwordedittext.text.toString().trim().isEmpty()){
                binding.passwordedittext.error = "Required"
                binding.progress.visibility = View.INVISIBLE
                Toast.makeText(activity,"Password Required",Toast.LENGTH_LONG).show()
            }else if(binding.firstnameeditlayout.text.toString().trim().isEmpty()){
                binding.firstnameeditlayout.error = "Required"
                binding.progress.visibility = View.INVISIBLE
                Toast.makeText(activity,"Name Required",Toast.LENGTH_LONG).show()
            }else{
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener {
                        if (it.isSuccessful) {
                            uploadUserImageToDatabase()
                        }
                    }
            }
            }



    private fun uploadUserImageToDatabase() {
        if (selectedUri == null) return

        val fileName = UUID.randomUUID().toString()

        val ref = FirebaseStorage.getInstance().getReference("/images/$fileName")

        ref.putFile(selectedUri!!)
            .addOnSuccessListener {
                Toast.makeText(context,"Successfully Added",Toast.LENGTH_SHORT).show()

                ref.downloadUrl.addOnSuccessListener {
                    saveUserToFirebaseDatabase(it.toString())
                }
            }.addOnFailureListener{
                binding.progress.visibility = View.GONE

                Toast.makeText(context,it.message.toString(),Toast.LENGTH_SHORT).show()
            }

    }

    private fun saveUserToFirebaseDatabase(profileImageUrl :String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""

        database = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        val firstName = binding.firstnameeditlayout.text.toString()
        val email = binding.emaileditlayout.text.toString()

        val user = User(uid, firstName, email, profileImageUrl)
        database.setValue(user)
            .addOnSuccessListener {
                //moving to home activity
                binding.progress.visibility = View.GONE

                val i = Intent(context,TellUsMoreActivity::class.java)
                startActivity(i)
                Toast.makeText(context,"Successfully Added",Toast.LENGTH_SHORT).show()

                requireActivity().finish()
            }.addOnFailureListener{
                binding.progress.visibility = View.GONE

                Toast.makeText(context,"Not Added",Toast.LENGTH_SHORT).show()
            }
    }


}
