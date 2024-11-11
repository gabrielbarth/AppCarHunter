package com.example.carhunter.ui.carForm

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarFormScreen(
    viewModel: CarFormViewModel = viewModel(),
    onSave: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    var name by remember {
        mutableStateOf(
            TextFieldValue(uiState.car?.name ?: "")
        )
    }
    var year by remember {
        mutableStateOf(
            TextFieldValue(uiState.car?.year ?: "")
        )
    }
    var licence by remember {
        mutableStateOf(
            TextFieldValue(uiState.car?.licence ?: "")
        )
    }

    LaunchedEffect(uiState.car) {
        name = TextFieldValue(uiState.car?.name ?: "")
        year = TextFieldValue(uiState.car?.year ?: "")
        licence = TextFieldValue(uiState.car?.licence ?: "")
    }

    val context = LocalContext.current

    val imageUri by viewModel.imageUri.collectAsState()

    // save temp image
    val tempImageFile = remember { File(context.cacheDir, "car_image.jpg") }
    val uri =
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempImageFile)
    val takePictureLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                println("sucess $uri")
                viewModel.setImageUri(uri)
            }
        }
    )

    // launcher for requesting camera permission
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        viewModel.updateCameraPermissionStatus(isGranted)
    }

    // initial check for camera permission
    LaunchedEffect(Unit) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (isGranted) {
            viewModel.updateCameraPermissionStatus(true)
        }
    }

    // on save successfully
    LaunchedEffect(viewModel.uiState.value?.hasSuccessfullySaved) {
        if (viewModel.uiState.value.hasSuccessfullySaved) {
            onSave()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Nome do Carro") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = year,
            onValueChange = { year = it },
            label = { Text("Ano") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = licence,
            onValueChange = { licence = it },
            label = { Text("Licença") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (imageUri != null) {
            Image(
                painter = rememberImagePainter(data = imageUri),
                contentDescription = "Car Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        } else if (uiState.car?.imageUrl?.isNotEmpty() == true) {
            Image(
                painter = rememberImagePainter(data = uiState.car?.imageUrl),
                contentDescription = "Car Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )
        }


        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                if (viewModel.uiState.value.hasCameraPermission) {
                    takePictureLauncher.launch(uri)
                } else {
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
                }
            }) {
            Text("Tirar Foto")
        }

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.validateAndSaveCard(name.text, year.text, licence.text)
            }) {
            Text("Salvar")
        }

        if (uiState.car?.id?.isNotEmpty() == true) {
            uiState.car?.id.let {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        viewModel.deleteCar()
                    }) {
                    Text("Deletar")
                }
            }
        }
    }

}
