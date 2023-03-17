package com.example.templatecrudproject.screen.create

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import com.example.templatecrudproject.entity.domain.Entity
import com.example.templatecrudproject.entity.viewmodel.EntityViewModel
import com.example.templatecrudproject.screen.main.*
import com.example.templatecrudproject.utils.checkValidDate
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun CreateScreen(

    onCreateClick: () -> Unit,
    viewModel: EntityViewModel

) {

    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(

        topBar = { MainToolbar() },
        scaffoldState = scaffoldState

    ) {

        MainBody {

            CreateForm(

                onCreateClick = onCreateClick,
                onError = {

                    coroutineScope.launch {

                        val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                            message = "Mandatory (*) fields missing or invalid values!",
                            actionLabel = "Dismiss"

                        )
                        when (snackBarResult) {
                            SnackbarResult.Dismissed -> Log.d("SnackBarDemo", "Dismissed")
                            SnackbarResult.ActionPerformed -> Log.d("SnackBarDemo", "SnackBar's button clicked")
                        }
                    }

                          },
                viewModel = viewModel)
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
        // TODO put entity name in title
        TopAppBar(
            title = { Text(text = "Create New Exercise") },
            backgroundColor = MaterialTheme.colors.primaryVariant,
            contentColor = MaterialTheme.colors.onPrimary
        )
    }
}

@Composable
fun MainBody(content: @Composable () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.background)
            .padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)

    ) {

        content()
    }
}


@Composable
fun CreateForm(viewModel: EntityViewModel, onCreateClick: () -> Unit, onError: () -> Unit) {

    val datesList by viewModel.datesList.collectAsState()

    var menuExpanded by remember {
        mutableStateOf(false)
    }


    // TODO Add all entity fields to component

    var date by remember {

        mutableStateOf("")
    }

    var type by remember {

        mutableStateOf("")
    }

    var duration by remember {

        mutableStateOf("")
    }

    var distance by remember {

        mutableStateOf("")
    }

    var calories by remember {

        mutableStateOf("")
    }

    var rate by remember {

        mutableStateOf("")
    }

    var selectedDate: String? by remember {

        mutableStateOf(null)
    }

    // TODO add all entity fields, set the labels and mark the mandatory ones
    TextField(
        value = date,
        onValueChange = { date = it },
        singleLine = true,
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(fraction = 0.8f)
            .padding(10.dp),
        textStyle = TextStyle(color = Color.White),
        // TODO set field label to field name
        label = { Text("Date (YYYY-MM-DD) *", color = Color.White)},
        colors = TextFieldDefaults.textFieldColors(cursorColor = Color.White)
    )

    TextField(
        value = type,
        onValueChange = { type = it },
        singleLine = true,
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(fraction = 0.8f)
            .padding(10.dp),
        textStyle = TextStyle(color = Color.White),
        // TODO set field label to field name
        label = { Text("Type *", color = Color.White)},
        colors = TextFieldDefaults.textFieldColors(cursorColor = Color.White)
    )

    TextField(
        value = duration,
        onValueChange = { duration = it },
        singleLine = true,
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(fraction = 0.8f)
            .padding(10.dp),
        textStyle = TextStyle(color = Color.White),
        // TODO set field label to field name
        label = { Text("Duration *", color = Color.White)},
        colors = TextFieldDefaults.textFieldColors(cursorColor = Color.White),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )

    TextField(
        value = distance,
        onValueChange = { distance = it },
        singleLine = true,
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(fraction = 0.8f)
            .padding(10.dp),
        textStyle = TextStyle(color = Color.White),
        // TODO set field label to field name
        label = { Text("Distance *", color = Color.White)},
        colors = TextFieldDefaults.textFieldColors(cursorColor = Color.White),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )

    TextField(
        value = calories,
        onValueChange = { calories = it },
        singleLine = true,
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(fraction = 0.8f)
            .padding(10.dp),
        textStyle = TextStyle(color = Color.White),
        // TODO set field label to field name
        label = { Text("Calories *", color = Color.White)},
        colors = TextFieldDefaults.textFieldColors(cursorColor = Color.White),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )

    TextField(
        value = rate,
        onValueChange = { rate = it },
        singleLine = true,
        modifier = Modifier
            .background(
                color = MaterialTheme.colors.primary,
                shape = RoundedCornerShape(16.dp)
            )
            .border(
                BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth(fraction = 0.8f)
            .padding(10.dp),
        textStyle = TextStyle(color = Color.White),
        // TODO set field label to field name
        label = { Text("Rate *", color = Color.White)},
        colors = TextFieldDefaults.textFieldColors(cursorColor = Color.White),
        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
    )


    Button(

        onClick = {

            // TODO add non-nullable fields to be checked
            if (date.checkValidDate() && type != "" && duration.toIntOrNull() != null && distance.toIntOrNull() != null && calories.toIntOrNull() != null && rate.toIntOrNull() != null) {
                viewModel.addEntity(
                    Entity(
                        date = date,
                        type = type,
                        duration = duration.toInt(),
                        distance = distance.toInt(),
                        calories = calories.toInt(),
                        rate = rate.toInt()
                    )
                )
                onCreateClick()
            }
            else {
                onError()
            }

        },

        modifier = Modifier
            .border(
                BorderStroke(1.dp, MaterialTheme.colors.onBackground),
                shape = RoundedCornerShape(16.dp)
            )
            .fillMaxWidth()
            .background(
                color = Color.Green,
                shape = RoundedCornerShape(16.dp)
            )
    ) {

        // TODO insert entity name
        Text("Create Exercise")
    }

}