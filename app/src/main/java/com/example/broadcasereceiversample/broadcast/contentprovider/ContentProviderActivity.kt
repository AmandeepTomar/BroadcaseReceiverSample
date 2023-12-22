package com.example.broadcasereceiversample.broadcast.contentprovider

import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.mycomposeproject.ui.theme.MyComposeProjectTheme
import kotlin.math.log


class ContentProviderActivity : ComponentActivity() {

    private val mColumnProjection = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
        ContactsContract.Contacts.CONTACT_STATUS,
        ContactsContract.Contacts.HAS_PHONE_NUMBER
    )

    private val mSelectionCluse = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = ?"

    private val mSelectionArguments = arrayOf("Ajay")

    private val mOrderBy = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY

   private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission()){
       if (it){
       }else{
           Log.e("TAG", ": provide permission" )
       }
   }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestPermission.launch(android.Manifest.permission.READ_CONTACTS)
        setContent {
            MyComposeProjectTheme {
                val data = readContactProvider()
                Log.e("TAG", "onCreate:$data ", )
                fetchContachtList(data)
            }
        }
    }


    private fun readContactProvider(): String {
        contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            mColumnProjection,
            null,
            null,
            null
        )?.use { cursor ->
            Log.e("TAG", "readContactProvider: ", )
            val stringBuilder = StringBuilder()
            while (cursor.moveToNext()) {
                stringBuilder.append(cursor.getString(0)).append(" -> ").append(cursor.getString(1))
                    .append("->").append(cursor.getString(2))
            }
            Log.e("TAG", "readContactProvider: ${stringBuilder.toString()}", )
            return stringBuilder.toString()
        }
        return "No Contact Available"
    }
}

@Composable
private fun fetchContachtList(readContactProvider: String) {
    Text(text = readContactProvider, color = Color.Black)
}

