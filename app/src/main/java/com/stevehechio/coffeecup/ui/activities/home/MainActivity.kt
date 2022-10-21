package com.stevehechio.coffeecup.ui.activities.home

import android.os.Bundle
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.stevehechio.coffeecup.data.Resource
import com.stevehechio.coffeecup.data.local.entities.CoffeeEntity
import com.stevehechio.coffeecup.ui.theme.CoffeeCupTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoffeeCupTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Screen()
                }
            }
        }
    }
}

@Composable
fun Screen(
    coffeeViewModel: CoffeeViewModel = viewModel()
) {
    when (val uiState = coffeeViewModel.getCoffeeEntityLiveData.value) {
        is Resource.Success -> {
            uiState.data?.let { CoffeeGrid(coffeeEntity = it) }
        }
        is Resource.Loading -> {
            Box(modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center){
                CircularProgressIndicator(modifier = Modifier.size(60.dp))
            }
        }
        is Resource.Failure -> {
            Text(text = "Failed")
        }
    }


}

@Composable
fun CoffeeGrid(coffeeEntity: List<CoffeeEntity>){
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ){
        items(coffeeEntity){ coffeeEntity ->
            CoffeeRow(coffeeEntity)
        }
    }
}

@Composable
fun CoffeeRow(coffeeEntity: CoffeeEntity) {
    Card(
        shape = RoundedCornerShape(8.dp),
    backgroundColor = MaterialTheme.colors.surface) {
        Column(
            modifier = Modifier.padding(8.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = coffeeEntity.image,
                contentDescription = "",
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape), contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(8.dp))
            Text(text = coffeeEntity.title,
                color = MaterialTheme.colors.secondary)

        }
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoffeeCupTheme {
//        Screen("Android")
    }
}