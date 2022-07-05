package com.example.projectpartner

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.coroutineScope
import com.example.projectpartner.databinding.LoginFragmentBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collect
import java.lang.Exception

class LoginFragment : Fragment(R.layout.login_fragment) {

    companion object{
        private const val RC_SIGN_IN = 120
    }

    private lateinit var binding:LoginFragmentBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignIn: GoogleSignInClient
    lateinit var viewModel:MyViewModel
    private lateinit var userDb : DatabaseReference
    private lateinit var database : DatabaseReference


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        viewModel = ViewModelProvider(this)[MyViewModel::class.java]
        binding = LoginFragmentBinding.inflate(inflater,container,false)
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignIn = GoogleSignIn.getClient(requireActivity(), gso)

        auth = FirebaseAuth.getInstance()

        binding.googlebtn.setOnClickListener {
            signIn()
        }



        binding.loginBtn.setOnClickListener {
            if(binding.emaileditlayout.text.toString().trim().isEmpty()){
                binding.emaileditlayout.error = "Required"
                Toast.makeText(activity,"Email Required",Toast.LENGTH_LONG).show()
            }else if(binding.passwordedittext.text.toString().trim().isEmpty()){
                binding.passwordedittext.error = "Required"
                Toast.makeText(activity,"Password Required",Toast.LENGTH_LONG).show()
            }else {
                viewModel.login(binding.emaileditlayout.text.toString(), binding.passwordedittext.text.toString())
                closeKeyboard()
            }
        }

        lifecycle.coroutineScope.launchWhenCreated {
            viewModel.loginUI.collect{
                when(it){
                    is MyViewModel.LoginUIController.loading->{
                        binding.progress.visibility= View.VISIBLE
                    }
                    is MyViewModel.LoginUIController.success->{
                        binding.progress.visibility= View.GONE
                        startActivity(Intent(activity,ProfileActivity::class.java))
                        requireActivity().finish()
                        Snackbar.make(binding.root,"Authenticated", Snackbar.LENGTH_LONG).show()
                    }
                    is MyViewModel.LoginUIController.error->{
                        binding.progress.visibility= View.GONE
                        Snackbar.make(binding.root,it.msg, Snackbar.LENGTH_LONG).show()

                    }
                    else -> {

                    }
                }
            }
        }

    }

    private fun signIn() {
        val signInIntent = googleSignIn.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            val exception =task.exception

            if (task.isSuccessful){
                try {
                    // Google Sign In was successful, authenticate with Firebase
                    val account = task.getResult(ApiException::class.java)!!
                    Log.d("main", "firebaseAuthWithGoogle:" + account.id)
                    firebaseAuthWithGoogle(account.idToken!!)
                } catch (e: ApiException) {
                    // Google Sign In failed, update UI appropriately
                    Log.w("main", "Google sign in failed", e)
                }
            }else{
                Log.w("main", "Google sign in failed", exception)

            }
        }
    }
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    saveUserToFirebaseDatabase()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("TAG", "signInWithCredential:failure", task.exception)
                }
            }
    }


    private fun closeKeyboard() {
        val imm = view?.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }


    private fun saveUserToFirebaseDatabase() {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val currentUser = auth.currentUser
        database = FirebaseDatabase.getInstance().getReference("/Users/$uid")

        val firstName = currentUser?.displayName
        val email = currentUser?.email
        val profileImageUrl = currentUser?.photoUrl

        val user = User(uid, firstName, email, profileImageUrl.toString())
        database.setValue(user)
            .addOnSuccessListener {
                //moving to home activity
                val i = Intent(context,TellUsMoreActivity::class.java)
                startActivity(i)
                Toast.makeText(context,"Successfully Added",Toast.LENGTH_SHORT).show()
                requireActivity().finish()
            }.addOnFailureListener{
                Toast.makeText(context,"Not Added",Toast.LENGTH_SHORT).show()

            }
    }

  /*  fun Login(){

        binding.loginBtn.setOnClickListener {

            auth = FirebaseAuth.getInstance()

            val email = binding.emaileditlayout.text.toString()
            val password = binding.passwordedittext.text.toString()

            if(binding.emaileditlayout.text.toString().trim().isEmpty()){
                binding.emaileditlayout.error = "Required"
                Toast.makeText(activity,"Email Required",Toast.LENGTH_LONG).show()
            }else if(binding.passwordedittext.text.toString().trim().isEmpty()){
                binding.passwordedittext.error = "Required"
                Toast.makeText(activity,"Password Required",Toast.LENGTH_LONG).show()
            }else{

                auth.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    if (it.isSuccessful){
                        startActivity(Intent(activity,ProfileActivity::class.java))
                        activity?.finish()
                        Toast.makeText(context,"Success", Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"ERROR", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener {
                    Toast.makeText(activity,it.message.toString(), Toast.LENGTH_SHORT).show()
                }
            }

        }

    } */


}