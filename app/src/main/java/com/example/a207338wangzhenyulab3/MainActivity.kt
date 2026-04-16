package com.example.a207338wangzhenyulab3



import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import ui.theme.HabitTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {//设置页面内容，应用自定义主题
            HabitTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    // 搜索框输入内容（用户打字的内容）
    // remember：记住状态，防止界面刷新时丢失
    // mutableStateOf：可变状态，变化时UI会自动更新

    var searchText by remember { mutableStateOf("") }
    var searchMessage by remember { mutableStateOf("") }
    val searchResults = remember { mutableStateListOf<String>() }
    val habits = listOf(
        "Drink 2000ml Water",
        "Morning Walk",
        "Stretch for 10 Minutes",
        "Sleep Early",
        "Eat Healthy Breakfast",
        "Track Calories",
        "Walk 8000 Steps",
        "Meditation",
        "Exercise 30 Minutes",
        "Morning Routine"
    )

    var challengeStage by remember { mutableStateOf(0) }
    var challengeSubtitle by remember { mutableStateOf("Build a better routine") }

    var weightValue by remember { mutableStateOf("90.00 kg") }
    var caloriesValue by remember { mutableStateOf("1809 kcal") }
    var waterValue by remember { mutableStateOf("0 ml") }
    var stepsValue by remember { mutableStateOf("--") }

    var routineDone by remember { mutableStateOf(false) }

    var dialogType by remember { mutableStateOf("") }
    var dialogInput by remember { mutableStateOf("") }
// 弹窗输入内容（用户在弹窗中输入的数据）
    Box(
        modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)// 避开状态栏/刘海
            .background(MaterialTheme.colorScheme.background) // 设置背景颜色
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            HomeScreen(
                searchText = searchText,
                onSearchTextChange = { searchText = it },// 当用户输入时触发（实时更新 searchText）
                onSearchClick = {
                    searchResults.clear()

                    if (searchText.isBlank()) {
                        searchMessage = "Please enter a habit"// 提示用户输入
                    } else {
                        val matched = habits.filter { // 在 habits 列表中筛选符合条件的内容
                            it.contains(searchText.trim(), ignoreCase = true)// contains：包含匹配
                            // trim：去掉前后空格
                            // ignoreCase：忽略大小写
                        }

                        if (matched.isEmpty()) {
                            searchMessage = "No result found"
                        } else {
                            searchMessage = "Search results"
                            searchResults.addAll(matched) // 把匹配到的结果加入到列表中（用于显示）
                        }
                    }
                },
                onClearSearch = {
                    searchText = ""
                    searchMessage = ""
                    searchResults.clear()
                },
                searchMessage = searchMessage,
                searchResults = searchResults,

                challengeStage = challengeStage,
                challengeSubtitle = challengeSubtitle,
                onGoClick = {
                    challengeStage = (challengeStage + 1) % 7
                    challengeSubtitle = when {
                        challengeStage == 0 -> "Build a better routine"
                        challengeStage in 1..5 -> "Challenge in progress"
                        else -> "Completed"
                    }
                },

                weightValue = weightValue,
                caloriesValue = caloriesValue,
                waterValue = waterValue,
                stepsValue = stepsValue,
                onWeightClick = {
                    dialogType = "weight"
                    dialogInput = weightValue.replace(" kg", "") // 去掉 kg，只留下数字
                },
                onCaloriesClick = {
                    dialogType = "calories"
                    dialogInput = caloriesValue.replace(" kcal", "")
                },
                onWaterClick = {
                    dialogType = "water"
                    dialogInput = waterValue.replace(" ml", "")
                },
                onStepsClick = {
                    dialogType = "steps"
                    dialogInput = if (stepsValue == "--") "" else stepsValue
                },    // 如果当前是 "--"（没有数据），就给空字符串
                // 否则就用当前步数

                routineDone = routineDone,
                onRoutineClick = { routineDone = !routineDone },

                modifier = Modifier
                    .weight(1f)// 在 Column 里占剩余空间
                    .padding(16.dp)
            )

            BottomBar()
        }

        FloatingAddButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)// 对齐到右下角
                .padding(end = 20.dp, bottom = 74.dp)
        )
    }

    if (dialogType.isNotEmpty()) {
        AlertDialog(
            onDismissRequest = { dialogType = "" },    // 点击外面关闭弹窗
            title = {
                Text(
                    when (dialogType) {
                        "weight" -> "Update Weight"
                        "calories" -> "Update Calories"
                        "water" -> "Update Water"
                        "steps" -> "Update Steps"
                        else -> "Update Value"
                    }
                )
            },
            text = {
                OutlinedTextField(
                    value = dialogInput,
                    onValueChange = { dialogInput = it },
                    label = {
                        Text(
                            when (dialogType) {
                                "weight" -> "Weight (kg)"
                                "calories" -> "Calories (kcal)"
                                "water" -> "Water (ml)"
                                "steps" -> "Steps"
                                else -> "Value"
                            }
                        )
                    },
                    singleLine = true
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (dialogInput.isNotBlank()) {
                            when (dialogType) {//+单位
                                "weight" -> weightValue = "$dialogInput kg"
                                "calories" -> caloriesValue = "$dialogInput kcal"
                                "water" -> waterValue = "$dialogInput ml"
                                "steps" -> stepsValue = dialogInput
                            }
                        }
                        dialogType = ""// 关闭弹窗
                        dialogInput = "" // 清空输入框
                    }
                ) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogType = ""
                        dialogInput = ""
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun HomeScreen(
    searchText: String,//输入框内容
    onSearchTextChange: (String) -> Unit,
    onSearchClick: () -> Unit,
    onClearSearch: () -> Unit,
    searchMessage: String, // 提示信息
    searchResults: List<String>, // 搜索结果

    challengeStage: Int,
    challengeSubtitle: String,// 显示文字
    onGoClick: () -> Unit,// 点击GO按钮

    weightValue: String,
    caloriesValue: String,
    waterValue: String,
    stepsValue: String,
    onWeightClick: () -> Unit,
    onCaloriesClick: () -> Unit,
    onWaterClick: () -> Unit,
    onStepsClick: () -> Unit,

    routineDone: Boolean,
    onRoutineClick: () -> Unit,

    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) } //控制下拉菜单是否展开（不过你目前没真正用到）

    Column(//第一行：头像 + 搜索框（横向排列）
        modifier = modifier.verticalScroll(rememberScrollState())// 支持上下滑动
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.user),
                contentDescription = "Profile",//无障碍
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop// 图片填充裁剪
            )

            Spacer(modifier = Modifier.width(10.dp))

            Box(//搜索框
                modifier = Modifier
                    .weight(1f)// 占据Row剩余空间（让搜索框变长）
                    .background(
                        MaterialTheme.colorScheme.surfaceVariant,//背景颜色
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 10.dp)// 内边距
            ) {
                Row(// 搜索框内部一行（图标 + 输入区域）
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.search),
                        contentDescription = "Search",
                        modifier = Modifier
                            .size(18.dp)
                            .clickable { onSearchClick() },// 点击图标触发搜索
                        contentScale = ContentScale.Fit// 图片适应显示
                    )

                    Spacer(modifier = Modifier.width(8.dp))  // 图标和输入框之间的间距

                    Box(modifier = Modifier.weight(1f)) {
                        if (searchText.isEmpty()) {
                            Text(
                                text = "Search healthy habits...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,// 提示文字颜色
                                fontSize =14.sp
                            )
                        }

                        BasicTextField(// 实际输入框
                            value = searchText,
                            onValueChange = onSearchTextChange,
                            singleLine = true,
                            textStyle = TextStyle(
                                color = MaterialTheme.colorScheme.onSurface,//文字颜色
                                fontSize = 14.sp
                            ),
                            modifier = Modifier.fillMaxWidth()// 占满输入区域宽度
                        )
                    }

                    if (searchText.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(8.dp))  // 输入框和 X 按钮之间的间距
                        Text(
                            text = "✕",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 14.sp,
                            modifier = Modifier.clickable { onClearSearch() }// 点击后清空输入内容
                        )
                    }
                }
            }
        }

        if (searchMessage.isNotEmpty()) {// 如果有搜索提示信息
            Spacer(modifier = Modifier.height(8.dp))// 上下留一点间距
            Text(
                text = searchMessage,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 13.sp
            )
        }

        if (searchResults.isNotEmpty()) {
            Spacer(modifier = Modifier.height(6.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(12.dp) // 圆角
                    )
                    .padding(10.dp)
            ) {
                searchResults.forEach { result ->
                    Text(
                        text = "• $result",// 前面加一个点（列表效果）
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 14.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))  // 每一条之间的间距
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))// 上方留间距

        Text(
            text = "Habit Develop",
            fontSize = 26.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp,// 字母间距（让文字更舒服）
            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.85f)// 主色 + 半透明
        )

        Text(
            text = "Build healthy lifestyle",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))// 标题和图片之间间距

        Image(
            painter = painterResource(id = R.drawable.healthy_banner),
            contentDescription = "Healthy lifestyle banner",
            modifier = Modifier
                .fillMaxWidth()
                .height(170.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop// 图片裁剪填充（铺满）
        )

        Spacer(modifier = Modifier.height(16.dp))// 图片下方间距

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.surface,
                    RoundedCornerShape(16.dp)
                )
                .clickable { expanded = !expanded }// 点击切换展开/收起状态
                .padding(12.dp)// 内边距
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically// 垂直居中
            ) {
                Image(
                    painter = painterResource(id = R.drawable.walk),
                    contentDescription = "Challenge", // 无障碍描述
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(10.dp))//图标和文字之间的间距


                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "7-Day Challenge",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = challengeSubtitle,// 状态变量（会变化：Build / In progress / Completed）
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.primary,// 按钮背景颜色
                            RoundedCornerShape(20.dp)
                        )
                        .clickable {
                            onGoClick()// 执行点击逻辑
                            expanded = !expanded// 切换展开/收起状态
                        }
                        .padding(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "GO",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            AnimatedVisibility(visible = expanded) {//带动画的显示区域（展开/收起）
                Column(
                    modifier = Modifier.padding(top = 10.dp)// 和上面卡片留间距
                ) {
                    Spacer(modifier = Modifier.height(8.dp)) // 上方间距

                    Text(//显示天数
                        text = "Day ${challengeStage + 1} / 7",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text( // 显示进度百分比
                        text = "Progress: ${((challengeStage + 1) * 100) / 7}%",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))// 上方间距

        Text(
            text = "Health Data",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.85f)
        )

        Spacer(modifier = Modifier.height(10.dp))// 标题和卡片之间间距

        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                HealthCard(
                    title = "Weight",
                    subtitle = "395 days ago",
                    value = weightValue,
                    imageRes = R.drawable.weight,
                    modifier = Modifier
                        .weight(1f)//占一半宽度
                        .clickable { onWeightClick() }
                )

                Spacer(modifier = Modifier.width(10.dp)) // 卡片之间间距

                HealthCard(
                    title = "Calories",
                    subtitle = "No record today",
                    value = caloriesValue,
                    imageRes = R.drawable.kcal,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onCaloriesClick() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                HealthCard(
                    title = "Water",
                    subtitle = "Daily target",
                    value = waterValue,
                    imageRes = R.drawable.water,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onWaterClick() }
                )

                Spacer(modifier = Modifier.width(10.dp))

                HealthCard(
                    title = "Steps",
                    subtitle = "Not connected",
                    value = stepsValue,
                    imageRes = R.drawable.walk,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onStepsClick() }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Routine",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    if (routineDone) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.tertiaryContainer,
                    RoundedCornerShape(12.dp)
                )
                .clickable { onRoutineClick() }
                .padding(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(id = R.drawable.water),
                    contentDescription = "Routine image",
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = if (routineDone) "Completed" else "08:30 AM",
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (routineDone) "Morning routine done" else "Morning routine",
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = if (routineDone) "Completed today" else "Drink water and stretch",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(90.dp))
    }
}

