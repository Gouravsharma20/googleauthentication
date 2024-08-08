package wu.tutorials.spspspspspsp

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.Api.Client
import kotlinx.coroutines.launch
import wu.tutorials.spspspspspsp.Presentation.Profile.ProfileScreen
import wu.tutorials.spspspspspsp.Presentation.sign_in.SignInScreen
import wu.tutorials.spspspspspsp.Presentation.sign_in.SignInViewModel
import wu.tutorials.spspspspspsp.ui.GoogleAuthUIClient
import wu.tutorials.spspspspspsp.ui.theme.SpspspspspspTheme
import kotlin.contracts.contract

class MainActivity : ComponentActivity() {
    private val googleAuthUiClient by lazy {
        GoogleAuthUIClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(
                applicationContext
            )
        )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SpspspspspspTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                    , color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "sign_in" ) {
                        composable("sign_in") {
                            val viewModel = viewModel<SignInViewModel.SignInViewModel>()
                            val state by viewModel.state.collectAsStateWithLifecycle()
                            LaunchedEffect(key1 = Unit) {
                                if (googleAuthUiClient.getSignedInUser() !=null) {
                                    navController.navigate("Profile")
                                }
                                
                            }
                            val launcher = rememberLauncherForActivityResult(
                                contract = ActivityResultContracts.StartIntentSenderForResult(),
                                onResult = {
                                    result -> if (
                                    result.resultCode == RESULT_OK
                                        ) {
                                        lifecycleScope.launch {
                                            val signInResult = googleAuthUiClient.SignInWithIntent(
                                                intent = result.data?:return@launch
                                            )
                                            viewModel.onSignInResult(signInResult)
                                        }
                                    }
                                }
                            )
                            SignInScreen(
                                state = state,
                                onSignInClick = {
                                    lifecycleScope.launch {
                                        val signInIntentSender = googleAuthUiClient.signIn()
                                        launcher.launch(
                                            IntentSenderRequest.Builder(
                                                signInIntentSender?:return@launch
                                            ).build()
                                        )
                                    }
                                }
                            )
                            LaunchedEffect(key1 = state.isSignInSuccessful) {
                                if (state.isSignInSuccessful) {
                                    Toast.makeText(
                                        applicationContext,
                                        "signned in successfully",
                                        Toast.LENGTH_LONG
                                    ).show()
                                    navController.navigate("Profile")
                                    viewModel.resetState()
                                }
                            }
                        }
                        composable("Profile") {
                            ProfileScreen(
                                userDatas = googleAuthUiClient.getSignedInUser(),
                                onSignOut = {
                                    lifecycleScope.launch {
                                        googleAuthUiClient.signOut()
                                Toast.makeText(
                                    applicationContext,
                                    "Signed out Successfully",
                                    Toast.LENGTH_LONG
                                ).show()
                                    navController.popBackStack()
                                }
                                }
                            )
                        }
                    }

                }
            }
        }
    }
}



