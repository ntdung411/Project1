package vn.edu.hust.soict.sv.dungnt.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import vn.edu.hust.soict.sv.dungnt.project1.databinding.ActivitySignUpBinding
import vn.edu.hust.soict.sv.dungnt.project1.data.model.UserModel
import vn.edu.hust.soict.sv.dungnt.project1.util.UiUtil

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.signUpButton.setOnClickListener {
            signup()
        }

        binding.goToLoginButton.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.signUpButton.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.signUpButton.visibility = View.VISIBLE
        }
    }

    private fun signup() {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()
        val confirmPassword = binding.confirmPasswordInput.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.error = "Email not valid."
            return
        }
        if (password.length < 6) {
            binding.passwordInput.error = "Minimum 6 character."
            return
        }
        if (password!=confirmPassword) {
            binding.confirmPasswordInput.error = "Password not matched."
            return
        }

        signUpWithFirebase(email, password)
    }

    private fun signUpWithFirebase(email: String, password: String) {
        setInProgress(true)
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            it.user?.let {user->
                val userModel = UserModel(user.uid,email,email.substringBefore("@"))
                Firebase.firestore.collection("users").document(user.uid).set(userModel).addOnSuccessListener {
                    UiUtil.showToast(applicationContext,"Account created successfully.")
                    setInProgress(false)
                    startActivity(Intent(this,LoginActivity::class.java))
                    finish()
                }

            }
        }.addOnFailureListener{
            UiUtil.showToast(applicationContext,it.localizedMessage?: "Something went wrong.")
            setInProgress(false)
        }
    }
}