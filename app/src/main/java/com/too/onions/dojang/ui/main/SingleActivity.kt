package com.too.onions.dojang.ui.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.too.onions.dojang.R
import com.too.onions.dojang.ui.theme.DojangTheme


data class ItemData (
    var imageId: Int,
    var description: String
)

class SingleActivity : ComponentActivity() {
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

// region
// Composable 영역

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
    var btnAdd = ItemData(-1, "")
    var extendedItemList = itemList + btnAdd

    var isNeedInit by remember { mutableStateOf(false) }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 24.dp, top = 190.dp, end = 24.dp, bottom = 86.dp),
        verticalArrangement = Arrangement.spacedBy(25.dp)
    ) {
        items(extendedItemList) {item ->

            if (item.imageId != -1) {
                Item(item)
            } else {
                Box(
                    modifier = Modifier
                        .size(150.dp, 150.dp)
                        .padding(end = 24.dp)
                ) {
                    Button(
                        onClick = {
                            // 버튼을 클릭했을 때 수행할 동작 작성

                            // 타이틀영역이 작성되지 않았을경우
                            isNeedInit = true
                        },
                        modifier = Modifier
                            .fillMaxSize()
                            .border(1.dp, Color(0x20000000), RectangleShape)
                            .background(color = Color.Transparent),
                        colors = ButtonDefaults.buttonColors(Color.Transparent)
                    ) {
                        Icon(
                            painterResource(id = R.drawable.ic_add_bucket),
                            contentDescription = null,
                            tint = Color(0xffa0a0a0)
                        )
                    }
                }
            }
        }
    }
    // 초기 세팅이 필요한 경우 다이얼로그 발생
    if (isNeedInit) {
        InitTitleDialog(onDismiss = { isNeedInit = false })
    }
}
@Composable
fun Item(itemData: ItemData) {
    Box(
        modifier = Modifier.size(150.dp, 165.dp)
    ) {
        Image(
            painterResource(id = itemData.imageId),
            contentDescription = null,
            modifier = Modifier
                .size(150.dp, 150.dp)
                .border(1.dp, Color(0xff123485), RectangleShape)
        )

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
                text = itemData.description,
                fontSize = 10.sp,
                color = Color(0xff123485)
            )
        }

    }
}
@Composable
fun BottomBar() {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(76.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(Color(0xfff3fff4), Color(0xffc2ffd3)),
                        startY = 0f,
                        endY = 500f
                    )
                )
                .padding(start = 270.dp, top = 15.dp)
        ) {
            Image(
                painterResource(id = R.drawable.ic_noti),
                contentDescription = null
            )

            Spacer(modifier = Modifier.size(25.dp))
            
            Image(
                painterResource(id = R.drawable.ic_setting),
                contentDescription = null
            )
        }
    }
}
@Composable
fun StampButton() {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 20.dp, bottom = 18.dp)
    ) {
        Image(
            painterResource(id = R.drawable.ic_btn_stamp),
            contentDescription = null,
            modifier = Modifier
                .size(72.dp, 72.dp)
                .align(Alignment.BottomStart)
        )
    }
}
@Composable
fun InitTitleDialog(onDismiss: () -> Unit) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        Column(
            modifier = Modifier
                .size(320.dp, 220.dp)
                .background(color = Color(0xfff3f2f4), shape = RectangleShape)
                .border(2.dp, color = Color(0xff242424)),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painterResource(id = R.drawable.bg_pattern_dialog_png),
                contentDescription = null,
                modifier = Modifier.size(312.dp, 20.dp)
            )

            Spacer(modifier = Modifier.size(50.dp))

            Text(
                text = "도장깨기 페이지명을\n먼저 등록해 주세요",
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.size(50.dp))

            Row {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.size(120.dp, 46.dp)
                        .background(color = Color(0xff61b476), shape = RectangleShape)
                        .border(2.dp, color = Color(0xff17274e))
                ) {
                    Text(
                        text = "닫기",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                    )
                }

                Spacer(modifier = Modifier.size(10.dp))

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    modifier = Modifier.size(120.dp, 46.dp)
                        .background(color = Color(0xff123485), shape = RectangleShape)
                        .border(2.dp, color = Color(0xff17274e))
                ) {
                    Text(
                        text = "등록하기",
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.neo_dunggeunmo_pro))
                    )
                }
            }
        }
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
        BottomBar()
        StampButton()
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DojangTheme {
        DrawSingleView()
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

// endregion