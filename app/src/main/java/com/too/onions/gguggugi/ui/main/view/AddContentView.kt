package com.too.onions.gguggugi.ui.main.view

import android.net.Uri
import android.provider.DocumentsContract
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.too.onions.gguggugi.R
import com.too.onions.gguggugi.db.data.Content
import com.too.onions.gguggugi.viewmodel.MainViewModel

@Composable
fun AddContentView(
    viewModel: MainViewModel,
    navController: NavHostController
) {
    val selectedColor = remember { mutableStateOf(Color(0xFFDBCEEC)) }
    val selectedImageUri = remember { mutableStateOf<Uri?>(null) }
    val contentName = remember { mutableStateOf("") }
    val contentDescription = remember { mutableStateOf("") }
    val address = remember { mutableStateOf("") }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xfff2f1f3))
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(bottom = 94.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ContentTitleBar(navController)

        Spacer(modifier = Modifier.size(15.dp))
        DisplayContent(selectedColor, selectedImageUri, contentName)

        Spacer(modifier = Modifier.size(30.dp))
        ImageButtons(selectedColor, selectedImageUri)

        Spacer(modifier = Modifier.size(30.dp))
        InputName(contentName)

        Spacer(modifier = Modifier.size(30.dp))
        InputDescription(contentDescription)

        Spacer(modifier = Modifier.size(30.dp))
        InputAddress(address)
    }

    RegistButton(contentName, contentDescription) {
        val content = Content(
            pageId = viewModel.currentPage.value.id,
            color = selectedColor.value.toArgb(),
            imageUri = checkAndReplaceUri(selectedImageUri.value).toString(),
            title = contentName.value,
            description = contentDescription.value,
            address = address.value
        )

        viewModel.insertContent(content)
        navController.popBackStack()
    }
}
fun checkAndReplaceUri(uri: Uri?) : String? {
    if (uri == null) return null

    if (isDocumentUri(uri)) {
        return getUriFromDocumentUri(uri)
    } else {
        return uri.toString()
    }
}
fun isDocumentUri(uri: Uri?): Boolean {
    return uri.toString().startsWith("content://com.android.providers.media.documents/document/")
}
fun getUriFromDocumentUri(documentUri: Uri): String? {

    val documentId = DocumentsContract.getDocumentId(documentUri).split(":")

    if (documentId.size > 1) {
        return "content://media/external/images/media/" + documentId[1]
    }
    return null
}
@Composable
fun ContentTitleBar(navController: NavHostController) {

    Surface(
        tonalElevation = 15.dp,
        color = Color(0xfff2f1f3),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {
        Box(
            modifier = Modifier
                .size(342.dp, 40.dp)
                .padding(start = 24.dp, top = 60.dp, end = 24.dp, bottom = 10.dp)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        navController.popBackStack()
                    }
                )
        ) {
            Text(
                text = "< 추가하기",
                color = Color(0xff000000),
                fontSize = 18.sp,
                modifier = Modifier
                    .wrapContentHeight()
                    .align(Alignment.CenterStart)
            )
        }
    }
}

@Composable
fun DisplayContent(
    selectedColor: MutableState<Color>,
    selectedImageUri: MutableState<Uri?>,
    content: MutableState<String>
) {
    Box(
        modifier = Modifier
            .size(150.dp, 165.dp)
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .size(150.dp, 150.dp)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = selectedColor.value)
        )

        if (selectedImageUri.value != null) {
            AsyncImage(
                model = selectedImageUri.value,
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp, 150.dp)
                    .border(1.dp, Color(0xff123485), RectangleShape),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier
                .size(120.dp, 40.dp)
                .align(Alignment.BottomStart)
                .border(1.dp, Color(0xff123485), RectangleShape)
                .background(color = Color(0xff5dcc83)),
        ) {
            Text(
                modifier = Modifier
                    .width(120.dp)
                    .align(Alignment.Center)
                    .padding(start = 5.dp),
                textAlign = TextAlign.Start,
                text = content.value,
                fontSize = 10.sp,
                color = Color(0xff123485)
            )
        }
    }
}

@Composable
fun ImageButtons(
    selectedColor: MutableState<Color>,
    selectedImageUri: MutableState<Uri?>,
) {
    val galleryLauncher = createImagePickerLauncher(selectedImageUri)

    Row {
        Box(
            modifier = Modifier
                .size(30.dp)
                .background(color = Color.White)
                .clickable(
                    interactionSource = MutableInteractionSource(),
                    indication = null,
                    onClick = {
                        galleryLauncher.launch("image/*")
                    }
                )
        ) {
            Image(
                painterResource(id = R.drawable.ic_camera),
                contentDescription = null,
                modifier = Modifier
                    .size(20.dp, 15.dp)
                    .align(Alignment.Center)
            )
        }


        Spacer(modifier = Modifier.size(13.dp))

        ColorButton(
            color = Color(0xFFDBCEEC),
            onClick = { selectedColor.value = Color(0xFFDBCEEC) },
            isSelected = selectedColor.value == Color(0xFFDBCEEC)
        )
        Spacer(modifier = Modifier.size(13.dp))

        ColorButton(
            color = Color(0xFF62B273),
            onClick = { selectedColor.value = Color(0xFF62B273) },
            isSelected = selectedColor.value == Color(0xFF62B273)
        )
        Spacer(modifier = Modifier.size(13.dp))

        ColorButton(
            color = Color(0xFFD87056),
            onClick = { selectedColor.value = Color(0xFFD87056) },
            isSelected = selectedColor.value == Color(0xFFD87056)
        )
        Spacer(modifier = Modifier.size(13.dp))

        ColorButton(
            color = Color(0xFFD3E9DD),
            onClick = { selectedColor.value = Color(0xFFD3E9DD) },
            isSelected = selectedColor.value == Color(0xFFD3E9DD)
        )
        Spacer(modifier = Modifier.size(13.dp))

        ColorButton(
            color = Color(0xFF010101),
            onClick = { selectedColor.value = Color(0xFF010101) },
            isSelected = selectedColor.value == Color(0xFF010101)
        )
        Spacer(modifier = Modifier.size(13.dp))

        ColorButton(
            color = Color(0xFFFFFFFF),
            onClick = { selectedColor.value = Color(0xFFFFFFFF) },
            isSelected = selectedColor.value == Color(0xFFFFFFFF)
        )
        Spacer(modifier = Modifier.size(13.dp))

        ColorButton(
            color = Color(0xFF7B6ECF),
            onClick = { selectedColor.value = Color(0xFF7B6ECF) },
            isSelected = selectedColor.value == Color(0xFF7B6ECF)
        )
    }
}
@Composable
fun createImagePickerLauncher(selectedImageUri: MutableState<Uri?>): ActivityResultLauncher<String> {

    return rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri ->
            selectedImageUri.value = uri
        }
    )
}

