package com.devmasterteam.tasks.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.devmasterteam.tasks.service.listener.APIListener
import com.devmasterteam.tasks.service.model.PersonModel
import com.devmasterteam.tasks.service.model.Validation
import com.devmasterteam.tasks.service.model.constants.PersonConstants
import com.devmasterteam.tasks.service.model.constants.TaskConstants
import com.devmasterteam.tasks.service.repository.PersonRepository
import com.devmasterteam.tasks.service.repository.SecurityPreferences
import com.devmasterteam.tasks.service.repository.remote.RetrofitClient

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val personRepository = PersonRepository(application.applicationContext)
    private val securityPreferences = SecurityPreferences(application.applicationContext)

    private val _login = MutableLiveData<Validation>()
    val login:LiveData<Validation> = _login

    private val _loggedUser = MutableLiveData<Boolean>()
    val loggedUser:LiveData<Boolean> = _loggedUser

    /**
     * Faz login usando API
     */
    fun doLogin(email: String, password: String) {
        personRepository.login(email, password, object: APIListener<PersonModel>{
            override fun onResolve(result: PersonModel) {
                securityPreferences.store(TaskConstants.SHARED.TOKEN_KEY, result.token )
                securityPreferences.store(TaskConstants.SHARED.PERSON_KEY, result.personKey )
                securityPreferences.store(TaskConstants.SHARED.PERSON_NAME, result.name )

                RetrofitClient.addHeaders(result.token, result.personKey)

                _login.value = Validation()
            }

            override fun onReject(message: String) {
                _login.value = Validation(message)
            }
        })
    }

    /**
     * Verifica se usuário está logado
     */
    fun verifyLoggedUser() {
        val tokenKey = securityPreferences.get(TaskConstants.SHARED.TOKEN_KEY)
        val personKey = securityPreferences.get(TaskConstants.SHARED.PERSON_KEY)
        val personName = securityPreferences.get(TaskConstants.SHARED.PERSON_NAME)

        RetrofitClient.addHeaders(tokenKey, personKey)

        _loggedUser.value = (tokenKey.isNotEmpty() && personKey.isNotEmpty())
    }

}