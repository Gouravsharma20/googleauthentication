package wu.tutorials.spspspspspsp.ui

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await
import wu.tutorials.spspspspspsp.Presentation.sign_in.SignInResult
import wu.tutorials.spspspspspsp.Presentation.sign_in.UserDatas
import wu.tutorials.spspspspspsp.R
import java.util.concurrent.CancellationException

class GoogleAuthUIClient (
    private val context: Context,
    private val oneTapClient: SignInClient
){
    private val auth = Firebase.auth
    suspend fun signIn() : IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(buildSignInRequest()).await()
        }catch (e:Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }
    suspend fun SignInWithIntent(intent: Intent):SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken,null)

        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserDatas(
                        userId = uid,
                        usersName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                }, errorMessage = null
            )
        }catch (e:Exception)
        {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message)
        }
    }
    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        }
        catch (e:Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser() : UserDatas? = auth.currentUser?.run {
        UserDatas (
            userId = uid ,
            usersName = displayName ,
            profilePictureUrl = photoUrl.toString()
        )
    }
    private fun buildSignInRequest():BeginSignInRequest {
        return BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true).
                    setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(
                        R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true).build()
    }
}
