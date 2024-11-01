package com.example.team25.ui.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.team25.databinding.ActivityLoginEntryBinding
import com.example.team25.ui.main.MainActivity
import com.example.team25.ui.register.RegisterEntryActivity
import com.example.team25.ui.register.RegisterStatusActivity
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoginEntryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginEntryBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginEntryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkAutoLogin()
        navigateToMain()
        setKakaoLoginBtnClickListener()
        observeLoginState()
    }

    private fun checkAutoLogin() {
        lifecycleScope.launch {
            val tokens = loginViewModel.getSavedTokens()
            if (tokens != null && tokens.accessToken.isNotEmpty()) {
                navigateToRegisterStatus()
            }
        }
    }

    private fun navigateToRegisterStatus() {
        val intent = Intent(this, RegisterStatusActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun navigateToMain() {
        binding.appNameTextView.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    private fun navigateToRegisterEntry() {
        val intent = Intent(this, RegisterEntryActivity::class.java)
        startActivity(intent)
    }

    private fun setKakaoLoginBtnClickListener() {
        binding.kakaoLoginBtn.setOnClickListener {
            handleKakaoLogin()
        }
    }

    private fun observeLoginState() {
        lifecycleScope.launch {
            loginViewModel.loginState.collect { state ->
                when (state) {
                    is LoginState.Loading -> {
                        // 로딩 상태 처리 (프로그레스바 표시)
                    }

                    is LoginState.Success -> {
                        Log.i(TAG, "로그인 성공")
                    }

                    is LoginState.Error -> {
                        binding.loginErrorTextView.text = state.message
                    }

                    LoginState.Idle -> {}
                }
            }
        }
    }

    private fun handleKakaoLogin() {
        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                Log.e(TAG, "카카오 계정으로 로그인 실패", error)
                loginViewModel.updateErrorMessage("카카오 로그인 실패")
            } else if (token != null) {
                Log.i(TAG, "카카오 계정으로 로그인 성공 ${token.accessToken}")
                fetchUserInfo(token.accessToken)
            }
        }

        if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
            UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                if (error != null) {
                    Log.e(TAG, "카카오톡으로 로그인 실패", error)

                    if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                        return@loginWithKakaoTalk
                    }

                    UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                } else if (token != null) {
                    Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                    fetchUserInfo(token.accessToken)
                }
            }
        } else {
            UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
        }
    }

    private fun fetchUserInfo(accessToken: String) {
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
                loginViewModel.updateErrorMessage("사용자 정보 요청 실패")
            } else if (user != null) {
                Log.i(TAG, "사용자 정보 요청 성공: ${user.kakaoAccount?.profile?.nickname}, ${user.id}")

                loginViewModel.login(accessToken)
                Log.d(TAG, accessToken)
                navigateToRegisterEntry()
            }
        }
    }

    companion object {
        private const val TAG = "kakaoLogin"
    }
}
