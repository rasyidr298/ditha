package id.ditha.pcs.ui.login

import android.content.Context
import id.ditha.pcs.data.network.MyApi
import id.ditha.pcs.data.network.NetworkConnectionInterceptor
import id.ditha.pcs.data.repository.AppRepository
import id.ditha.pcs.data.response.AuthResponse
import id.ditha.pcs.utils.ApiException
import id.ditha.pcs.utils.BaseViewModel
import id.ditha.pcs.utils.Coroutines
import id.ditha.pcs.utils.NoInternetException
import okhttp3.RequestBody
import java.io.IOException


class AuthViewModel(context: Context): BaseViewModel<AuthViewModel.LoginState>() {

    private val networkConnectionInterceptor = NetworkConnectionInterceptor(context)
    private val api = MyApi(networkConnectionInterceptor)
    private val repository = AppRepository(api)

    sealed class LoginState{
        data class Loading(val isLoading:Boolean) : LoginState()
        data class Succes(val user : AuthResponse) : LoginState()
        data class Error(val message :String) : LoginState()
    }

    fun loginUser(dataLogin: HashMap<String, RequestBody>) {
        Coroutines.main {
            _state.value= (LoginState.Loading(true))

            try {
                val authResponse = repository.userLogin(dataLogin)
                authResponse.let {
                    _state.value= (LoginState.Succes(it))
                    return@main
                }

            } catch (e: ApiException) {
                _state.value= (LoginState.Error(e.message.toString()))
            } catch (e: NoInternetException) {
                _state.value= (LoginState.Error(e.message.toString()))
            }catch (e: IOException){
                _state.value= (LoginState.Error(e.message.toString()))
            }
        }
    }
}