package com.example.petcommunity.presentation.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.petcommunity.R
import com.example.petcommunity.presentation.wigdet.TextTile
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(homeViewModel: HomeViewModel) {

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val coroutine = rememberCoroutineScope()
    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet {

        }
    }, drawerState = drawerState) {
        Scaffold(topBar = {
            TopAppBar(title = {
                Text(
                    "Centered Top App Bar",
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),

                    )
            }, navigationIcon = {

                IconButton(onClick = {
                    coroutine.launch {
                        drawerState.open()
                    }
                }) {
                    Icon(Icons.Default.Menu, contentDescription = "")
                }
            })
        }) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier
                        .fillMaxSize()
                        .padding((LocalConfiguration.current.screenWidthDp * 0.04).dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.donate),
                        contentDescription = "", contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height((LocalConfiguration.current.screenHeightDp * 0.2).dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Color.Gray)
                    )

                    OutlinedTextField(value = "", onValueChange = {}, leadingIcon = {
                        IconButton(
                            onClick = { /*TODO*/ }) {
                            Icon(Icons.Default.Search, contentDescription = "")
                        }
                    }, colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White,
                        disabledContainerColor = Color.White
                    )
                    )
                    TextTile(
                        title = "Category",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start),
                    )









                    TextTile(
                        title = "Near by",
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.Start),
                    )
                }

            }
        }
    }
}


@Composable
private fun HeaderHomeScreen() {

}