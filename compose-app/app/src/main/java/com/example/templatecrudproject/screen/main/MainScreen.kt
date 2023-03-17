package com.example.templatecrudproject.screen.main

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.templatecrudproject.R
import com.example.templatecrudproject.entity.domain.Entity
import com.example.templatecrudproject.entity.viewmodel.EntityViewModel
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun MainScreen(

    onCreateFabClick: () -> Unit = {},
    onTopFabClick: () -> Unit = {},
    viewModel: EntityViewModel

) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val onNoNetwork: () -> Unit = {

        coroutineScope.launch {

            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = "Device is offline.",
                actionLabel = "RETRY"
            )

            when (snackBarResult) {
                SnackbarResult.Dismissed -> Log.d("SnackBarDemo", "Dismissed")
                SnackbarResult.ActionPerformed -> viewModel.retryConnection()

            }
        }
    }

    val onNewEntity: (String) -> Unit = {

        coroutineScope.launch {

            // TODO handle new entity notification
            val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                message = "New exercise added: $it",
                actionLabel = "OK"
            )

            when (snackBarResult) {
                SnackbarResult.Dismissed -> Log.d("SnackBarDemo", "Dismissed")
                SnackbarResult.ActionPerformed -> Log.d("SnackBarDemo", "Action Performed")
            }
        }
    }

    viewModel.setNewEntityAction(onNewEntity)

    Scaffold(

        topBar = { MainToolbar() },
        scaffoldState = scaffoldState,

        floatingActionButton = {

            Row(
                horizontalArrangement = Arrangement.spacedBy(20.dp)
            ) {

                CreateFab(viewModel, onCreateFabClick, onNoNetwork)
                TopFab(viewModel = viewModel, onDifficultyFabClick = onTopFabClick, onNoNetwork = onNoNetwork)
            }
        }

    ) {

        MainBody {
            MainEntityList(viewModel = viewModel, onNoNetwork = onNoNetwork)
        }

    }
}

@Composable
fun MainToolbar() {

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        TopAppBar(
            title = { Text(text = "All Data") },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            contentColor = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun MainBody(content: @Composable () -> Unit) {

    Surface(
        color = MaterialTheme.colors.background,
        modifier = Modifier
            .fillMaxWidth()

    ) {

        content()
    }
}


@Composable
fun MainEntityList(viewModel: EntityViewModel, onNoNetwork: () -> Unit) {

    val connected by viewModel.serverAvailability.collectAsState()

    var menuExpanded by remember {
        mutableStateOf(false)
    }

    val datesList by viewModel.datesList.collectAsState()

    val datesListState by datesList.observeAsState()

    val selectedDate by viewModel.selectedDate.collectAsState()

    val entityList by viewModel.filteredList.collectAsState()

    val entityListState by entityList.observeAsState()

    Surface(

        modifier = Modifier.fillMaxWidth(),
    ) {

        Column {

            Box(modifier = Modifier.clickable(onClick = {
                menuExpanded = true
                if (!connected) {
                    onNoNetwork()
                }
            })) {

                Row(modifier = Modifier
                    .border(
                        BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .background(
                        color = MaterialTheme.colors.primary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(24.dp),

                    verticalAlignment = Alignment.CenterVertically) {

                    selectedDate?.let { Text(it) } ?: Text("-- SELECT --")
                }

                DropdownMenu(expanded = menuExpanded, onDismissRequest = { /*TODO*/ }) {

                    DropdownMenuItem(onClick = { viewModel.setEntityListByDate(null); menuExpanded = false }) {
                        Text("-- SELECT --")
                    }

                    datesListState?.forEach { c -> DropdownMenuItem(onClick = { viewModel.setEntityListByDate(c); menuExpanded = false }) {

                        Text(c)
                    } }
                }
            }
            LazyColumn(

                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                items(entityListState ?: listOf()) {

                        entity -> MainEntityCard(viewModel = viewModel, entity = entity, onNoNetwork = onNoNetwork)
                }
            }
        }

    }
}

@Composable
fun MainEntityCard(viewModel: EntityViewModel, entity: Entity, onNoNetwork: () -> Unit) {

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
            Text(text = "Date: " + entity.date)
            Text(text = "Type: " + entity.type)
            Text(text = "Duration: " + entity.duration + " minutes")
        }

        Divider(
            modifier = Modifier
                .fillMaxHeight()
                .width(2.dp),
            color = MaterialTheme.colors.onBackground
        )

        Column(

            modifier = Modifier
                .fillMaxSize()
                .clickable {
                    if (viewModel.serverAvailability.value) {
                        viewModel.deleteEntity(entity.id!!)
                    } else {
                        onNoNetwork()
                    }
                },

            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Image(
                painter = painterResource(id = R.drawable.ic_baseline_clear_24),
                contentDescription = "delete"
            )
        }

    }
}

@Composable
fun CreateFab(viewModel: EntityViewModel, onAddFabClick: () -> Unit, onNoNetwork: () -> Unit) {

    FloatingActionButton(
        shape = CircleShape,
        onClick = {
            if (viewModel.serverAvailability.value) {
                onAddFabClick()
            }
            else {
                onNoNetwork()
            }
                  },
        backgroundColor = MaterialTheme.colors.background,
    ) {

        Image(
            painter = painterResource(id = R.drawable.ic_baseline_add_64),
            contentDescription = "create",
            modifier = Modifier
                .padding(6.dp)
        )

    }
}

@Composable
fun TopFab(viewModel: EntityViewModel, onDifficultyFabClick: () -> Unit, onNoNetwork: () -> Unit) {

    FloatingActionButton(
        shape = CircleShape,
        onClick = {
            if (viewModel.serverAvailability.value) {
                onDifficultyFabClick()
            }
            else {
                onNoNetwork()
            }
                  },
        backgroundColor = MaterialTheme.colors.background,

        ) {

        Image(
            painter = painterResource(id = R.drawable.baseline_keyboard_double_arrow_up_64),
            contentDescription = "top",
            modifier = Modifier
                .padding(6.dp)
        )
    }
}