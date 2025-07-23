package com.example.Sin_Riesgo.screens

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.Sin_Riesgo.R
import com.example.Sin_Riesgo.viewmodels.LoginViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit,
    loginViewModel: LoginViewModel = viewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current as Activity

    // Configuración de Google Sign-In
    val gso = remember {
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .requestProfile()
            .build()
    }
    val googleSignInClient = remember { GoogleSignIn.getClient(context, gso) }

    // Launcher para el resultado de la actividad de Google Sign-In
    val signInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                loginViewModel.signInWithGoogle(account.idToken!!, account.email, account.displayName)
            } catch (e: ApiException) {
                Toast.makeText(context, "Error al iniciar sesión con Google: ${e.statusCode}", Toast.LENGTH_LONG).show()
            } catch (e: Exception) {
                Toast.makeText(context, "Error inesperado: ${e.message}", Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, "Inicio de sesión con Google cancelado.", Toast.LENGTH_SHORT).show()
        }
    }

    // Observar eventos del ViewModel
    LaunchedEffect(Unit) {
        loginViewModel.loginEvent.collect { event ->
            when (event) {
                LoginViewModel.LoginEvent.Success -> {
                    onLoginSuccess()
                }
                is LoginViewModel.LoginEvent.Error -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                is LoginViewModel.LoginEvent.Message -> {
                    snackbarHostState.showSnackbar(event.message)
                }
                LoginViewModel.LoginEvent.LoggedOut -> {
                    onLoginSuccess()
                }
            }
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(hostState = snackbarHostState) }) { paddingValues ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color(0xFF050533))
                .padding(paddingValues)
                .padding(horizontal = 30.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 100.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                LoginBody(
                    loginViewModel = loginViewModel,
                    onGoogleSignInClick = { signInLauncher.launch(googleSignInClient.signInIntent) },
                    onPhoneNumberClick = { loginViewModel.requestPhoneOtp() }
                )
            }
            LoginFooter(Modifier.align(Alignment.BottomCenter))
        }
    }

    if (loginViewModel.showPhoneOtpDialog) {
        PhoneVerificationDialog(
            phoneNumber = loginViewModel.phoneNumber,
            otpCode = loginViewModel.otpCode,
            onOtpChange = loginViewModel::onOtpCodeChange,
            onVerifyClick = loginViewModel::verifyPhoneOtp,
            onDismiss = loginViewModel::dismissPhoneOtpDialog,
            isLoading = loginViewModel.isLoading
        )
    }
}

@Composable
fun LoginFooter(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            "Registrarse con",
            fontSize = 14.sp,
            color = Color.White,
            fontWeight = FontWeight.Normal
        )
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SocialButton(icon: Painter, text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(38.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.White,
            contentColor = Color.Black
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = icon,
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(text)
        }
    }
}

@Composable
fun LoginBody(
    loginViewModel: LoginViewModel,
    onGoogleSignInClick: () -> Unit,
    onPhoneNumberClick: () -> Unit
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        LoginLogo(Modifier)
        Spacer(Modifier.size(40.dp))
        LoginEmailField(
            email = loginViewModel.email,
            onTextChanged = loginViewModel::onEmailChange
        )
        Spacer(Modifier.size(16.dp))
        LoginPasswordField(
            password = loginViewModel.password,
            onTextChanged = loginViewModel::onPasswordChange,
            showPassword = loginViewModel.showPassword,
            toggleShowPassword = loginViewModel::toggleShowPassword
        )
        LoginForgotPassword()
        Spacer(modifier = Modifier.size(10.dp))
        LoginButton(
            loginEnable = loginViewModel.isLoginEnabled,
            isLoading = loginViewModel.isLoading,
            modifier = Modifier.fillMaxWidth(),
            onClick = loginViewModel::login
        )

        Spacer(modifier = Modifier.size(20.dp))
        HorizontalDivider(
            Modifier
                .background(Color(0xFFFAFAFA))
                .height(1.dp)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        SocialButton(
            icon = painterResource(id = R.drawable.google),
            text = "Continuar con Google",
            onClick = onGoogleSignInClick
        )
        Spacer(modifier = Modifier.height(12.dp))

        SocialButton(
            icon = painterResource(id = R.drawable.peru),
            text = "Continuar con número",
            onClick = onPhoneNumberClick
        )
    }
}

@Composable
fun LoginButton(loginEnable: Boolean, isLoading: Boolean, modifier: Modifier, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = loginEnable && !isLoading,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF000000),
            contentColor = Color.White,
            disabledContainerColor = Color(0xFF212121),
            disabledContentColor = Color.White
        )
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
        } else {
            Text("Iniciar Sesión")
        }
    }
}

@Composable
fun LoginForgotPassword(modifier: Modifier = Modifier) {
    Text(
        text = "Olvidaste tu contraseña?",
        fontSize = 12.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF4EA8E9),
        modifier = modifier
            .fillMaxWidth()
            .wrapContentWidth(Alignment.End)
    )
}

@Composable
fun LoginPasswordField(
    password: String,
    onTextChanged: (String) -> Unit,
    showPassword: Boolean,
    toggleShowPassword: () -> Unit
) {
    TextField(
        value = password,
        onValueChange = onTextChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Contraseña") },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFF050533),
            unfocusedIndicatorColor = Color(0xFF050533),
        ),
        trailingIcon = {
            val icon = if (showPassword) {
                Icons.Filled.Visibility
            } else {
                Icons.Filled.VisibilityOff
            }
            IconButton(onClick = toggleShowPassword) {
                Icon(icon, contentDescription = "Toggle password visibility")
            }
        },
        visualTransformation = if (showPassword) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        }
    )
}

@Composable
fun LoginEmailField(email: String, onTextChanged: (String) -> Unit) {
    TextField(
        value = email,
        onValueChange = onTextChanged,
        modifier = Modifier.fillMaxWidth(),
        label = { Text("Email o Número de celular") },
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color(0xFF050533),
            unfocusedIndicatorColor = Color(0xFF050533)
        ),
        trailingIcon = {
            Icon(imageVector = Icons.Default.Email, contentDescription = "Email Icon")
        }
    )
}

@Composable
fun LoginLogo(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.alerta),
            contentDescription = "Logo Login",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "SIN",
            fontSize = 36.sp,
            color = Color.White,
            fontWeight = FontWeight.Light
        )
        Text(
            text = "RIESGO",
            fontSize = 48.sp,
            color = Color.White,
            fontWeight = FontWeight.Light
        )
    }
}

@Composable
fun PhoneVerificationDialog(
    phoneNumber: String,
    otpCode: String,
    onOtpChange: (String) -> Unit,
    onVerifyClick: () -> Unit,
    onDismiss: () -> Unit,
    isLoading: Boolean
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Verificar número de teléfono", color = Color.Black) },
        text = {
            Column {
                Text("Se ha enviado un código a $phoneNumber. Ingresa el código a continuación:", color = Color.Black)
                Spacer(modifier = Modifier.height(16.dp))
                TextField(
                    value = otpCode,
                    onValueChange = onOtpChange,
                    label = { Text("Código OTP") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    enabled = !isLoading
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onVerifyClick,
                enabled = otpCode.isNotBlank() && !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Text("Verificar")
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss, enabled = !isLoading) {
                Text("Cancelar")
            }
        },
        containerColor = Color.White,
        modifier = Modifier.padding(16.dp)
    )
}