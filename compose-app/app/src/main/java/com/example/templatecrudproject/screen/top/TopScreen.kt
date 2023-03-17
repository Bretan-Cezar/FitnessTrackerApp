package com.example.templatecrudproject.screen.top

import android.annotation.SuppressLint
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.templatecrudproject.R
import com.example.templatecrudproject.entity.domain.Entity
import com.example.templatecrudproject.entity.viewmodel.EntityViewModel
import androidx.compose.runtime.*
import com.example.templatecrudproject.entity.domain.PopularType

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun TopScreen(
    viewModel: EntityViewModel
) {

    Scaffold(

        topBar = { TopToolbar() },

        ) {

        TopBody {
            TopEntityList(viewModel = viewModel)
        }

    }
}

@Composable
fun TopToolbar() {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        // TODO add title
        TopAppBar(
            title = { Text(text = "Most Popular Exercise Types") },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            contentColor = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun TopBody(content: @Composable () -> Unit) {

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()

    ) {

        content()
    }
}

@Composable
fun TopEntityList(viewModel: EntityViewModel) {

    val entityList by viewModel.topList.collectAsState()

    val entityListState by entityList.observeAsState()

    Surface(

        modifier = Modifier.fillMaxWidth(),
    ) {
        Column {

            LazyColumn(

                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(entityListState ?: listOf()) {

                        entity -> TopEntityCard(viewModel = viewModel, entity = entity)
                }
            }
        }
    }
}

@Composable
fun TopEntityCard(viewModel: EntityViewModel, entity: PopularType) {

    Row(
        modifier = Modifier
            .border(
                BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .height(IntrinsicSize.Min),

        verticalAlignment = Alignment.CenterVertically

    ) {

        Column(

            modifier = Modifier
                .padding(20.dp, 20.dp, 0.dp, 20.dp)
                .fillMaxWidth(fraction = 0.85f),

            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {

            // TODO add Text components for all entity fields
            Text(text = entity.type)
            Text(text = entity.count.toString())
        }
    }
}

