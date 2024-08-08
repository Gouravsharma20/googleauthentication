package wu.tutorials.spspspspspsp.Presentation.sign_in

import android.service.autofill.UserData

data class SignInResult(
    val data: UserDatas?,
    val errorMessage : String?
) data class UserDatas (
    val userId:String,
    val usersName:String?,
    val profilePictureUrl:String?
)

