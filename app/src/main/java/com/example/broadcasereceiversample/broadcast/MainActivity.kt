package com.example.mycomposeproject

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.broadcasereceiversample.broadcast.br.BroadcastActivity
import com.example.broadcasereceiversample.broadcast.contentprovider.ContentProviderActivity
import com.example.mycomposeproject.ui.theme.MyComposeProjectTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyComposeProjectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(this)
                }
            }
        }
    }
}

@Composable
fun Greeting(mainActivity: MainActivity?) {
    Column {
        Button(onClick = {
            Intent(mainActivity, BroadcastActivity::class.java).also {
                mainActivity?.startActivity(it)
            }
        }) {
            Text(text = "Open Broadcast")
        }
        Spacer(Modifier.size(4.dp))
        Button(onClick = {
            Intent(mainActivity, ContentProviderActivity::class.java).also {
                mainActivity?.startActivity(it)
            }
        }) {
            Text(text = "Open COntent Provider")
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun GreetingPreview() {
    MyComposeProjectTheme {
        Greeting(null)
    }
}