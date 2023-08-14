package ru.netology.nmedia.activity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.nmedia.databinding.FragmentSignInBinding
import ru.netology.nmedia.viewmodel.AuthViewModel

@AndroidEntryPoint
class SignInFragment : DialogFragment() {
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var auth: FirebaseAuth // Объявляем FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSignInBinding.inflate(
            inflater,
            container,
            false
        )
        auth = FirebaseAuth.getInstance() // Инициализируем FirebaseAuth
        authViewModel.error.observe(viewLifecycleOwner) { error ->
            Toast.makeText(requireContext(), "Ошибка: $error", Toast.LENGTH_SHORT).show()
        }
        binding.authorizeButton.setOnClickListener {
            // Получаем значения логина и пароля из полей ввода
            val email = binding.login.text.toString()
            val password = binding.password.text.toString()
            // Проверяем, что поля не пустые
            if (email.isNotEmpty() && password.isNotEmpty()) {
                signIn(email, password) // Вызываем метод аутентификации
            } else {
                // Если поля пустые, показываем сообщение об ошибке
                Toast.makeText(requireContext(), "Введите логин и пароль", Toast.LENGTH_SHORT).show()
            }
        }
        return binding.root
    }
    private fun signIn(email: String, password: String) { // Метод аутентификации
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) { // Аутентификация прошла успешно
                    authViewModel.updateUser(email, password) // Обновляем информацию о пользователе в ViewModel
                    authViewModel.state.observe(viewLifecycleOwner) {
                        if (it != null) {
                            findNavController().navigateUp()
                        }
                    }
                    authViewModel.authorized = true // Пользователь авторизован
                } else { // Неудачная аутентификация
                    Toast.makeText(requireContext(), "Ошибка входа.", Toast.LENGTH_SHORT).show()
                    authViewModel.authorized = false // Пользователь не авторизован
                }
            }
    }
}