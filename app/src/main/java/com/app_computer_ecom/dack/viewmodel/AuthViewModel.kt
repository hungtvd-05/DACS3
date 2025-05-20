package com.app_computer_ecom.dack.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.app_computer_ecom.dack.AppUtil
import com.app_computer_ecom.dack.model.UserModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException

class AuthViewModel(private val application: Application) : AndroidViewModel(application) {
    private val auth = Firebase.auth
    private val firestore = Firebase.firestore
    private val credentialManager = CredentialManager.create(application.applicationContext)

    var user by mutableStateOf(auth.currentUser?.takeIf { it.isEmailVerified })
        private set

    var userModel by mutableStateOf<UserModel?>(null)
        private set

    init {
        auth.addAuthStateListener { firebaseAuth ->
            val currentUser = firebaseAuth.currentUser
            user = currentUser?.takeIf { it.isEmailVerified }
            if (user != null) {
                getUserModel()
            } else {
                userModel = null
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val result = auth.signInWithEmailAndPassword(email, password).await()
                val firebaseUser = result.user
                if (firebaseUser?.isEmailVerified == true) {
                    onResult(true, null)
                } else {
                    auth.signOut()
                    onResult(false, "Email chưa được xác thực. Vui lòng kiểm tra hộp thư của bạn.")
                }
            } catch (e: Exception) {
                onResult(false, e.localizedMessage)
            }
        }
    }

    fun changePassword(
        currentPassword: String,
        newPassword1: String,
        newPassword2: String,
        context: Context,
        onResult: (Boolean, String?) -> Unit
    ) {
        if (currentPassword.isEmpty()) {
            AppUtil.showToast(context, "Vui lòng nhập mật khẩu hiện tại")
        } else if (newPassword1.isEmpty()) {
            AppUtil.showToast(context, "Vui lòng nhập mật khẩu mới")
        } else if (newPassword1 != newPassword2) {
            AppUtil.showToast(context, "Mật khẩu mới không khớp")
        } else if (currentPassword == newPassword2) {
            AppUtil.showToast(context, "Mật khẩu mới không được trùng với mật khẩu hiện tại")
        } else {
            viewModelScope.launch {
                try {
                    // Xác thực lại người dùng với mật khẩu hiện tại
                    val credential =
                        EmailAuthProvider.getCredential(user!!.email!!, currentPassword)
                    user!!.reauthenticate(credential).await()

                    // Đổi mật khẩu
                    user!!.updatePassword(newPassword1).await()
                    AppUtil.showToast(context, "Đổi mật khẩu thành công")
                    onResult(true, "Đổi mật khẩu thành công.")
                } catch (e: Exception) {
                    when (e) {
                        is FirebaseAuthWeakPasswordException -> {
                            onResult(
                                false,
                                "Mật khẩu mới quá yếu. Vui lòng chọn mật khẩu mạnh hơn."
                            )
                        }

                        is FirebaseAuthInvalidCredentialsException -> {
                            onResult(false, "Mật khẩu hiện tại không đúng.")
                        }

                        else -> {
                            onResult(false, e.localizedMessage ?: "Lỗi khi đổi mật khẩu.")
                        }
                    }
                }
            }
        }
    }

