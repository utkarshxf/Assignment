package com.myjar.jarassignment.ui.screens

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.myjar.jarassignment.data.model.ComputerItem
import com.myjar.jarassignment.ui.vm.JarViewModel

@Composable
fun ItemListScreen(
    viewModel: JarViewModel,
    onNavigateToDetail: (String) -> Unit,
    navigate: MutableState<String>,
    navController: NavHostController
) {
    val items = viewModel.searchItem.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    Log.e("ItemListScreen", "ItemListScreen: ${items.value}")
    // Handle navigation
    DisposableEffect(navigate.value) {
        onDispose {
            navigate.value = ""
        }
    }
    if (navigate.value.isNotBlank()) {
        Log.e("currentDestination", "ItemListScreen: ${navigate.value}")
        val currRoute = navController.currentDestination?.route.orEmpty()
        if (!currRoute.contains("item_detail")) {
            navController.navigate("item_detail/${navigate.value}")
        }
    }
    Scaffold(topBar = {
        Text(
            text = "Item List",
            modifier = Modifier.padding(16.dp),
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
    }, content = {
        Column {
            TextField(modifier = Modifier
                .padding(it)
                .fillMaxWidth()
                .padding(16.dp),
                label = { Text("Search") },
                value = searchQuery,
                onValueChange = { onChange ->
                    searchQuery = onChange
                    viewModel.search(searchQuery)
                }
            )
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                items(items.value) { item ->
                    ItemCard(item = item, onClick = { onNavigateToDetail(item.id) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    })
}

@Composable
fun ItemCard(item: ComputerItem, onClick: () -> Unit) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .clickable { onClick() }) {
        // Colour was Transparent
        Text(
            text = item.name,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        item.data?.color?.let {
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
        item.data?.capacity?.let {
            Text(text = it, color = MaterialTheme.colorScheme.primary)
        }
    }
}