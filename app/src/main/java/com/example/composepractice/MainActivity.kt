package com.example.composepractice

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composepractice.ui.theme.ComposePracticeTheme
import com.example.composepractice.ui.theme.GreenWt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()

        val splashScreen = installSplashScreen()

        val viewModel by viewModels<MainViewModel>()

        splashScreen.setKeepOnScreenCondition {
            viewModel.isReady.value == true
        }

        setContent {
            ComposePracticeTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        NavigationDrawer()
                        MainContent(viewModel = viewModel)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavigationDrawer(){
    val navController = rememberNavController()
    val coroutineScope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current.applicationContext

    ModalNavigationDrawer(drawerState = drawerState, gesturesEnabled = true, drawerContent = {
        ModalDrawerSheet {
            Box(modifier = Modifier
                .background(GreenWt)
                .fillMaxWidth()
                .height(140.dp)){
                Text(text = "")
            }
            Divider()
            NavigationDrawerItem(label = { Text(text = "Home", color = Color.Black) },
                selected = false,
                icon = { Icon(imageVector = Icons.Filled.Home,contentDescription ="Home") },
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Screens.Home.screens){
                        popUpTo(0)
                    }
                 })
            NavigationDrawerItem(label = { Text(text = "Profile", color = Color.Black) },
                selected = false,
                icon = { Icon(imageVector = Icons.Filled.Person,contentDescription ="Profile") },
                onClick = {
                    coroutineScope.launch {
                        drawerState.close()
                    }
                    navController.navigate(Screens.Profile.screens){
                        popUpTo(0)
                    }
                 })
        }
    }) {
        Column(Modifier.fillMaxSize()) {
            LearnTopAppBar(drawerState,coroutineScope)
            NavigationHost(navController = navController)
        }
    }


}



@Composable
fun NavigationHost(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Screens.Home.screens) {
        composable(Screens.Home.screens) { Home() }
        composable(Screens.Profile.screens) { Profile() }
    }
}

@Composable
fun MainContent(viewModel: MainViewModel) {

    val isReady by viewModel.isReady.observeAsState()

    LaunchedEffect(Unit) {
        if (isReady!!) {
            delay(10000)
            viewModel.setReady(false)
        }
    }
    UpdateScreen(isReady!!)

}

@Composable
fun UpdateScreen(state: Boolean) {
    if (!state) {
        Greeting(name = "Android")
    } else {
        Text(
            "Loading...",
            modifier = Modifier.fillMaxSize(),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LearnTopAppBar(
    drawerState: DrawerState,
    scope: CoroutineScope,
) {
    val context = LocalContext.current
    TopAppBar(title = { Text(text = "Open Nav") }, navigationIcon = {
        IconButton(onClick = {
            scope.launch {
                drawerState.open()
            }
            Toast.makeText(context, "Drawer Opened", Toast.LENGTH_SHORT).show()
        }) {
            Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
        }
    }, colors = TopAppBarDefaults.largeTopAppBarColors(
        containerColor = GreenWt,
        titleContentColor = Color.White,
        navigationIconContentColor = Color.White,
    ), actions = {
        IconButton(onClick = {
            Toast.makeText(context, "Profile", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile", tint = Color.White
            )
        }
        IconButton(onClick = {
            Toast.makeText(context, "Search", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search", tint = Color.White
            )
        }
        IconButton(onClick = {
            Toast.makeText(context, "Menu", Toast.LENGTH_SHORT).show()
        }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Menu", tint = Color.White
            )
        }
    }
    )

}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {

    val byte by remember {
        mutableStateOf(0)
    }

    val context = LocalContext.current
    val toast = {
        Toast.makeText(context, "Wow $byte", Toast.LENGTH_SHORT).show()
    }


    Column(
        modifier = Modifier
            .padding(2.dp)
            .height(500.dp)
            .width(300.dp)
    ) {
        Text(
            text = "Hello $name!",
            modifier = modifier
                .clickable(onClick = toast)
                .background(Color.Magenta),
            fontStyle = FontStyle.Italic,
            fontFamily = FontFamily.SansSerif,
            fontSize = 30.sp,
        )

        Image(
            painter = painterResource(id = R.drawable.lion_image),
            contentDescription = "Lion image"
        )

    }

}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposePracticeTheme {
        NavigationDrawer()
    }
}