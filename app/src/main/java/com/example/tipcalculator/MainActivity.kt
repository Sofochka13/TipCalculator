package com.example.tipcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
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
    DemoText(
        message = "Калькулятор чаевых",
        fontSize = 24.sp
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DemoScreenPreview() {
    TipCalculatorTheme {
        DemoScreen()
    }
}