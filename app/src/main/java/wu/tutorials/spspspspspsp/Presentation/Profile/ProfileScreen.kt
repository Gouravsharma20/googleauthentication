package wu.tutorials.spspspspspsp.Presentation.Profile

import android.service.autofill.UserData
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import wu.tutorials.spspspspspsp.Presentation.sign_in.UserDatas

@Composable
fun ProfileScreen(
    userDatas: UserDatas?,
    onSignOut:()->Unit
) {
    Column(
        modifier = Modifier.fillMaxSize()
        , verticalArrangement = Arrangement.Center
        , horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (
            userDatas?.profilePictureUrl!=null
            ) {
            AsyncImage(
                model = userDatas.profilePictureUrl,
                contentDescription = "profilephoto",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if (userDatas?.usersName!=null) {
            Text(
                text = userDatas.usersName,
                fontSize = 36.sp,
                color = Color.Blue,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = onSignOut

        ) {
            Text(text = "Sign-out")
        }
    }

}