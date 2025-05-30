package com.guardianapp.ui.splash

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.guardianapp.R
import com.guardianapp.ui.theme.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import org.koin.androidx.compose.koinViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun SplashScreen(
    navController: NavController,
    viewModel: SplashViewModel = koinViewModel()
) {
    val systemUiController = rememberSystemUiController()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()

    // Animation states
    val logoScale by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0.8f,
        animationSpec = tween(1000)
    )

    val dotAlpha = remember { Animatable(0f) }
    LaunchedEffect(Unit) {
        dotAlpha.animateTo(
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(500),
                repeatMode = RepeatMode.Reverse
            )
        )
    }

    val textSlideIn by animateFloatAsState(
        targetValue = if (isLoading) 1f else 0f,
        animationSpec = tween(1000)
    )

    SideEffect {
        systemUiController.setStatusBarColor(
            color = BackgroundBlue,
            darkIcons = false
        )
    }

    LaunchedEffect(key1 = true) {
        viewModel.startSplash()
    }

    LaunchedEffect(isLoading) {
        if (!isLoading) {
            val isAuthenticated = viewModel.checkAuthStatus()
            val route = if (isAuthenticated) "home" else "login"
            navController.navigate(route) {
                popUpTo("splash") { inclusive = true }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundBlue),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .scale(logoScale)
                .size(100.dp)
                .background(
                    color = White,
                    shape = CircleShape
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "G",
                fontSize = 48.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.scale(textSlideIn)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_heart),
                contentDescription = "Heart Icon",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color(0xFFE53935))
            )

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Guardian",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )

            Spacer(modifier = Modifier.width(8.dp))

            Image(
                painter = painterResource(id = R.drawable.ic_medical_file),
                contentDescription = "Medical File Icon",
                modifier = Modifier.size(24.dp),
                colorFilter = ColorFilter.tint(Color(0xFF4CAF50))
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            repeat(3) { index ->
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(
                            White.copy(alpha = dotAlpha.value),
                            CircleShape
                        )
                )
            }
        }

        AnimatedVisibility(
            visible = isLoading,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Text(
                text = "Loading...",
                fontSize = 16.sp,
                color = White,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        AnimatedVisibility(
            visible = true,
            enter = slideInVertically() + fadeIn(),
            modifier = Modifier.scale(textSlideIn)
        ) {
            Text(
                text = "\"Your Safety, Always Accessible ✨\"",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = White,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )
        }
    }
}