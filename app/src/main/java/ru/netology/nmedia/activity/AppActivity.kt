package ru.netology.nmedia.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.firebase.messaging.FirebaseMessaging
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.NewPostFragment.Companion.textArg

class AppActivity : AppCompatActivity(R.layout.activity_app) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        intent?.let {//Если intent не равно null, код внутри блока let будет выполнен
            if (it.action != Intent.ACTION_SEND) { //Проверяет, является ли действие intent экшеном ACTION_SEND
                return@let //Если нет, выполнение блока let прерывается с помощью return@let
            }
            val text = it.getStringExtra(Intent.EXTRA_TEXT) //Извлекает дополнительные данные из intent с помощью ключа Intent.EXTRA_TEXT и сохраняет их в переменной text.
            if (text?.isNotBlank() != true) { //Проверяет, является ли text непустой строкой.
                return@let //Если нет, выполнение блока let прерывается с помощью return@let.
            }
            intent.removeExtra(Intent.EXTRA_TEXT) //Удаляет данные с ключом Intent.EXTRA_TEXT из intent, чтобы предотвратить повторное использование данных при последующих вызовах onCreate (например, при изменении конфигурации).
            findNavController(R.id.nav_host_fragment) //Находит NavController, связанный с NavHostFragment, используя идентификатор R.id.nav_host_fragment.
                .navigate( //вызывает метод navigate для NavController, чтобы перейти к новому фрагменту
                    R.id.action_feedFragment_to_newPostFragment, //на основе указанного идентификатора действия R.id.action_feedFragment_to_newPostFragment.
                    Bundle().apply {//В качестве аргумента передается новый объект Bundle, в котором устанавливается значение textArg.
                        textArg = text
                    }
                )
        }
        checkGoogleApiAvailability()
    }

    private fun checkGoogleApiAvailability() {
        with(GoogleApiAvailability.getInstance()) {
            val code = isGooglePlayServicesAvailable(this@AppActivity)
            if (code == ConnectionResult.SUCCESS) {
                return@with
            }
            if (isUserResolvableError(code)) {
                getErrorDialog(this@AppActivity, code, 9000)?.show()
                return
            }
            Toast.makeText(this@AppActivity, R.string.google_play_unavailable, Toast.LENGTH_LONG)
                .show()
        }

        FirebaseMessaging.getInstance().token.addOnSuccessListener {
            println(it)
        }
    } /*Функция проверяет доступность сервисов Google Play и выводит пользователю сообщение, если они недоступны.
    А также извлекает токен обмена сообщениями Firebase и печатает его. */
}