package vn.edu.hust.soict.sv.dungnt.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Patterns
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import vn.edu.hust.soict.sv.dungnt.project1.databinding.ActivityLoginBinding
import vn.edu.hust.soict.sv.dungnt.project1.ui.MainActivity
import vn.edu.hust.soict.sv.dungnt.project1.util.UiUtil

class LoginActivity : AppCompatActivity() {

    private lateinit var binding : ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        FirebaseAuth.getInstance().currentUser?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.loginButton.setOnClickListener {
            login()
        }
        binding.goToSignUpButton.setOnClickListener {
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }
    }

    private fun setInProgress(inProgress: Boolean) {
        if (inProgress) {
            binding.progressBar.visibility = View.VISIBLE
            binding.loginButton.visibility = View.GONE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.loginButton.visibility = View.VISIBLE
        }
    }

    private fun login() {
        val email = binding.emailInput.text.toString()
        val password = binding.passwordInput.text.toString()

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.emailInput.error = "Email not valid."
            return
        }
        if (password.length < 6) {
            binding.passwordInput.error = "Minimum 6 character."
            return
        }

        loginWithFirebase(email, password)
    }

    private fun loginWithFirebase(email: String, password: String) {
        setInProgress(true)
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnSuccessListener {
            UiUtil.showToast(this,"Login successfully.")
            setInProgress(false)
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }.addOnFailureListener{
            UiUtil.showToast(applicationContext,it.localizedMessage?: "Something went wrong.")
            setInProgress(false)
        }
    }
}