@Composable
fun ColorButton(
    color: Color,
    isSelected: Boolean,
    onClick: () -> Unit,
) {

    Box(
        modifier = Modifier
            .size(30.dp)
            .background(color)
            .clickable { onClick() }
            .border(
                width = 2.dp,
                shape = RectangleShape,
                color = if (isSelected) Color.Black else color
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputName(contentName: MutableState<String>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Text(
            text = "콘텐츠명",
            fontSize = 14.sp,
            color = Color(0xffa8a8a8)
        )

        TextField(
            value = contentName.value,
            onValueChange = {
                if (it.length <= 16) {
                    contentName.value = it
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Color.Black,
                disabledIndicatorColor  = Color.Transparent,
                focusedIndicatorColor = Color(0xffe1e1e1),
                unfocusedIndicatorColor = Color(0xffe1e1e1),
            ),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            placeholder = {
                Text(
                    text = "입력 (최대 16글자)",
                    fontSize = 16.sp,
                    color = Color(0xffe6e6e6)
                )
            },
            singleLine = false
        )
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDescription(contentDescription: MutableState<String>) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 20.dp, end = 20.dp)
    ) {
        Text(
            text = "내용 입력",
            fontSize = 14.sp,
            color = Color(0xffa8a8a8)
        )

        TextField(
            value = contentDescription.value,
            onValueChange = {
                if (it.length <= 57) {
                    contentDescription.value = it
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            colors = TextFieldDefaults.textFieldColors(
                containerColor = Color.Transparent,
                textColor = Color.Black,
                disabledIndicatorColor  = Color.Transparent,
                focusedIndicatorColor = Color(0xffe1e1e1),
                unfocusedIndicatorColor = Color(0xffe1e1e1)
            ),
            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
            placeholder = {
                Text(
                    text = "입력 (최대 57글자)",
                    fontSize = 16.sp,
                    color = Color(0xffe6e6e6)
                )
            },
            singleLine = false
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputAddress(address: MutableState<String>) {
    var isAdd by remember { mutableStateOf(false) }

    if (!isAdd) {
        Column(
            modifier = Modifier.clickable(onClick = { isAdd = true }),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "+ 주소 추가",
                fontSize = 16.sp,
                color = Color(0xff123485)
            )
            Spacer(modifier = Modifier.size(10.dp))
            Text(
                text = "가야 될 장소가 있다면 추가 해주세요",
                fontSize = 14.sp,
                color = Color(0xffc4c4c4)
            )
        }
    } else {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Text(
                text = "주소 입력",
                fontSize = 14.sp,
                color = Color(0xffa8a8a8)
            )

            TextField(
                value = address.value,
                onValueChange = {
                    if (it.length <= 50) {
                        address.value = it
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                colors = TextFieldDefaults.textFieldColors(
                    containerColor = Color.Transparent,
                    textColor = Color.Black,
                    disabledIndicatorColor  = Color.Transparent,
                    focusedIndicatorColor = Color(0xffe1e1e1),
                    unfocusedIndicatorColor = Color(0xffe1e1e1)
                ),
                textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                placeholder = {
                    Text(
                        text = "입력 (최대 50글자)",
                        fontSize = 16.sp,
                        color = Color(0xffe6e6e6)
                    )
                },
                singleLine = false
            )
        }
    }


}

@Composable
fun RegistButton(
    contentName: MutableState<String>,
    contentDescription: MutableState<String>,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(94.dp)
                .align(Alignment.BottomCenter)
                .background(Color(0xfff2f1f3))
        ) {
            Box(
                modifier = Modifier
                    .size(342.dp, 61.dp)
                    .align(Alignment.TopCenter)
                    .padding(top = 15.dp)
                    .background(color = Color(0xff123485))
                    .clickable(
                        onClick = onClick,
                        enabled = !(contentName.value.isEmpty() || contentDescription.value.isEmpty())
                    )
            ) {
                Text(
                    text = "등록 하기",
                    fontSize = 16.sp,
                    color = if (contentName.value.isEmpty() || contentDescription.value.isEmpty()) Color.Gray else Color.White,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

        }
    }


}