@Composable
fun HealthCard(
    title: String,
    subtitle: String,
    value: String,
    imageRes: Int,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(150.dp)
            .background(
                MaterialTheme.colorScheme.surface,//背景颜色
                RoundedCornerShape(16.dp)
            )
            .padding(12.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,// 主文字颜色
                fontSize = 16.sp,//字体大小
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )

            Spacer(modifier = Modifier.weight(1f))//占据剩余空间，把下面内容推到底部

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = value,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )

                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = title,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(10.dp)),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}

@Composable
fun BottomBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(top = 10.dp, bottom = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        BottomNavItem(
            text = "Record",
            iconRes = R.drawable.record,
            selected = true
        )

        BottomNavItem(
            text = "Square",
            iconRes = R.drawable.community,
            selected = false
        )

        BottomNavItem(
            text = "Center",
            iconRes = R.drawable.center,
            selected = false
        )

        BottomNavItem(
            text = "Store",
            iconRes = R.drawable.shop,
            selected = false
        )

        BottomNavItem(
            text = "Me",
            iconRes = R.drawable.me,
            selected = false
        )
    }
}

@Composable
fun BottomNavItem(
    text: String,
    iconRes: Int,
    selected: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = text,
            modifier = Modifier
                .size(22.dp)
                .clip(RoundedCornerShape(6.dp)),
            contentScale = ContentScale.Fit //图片完整显示
        )

        Spacer(modifier = Modifier.height(6.dp))//图标和文字之间的间距

        Text(
            text = text,
            fontSize = 11.sp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.onSurfaceVariant
            },
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
fun FloatingAddButton(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(68.dp)//按钮整体大小
            .background(MaterialTheme.colorScheme.primary, CircleShape),//背景颜色（主题主色）
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "+",
            color = MaterialTheme.colorScheme.onPrimary,
            fontSize = 34.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScreen() {
    HabitTheme {
        MainScreen()
    }
}