    fun loginWithUsername(
        username: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documents = firestore.collection("users")
                    .whereEqualTo("username", username)
                    .limit(1)
                    .get()
                    .await()

                if (documents.isEmpty) {
                    onResult(false, "Username không tồn tại")
                    return@launch
                }

                val email = documents.documents[0].getString("email")
                if (email.isNullOrEmpty()) {
                    onResult(false, "Lỗi dữ liệu người dùng")
                    return@launch
                }

                login(email, password, onResult)
            } catch (e: Exception) {
                onResult(false, "Lỗi khi tìm kiếm: ${e.localizedMessage}")
            }
        }
    }

    fun loginWithGoogle(activityContext: Context, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                val response = buildCredentialRequest(activityContext)
                Log.d("AuthViewModel", "Google Sign-In Response: $response")

                val success = handleGoogleSignIn(response)
                withContext(Dispatchers.Main) {
                    if (success) {
                        onResult(true, null)
                    } else {
                        onResult(false, "Google Sign-In thất bại")
                    }
                }
            } catch (e: Exception) {
                Log.e("AuthViewModel", "Lỗi đăng nhập Google: ${e.localizedMessage}", e)
                if (e is CancellationException) throw e
                onResult(false, "Lỗi đăng nhập Google: ${e.localizedMessage}")
            }
        }
    }


    private suspend fun handleGoogleSignIn(result: GetCredentialResponse): Boolean {
        val credential = result.credential
        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {

            return try {
                val tokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val authCredential = GoogleAuthProvider.getCredential(tokenCredential.idToken, null)
                val email = tokenCredential.id ?: return false

                val signInMethods =
                    auth.fetchSignInMethodsForEmail(email).await().signInMethods ?: listOf()

                if (signInMethods.isNotEmpty() && !signInMethods.contains(GoogleAuthProvider.PROVIDER_ID)) {
                    val user = auth.currentUser ?: return false
                    user.linkWithCredential(authCredential).await()
                } else {
                    auth.signInWithCredential(authCredential).await()
                }

                val firebaseUser = auth.currentUser ?: return false
                saveUserToFirestore(firebaseUser)
                true
            } catch (e: Exception) {
                false
            }
        }
        return false
    }

    private suspend fun saveUserToFirestore(firebaseUser: FirebaseUser) {
        val userRef = firestore.collection("users").document(firebaseUser.uid)
        val document = userRef.get().await()
        if (!document.exists()) {
            val newUser = UserModel.create(
                name = firebaseUser.displayName ?: "",
                username = firebaseUser.email ?: "",
                email = firebaseUser.email ?: "",
                role = "user",
                uid = firebaseUser.uid
            )
            userRef.set(newUser).await()
            newUser
        } else {
            Log.d("FirestoreData", "hahahahahah")
            userModel = document.toObject(UserModel::class.java)
        }
    }

    private suspend fun buildCredentialRequest(activityContext: Context): GetCredentialResponse {
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(
                GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId("788846564510-sniqs3bq1e8nnm2fu4d52h0k7tl2587g.apps.googleusercontent.com")
                    .setAutoSelectEnabled(false)
                    .build()
            )
            .build()

        return credentialManager.getCredential(activityContext, request)
    }

    fun signup(
        name: String,
        username: String,
        email: String,
        password: String,
        onResult: (Boolean, String?) -> Unit
    ) {
        viewModelScope.launch {
            try {
                val documents = firestore.collection("users")
                    .whereEqualTo("username", username)
                    .get()
                    .await()

                if (!documents.isEmpty) {
                    onResult(false, "Username đã được sử dụng. Vui lòng chọn username khác.")
                    return@launch
                }

                val result = auth.createUserWithEmailAndPassword(email, password).await()
                val userId = result.user?.uid ?: throw Exception("Lỗi tạo tài khoản")

                auth.currentUser?.sendEmailVerification()?.await()
                val userModel = UserModel.create(
                    name,
                    username,
                    email = email,
                    role = "user",
                    uid = userId
                )
                firestore.collection("users").document(userId).set(userModel).await()

                auth.signOut()
                onResult(true, "Đăng ký thành công. Vui lòng xác thực email của bạn để đăng nhập.")
            } catch (e: Exception) {
                onResult(false, "Lỗi đăng ký: ${e.localizedMessage}")
            }
        }
    }

    fun resetPassword(email: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            try {
                auth.sendPasswordResetEmail(email).await()
                onResult(
                    true,
                    "Email đặt lại mật khẩu đã được gửi. Vui lòng kiểm tra hộp thư của bạn."
                )
            } catch (e: Exception) {
                onResult(false, "Lỗi khi gửi email đặt lại mật khẩu: ${e.localizedMessage}")
            }
        }
    }

    fun getUserModel() {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                val document = firestore.collection("users")
                    .document(currentUser.uid)
                    .get()
                    .await()

                userModel = document.toObject(UserModel::class.java)
            } catch (e: Exception) {
                userModel = null
            }
        }
    }

    fun logout() {
        auth.signOut()
        userModel = null
        user = null
    }

    suspend fun updateAvatar(url: String) {
        val currentUser = auth.currentUser ?: return

        viewModelScope.launch {
            try {
                firestore.collection("users")
                    .document(currentUser.uid)
                    .update("avatar", url)
                    .await()
                userModel = userModel?.copy(avatar = url)
            } catch (e: Exception) {
                Log.e("updateAvatar", "Lỗi khi cập nhật avatar: ${e.localizedMessage}")
            }
        }
    }

    suspend fun updateUserInfo(data: Map<String, Any>) {
        val currentUser = auth.currentUser ?: return

        val updateData = mutableMapOf<String, Any>()
        data["name"]?.let { updateData["name"] = it }
        data["phoneNumber"]?.let { updateData["phoneNumber"] = it }
        data["sex"]?.let { updateData["sex"] = it }
        data["birthDate"]?.let { updateData["birthDate"] = it }

        try {
            withContext(Dispatchers.IO) {
                firestore.collection("users")
                    .document(currentUser.uid)
                    .update(updateData)
                    .await()
            }

            val snapshot = firestore.collection("users")
                .document(currentUser.uid)
                .get()
                .await()
            val updatedUser = snapshot.toObject(UserModel::class.java)
            userModel = updatedUser

            Log.d("updateUserInfo", "Cập nhật thông tin thành công")
        } catch (e: Exception) {
            Log.e("updateUserInfo", "Lỗi khi cập nhật: ${e.message}", e)
        }
    }

}

object GLobalAuthViewModel {
    private lateinit var authViewModel: AuthViewModel

    fun initialize(application: Application) {
        if (!::authViewModel.isInitialized) {
            authViewModel = AuthViewModel(application)
        }
    }

    fun getAuthViewModel(): AuthViewModel {
        check(::authViewModel.isInitialized) { "GLobalAuthViewModel must be initialized with an Application instance first." }
        return authViewModel
    }
}