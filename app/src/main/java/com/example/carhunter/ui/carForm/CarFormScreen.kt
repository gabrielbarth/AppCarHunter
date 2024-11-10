package com.example.carhunter.ui.carForm

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarFormScreen(viewModel: CarFormViewModel = viewModel()) {
    var name by remember { mutableStateOf(TextFieldValue("")) }
    var year by remember { mutableStateOf(TextFieldValue("")) }
    var license by remember { mutableStateOf(TextFieldValue("")) }

//    val imageUri by viewModel.imageUri.collectAsState()

//    val context = LocalContext.current
//    val tempImageFile = remember { File(context.cacheDir, "car_image.jpg") }
//    val uri = FileProvider.getUriForFile(context, "${context.packageName}.provider", tempImageFile)
//    val takePictureLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.TakePicture(),
//        onResult = { success ->
//            if (success) {
//                viewModel.setImageUri(uri)
//            }
//        }
//    )

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
            value = license,
            onValueChange = { license = it },
            label = { Text("Licença") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

//        Button(onClick = { takePictureLauncher.launch(uri) }) {
//            Text("Tirar Foto")
//        }

        Spacer(modifier = Modifier.height(16.dp))

//        imageUri?.let { uri ->
//            Image(
//                painter = rememberImagePainter(data = uri),
//                contentDescription = "Car Image",
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(200.dp)
//            )
//        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                viewModel.updateCar(name.text, year.text, license.text)
                viewModel.saveCar()
            }) {
            Text("Salvar")
        }
    }
}
