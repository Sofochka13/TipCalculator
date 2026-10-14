package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    DemoScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

fun calculateDiscount(dishes: Int): Int {
    return if (dishes in 1..2) {
        3
    } else if (dishes in 3..5) {
        5
    } else if (dishes in 6..10) {
        7
    } else if (dishes > 10) {
        10
    } else {
        0
    }
}

@Composable
fun DemoText(message: String, fontSize: TextUnit) {
    Text(
        text = message,
        fontSize = fontSize,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun DemoScreen(modifier: Modifier = Modifier) {

    var orderSum by remember { mutableStateOf("") }
    var dishesCount by remember { mutableStateOf("") }
    var tipPercent by remember { mutableFloatStateOf(0f) }
    var discountPercent by remember { mutableIntStateOf(0) }

    var tipsText by remember { mutableStateOf("") }
    var discountText by remember { mutableStateOf("") }
    var totalText by remember { mutableStateOf("") }

    LaunchedEffect(dishesCount) {
        val dishes = dishesCount.toIntOrNull()
        if (dishes != null && dishes > 0) {
            discountPercent = calculateDiscount(dishes)
        } else {
            discountPercent = 0
        }
    }

    Column(
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        DemoText(message = "Калькулятор чаевых", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(30.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Сумма заказа:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            OutlinedTextField(
                value = orderSum,
                onValueChange = { orderSum = it },
                modifier = Modifier.width(150.dp).background(Color(0xFFFFC0CB)),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Количество блюд:", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(10.dp))
            OutlinedTextField(
                value = dishesCount,
                onValueChange = { dishesCount = it },
                modifier = Modifier.width(100.dp).background(Color(0xFFFFC0CB)),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color.Transparent, unfocusedBorderColor = Color.Transparent)
            )
        }
        Spacer(modifier = Modifier.height(30.dp))

        DemoText(message = "Чаевые: ${tipPercent.toInt()}%", fontSize = 16.sp)
        Slider(
            value = tipPercent,
            onValueChange = { tipPercent = it },
            valueRange = 0f..25f,
            steps = 24,
            modifier = Modifier.fillMaxWidth(),
            colors = SliderDefaults.colors(thumbColor = Color(0xFF27A6F5), activeTrackColor = Color(0xFF27A6F5), inactiveTrackColor = Color(0xFFE0E0E0))
        )
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(text = "0", fontSize = 14.sp)
            Text(text = "25", fontSize = 14.sp)
        }
        Spacer(modifier = Modifier.height(30.dp))

        DemoText(message = "Скидка: $discountPercent%", fontSize = 16.sp)
        Spacer(modifier = Modifier.height(10.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            listOf(3, 5, 7, 10).forEach { percent ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    RadioButton(
                        selected = discountPercent == percent,
                        onClick = null,
                        enabled = false,
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF27A6F5), unselectedColor = Color.Gray)
                    )
                    Text("$percent%", fontSize = 14.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Button(
            onClick = {
                if (orderSum.isEmpty()) {
                    tipsText = "Введите сумму заказа!"
                    discountText = ""
                    totalText = ""
                } else {
                    val sum = orderSum.replace(",", ".").toDoubleOrNull()
                    if (sum == null || sum <= 0) {
                        tipsText = "Ошибка: некорректная сумма!"
                        discountText = ""
                        totalText = ""
                    } else {
                        val tips = sum * (tipPercent / 100)
                        val discount = sum * (discountPercent / 100)
                        val total = sum + tips - discount

                        tipsText = "Чаевые: ${"%.2f".format(tips)} руб."
                        discountText = "Скидка: ${"%.2f".format(discount)} руб."
                        totalText = "Итого к оплате: ${"%.2f".format(total)} руб."
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF27A6F5), contentColor = Color.White),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Рассчитать", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (tipsText.isNotEmpty()) {
            Text(text = tipsText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = discountText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text(text = totalText, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF27A6F5))
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DemoScreenPreview() {
    TipCalculatorTheme {
        DemoScreen()
    }
}