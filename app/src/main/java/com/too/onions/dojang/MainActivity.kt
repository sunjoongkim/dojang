package com.too.onions.dojang

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.too.onions.dojang.ui.theme.DojangTheme

data class ItemData (
    var imageId: Int,
    var description: String
)

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DojangTheme {
                DrawSingleView()
            }
        }
    }
}

@Composable
fun TitleBar() {
    Surface(
        tonalElevation = 15.dp,
        color = Color(0xfff2f1f3),
        modifier = Modifier
            .fillMaxWidth()
            .height(110.dp)
    ) {
        Row(
            modifier = Modifier
                .size(342.dp, 40.dp)
                .padding(start = 24.dp, top = 60.dp, end = 24.dp, bottom = 10.dp)
        ) {
            Row(
                modifier = Modifier
                    .width(254.dp)
                    .fillMaxHeight()
                    .background(color = Color.Black),
                verticalAlignment = Alignment.CenterVertically

            ) {
                Spacer(modifier = Modifier.size(width = 5.dp, height = 40.dp))
                Image(
                    painterResource(id = R.drawable.ic_emoticon_1),
                    contentScale = ContentScale.None,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp, 24.dp)
                )
                Spacer(modifier = Modifier.size(width = 5.dp, height = 40.dp))
                Text (
                    text = "페이지명 이 페이지에 대한 설명을 적는 곳입니다. 두줄까지 지원합니다.",
                    modifier = Modifier.size(width = 192.dp, height = 26.dp),
                    color = Color.White,
                    fontSize = 9.sp,
                    maxLines = 2,
                    textAlign = TextAlign.Left,
                    lineHeight = 12.sp
                )
                Image(
                    painterResource(id = R.drawable.ic_btn_side_menu),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp, 24.dp),
                    alignment = Alignment.CenterEnd
                )
            }
            Spacer(modifier = Modifier.size(width = 4.dp, height = 40.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color(0xffdddddd))
            ) {
                Image(
                    painterResource(id = R.drawable.ic_emoticon_2),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.size(width = 4.dp, height = 40.dp))
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(color = Color(0xffdddddd))
            ) {
                Image(
                    painterResource(id = R.drawable.ic_add),
                    contentDescription = null,
                    modifier = Modifier
                        .size(24.dp, 24.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}
@Composable
fun AddButton() {
    ElevatedButton(
        onClick = { System.out.println("친구 추가") },
        shape = CircleShape,
        contentPadding = PaddingValues(0.dp),
        elevation = ButtonDefaults.buttonElevation(20.dp, 15.dp, 0.dp, 15.dp, 10.dp),
        modifier = Modifier
            .size(65.dp, 170.dp)
            .padding(start = 25.dp, top = 130.dp)

    ) {
        Icon(
            painterResource(id = R.drawable.ic_btn_add_friend),
            contentDescription = null,
        )
    }
}
@Composable
fun listItem(itemList: List<ItemData>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 190.dp, end = 24.dp, bottom = 100.dp)
    ) {
        items(itemList) {item ->
            Item(item)
        }
    }
}
@Composable
fun Item(itemData: ItemData) {
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier.padding(16.dp)
    ) {
        Image(
            painterResource(id = itemData.imageId),
            contentDescription = null
        )
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            text = itemData.description
        )
    }
}
@Composable
fun DrawSingleView() {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painterResource(id = R.drawable.bg_single),
            contentDescription = null,
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        TitleBar()
        AddButton()

        listItem(items)

        Image(
            painterResource(id = R.drawable.ic_btn_stamp),
            contentDescription = null,
            modifier = Modifier
                .size(72.dp, 72.dp)
                .align(Alignment.BottomStart)
                .padding(start = 20.dp, bottom = 20.dp)
        )

    }



}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DojangTheme {
        DrawSingleView()
        /*Surface(
            modifier = Modifier.fillMaxSize()
        ) {
        }*/
    }
}

val items = listOf(
    ItemData(
        imageId = R.drawable.sample_1,
        description = "첫번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_2,
        description = "두번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_3,
        description = "세번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_4,
        description = "네번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_5,
        description = "다섯번째 샘플 입니다."
    ),
    ItemData(
        imageId = R.drawable.sample_6,
        description = "여섯번째 샘플 입니다."
    )
)