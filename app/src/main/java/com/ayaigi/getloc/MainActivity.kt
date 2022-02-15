package com.ayaigi.getloc

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.MutableLiveData
import com.ayaigi.getloc.ui.theme.GetLocationTheme
import com.ayaigi.location.GetLocation

class MainActivity : ComponentActivity(), GetLocation.Companion.LocationListener {

    val state = MutableLiveData<Location>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loc = GetLocation(this, this)
        setContent {
            val state by state.observeAsState()
            GetLocationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column() {
                        if (state != null) {
                            Text("${state!!.longitude}; ${state!!.latitude}")
                        }else {
                            Text("NULL")
                        }
                        val c = LocalContext.current
                        Button(
                            content = {
                                Text(text = "Update")
                            },
                            onClick = {
                                loc.get(c)
                            }
                        )
                    }
                }
            }
        }
    }

    override fun onComplete(location: Location?) {
        if (location != null) {
            Log.i("ApD", "LocateGetNNComplete")
            state.value = location
        }else{
            Log.i("ApD", "LocateGetCompleteNUll")
        }
    }
}
