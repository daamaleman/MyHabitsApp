package ni.edu.uam.myhabitsapp.ui

import android.content.res.Configuration
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.animateContentSize
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ni.edu.uam.myhabitsapp.ui.theme.AccentGreen
import ni.edu.uam.myhabitsapp.ui.theme.AccentGreenDark
import ni.edu.uam.myhabitsapp.ui.theme.BackgroundDeep
import ni.edu.uam.myhabitsapp.ui.theme.BorderSubtle
import ni.edu.uam.myhabitsapp.ui.theme.HabitFlowTheme
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceItem
import ni.edu.uam.myhabitsapp.ui.theme.TextPrimary
import ni.edu.uam.myhabitsapp.ui.theme.TextSecondary
import ni.edu.uam.myhabitsapp.data.StoredUser
import ni.edu.uam.myhabitsapp.data.ProfileImageStorage
import ni.edu.uam.myhabitsapp.data.UserLocalStorage
import ni.edu.uam.myhabitsapp.ui.components.UserAvatarImage

private enum class AuthMode { LOGIN, REGISTER }

@Composable
fun LoginScreen(
    viewModel: HabitViewModel,
    onLogin: () -> Unit
) {
    var mode by remember { mutableStateOf(AuthMode.LOGIN) }
    val context = LocalContext.current

    var loginEmail by remember { mutableStateOf("ana@correo.com") }
    var loginPassword by remember { mutableStateOf("12345678") }
    var loginPasswordVisible by remember { mutableStateOf(false) }

    var registerImageUri by remember { mutableStateOf<String?>(null) }
    var registerName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }
    var registerPasswordVisible by remember { mutableStateOf(false) }

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            val localUri = ProfileImageStorage.saveCircularAvatar(context, uri)
            if (localUri != null) {
                registerImageUri = localUri
                viewModel.updateProfileImage(localUri)
            } else {
                Toast.makeText(context, "No se pudo procesar la imagen", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(Unit) {
        val storedUser = UserLocalStorage.loadUser(context)
        if (storedUser != null) {
            loginEmail = storedUser.email
            loginPassword = storedUser.password
            registerName = storedUser.name
            registerEmail = storedUser.email
            registerPassword = storedUser.password
            registerImageUri = storedUser.imageUri
            viewModel.applyRegisteredUser(
                name = storedUser.name,
                email = storedUser.email,
                password = storedUser.password,
                imageUri = storedUser.imageUri
            )
        }
    }

    Surface(color = BackgroundDeep) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .widthIn(max = 520.dp)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Column(
                        modifier = Modifier
                            .shadow(18.dp, RoundedCornerShape(24.dp), clip = false)
                            .clip(RoundedCornerShape(24.dp))
                            .background(Brush.linearGradient(listOf(AccentGreen, AccentGreenDark)))
                            .padding(18.dp)
                    ) {
                        Text(text = "🌱", fontSize = 34.sp)
                    }

                    Spacer(modifier = Modifier.height(22.dp))
                    Text(
                        text = "HabitFlow",
                        color = Color.White,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "Construye mejores hábitos 🚀",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 6.dp, bottom = 24.dp)
                    )
                }

                AuthBubbleNav(
                    selectedMode = mode,
                    onModeChange = { mode = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(420, easing = FastOutSlowInEasing))
                ) {
                    Crossfade(
                        targetState = mode,
                        animationSpec = tween(420, easing = FastOutSlowInEasing),
                        label = "authMode"
                    ) { selectedMode ->
                        when (selectedMode) {
                            AuthMode.LOGIN -> LoginForm(
                                email = loginEmail,
                                password = loginPassword,
                                passwordVisible = loginPasswordVisible,
                                onEmailChange = { loginEmail = it },
                                onPasswordChange = { loginPassword = it },
                                onPasswordVisibilityToggle = { loginPasswordVisible = !loginPasswordVisible },
                                onSubmit = onLogin
                            )

                            AuthMode.REGISTER -> RegisterForm(
                                imageUri = registerImageUri,
                                name = registerName,
                                email = registerEmail,
                                password = registerPassword,
                                passwordVisible = registerPasswordVisible,
                                onPickImage = {
                                    imagePickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                onNameChange = { registerName = it },
                                onEmailChange = { registerEmail = it },
                                onPasswordChange = { registerPassword = it },
                                onPasswordVisibilityToggle = { registerPasswordVisible = !registerPasswordVisible },
                                onSubmit = {
                                    if (registerName.isBlank() || registerEmail.isBlank() || registerPassword.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Completa nombre, correo y contraseña",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        val user = StoredUser(
                                            name = registerName.trim(),
                                            email = registerEmail.trim(),
                                            password = registerPassword,
                                            imageUri = registerImageUri
                                        )
                                        UserLocalStorage.saveUser(context, user)
                                        viewModel.applyRegisteredUser(
                                            name = user.name,
                                            email = user.email,
                                            password = user.password,
                                            imageUri = user.imageUri
                                        )
                                        loginEmail = user.email
                                        loginPassword = user.password
                                        onLogin()
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AuthBubbleNav(
    selectedMode: AuthMode,
    onModeChange: (AuthMode) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(10.dp, RoundedCornerShape(28.dp), clip = false),
        shape = RoundedCornerShape(28.dp),
        color = SurfaceItem,
        border = BorderStroke(1.dp, BorderSubtle)
    ) {
        Row(
            modifier = Modifier.padding(6.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            BubbleTab(
                title = "Login",
                isSelected = selectedMode == AuthMode.LOGIN,
                onClick = { onModeChange(AuthMode.LOGIN) },
                modifier = Modifier.weight(1f)
            )
            BubbleTab(
                title = "Register",
                isSelected = selectedMode == AuthMode.REGISTER,
                onClick = { onModeChange(AuthMode.REGISTER) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun BubbleTab(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) BackgroundDeep else TextSecondary,
        animationSpec = tween(220),
        label = "bubbleTextColor"
    )

    val containerBrush = if (isSelected) {
        Brush.horizontalGradient(listOf(AccentGreen, AccentGreenDark))
    } else {
        Brush.horizontalGradient(listOf(SurfaceItem, SurfaceItem))
    }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
            .background(containerBrush)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = title, color = textColor, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
private fun LoginForm(
    email: String,
    password: String,
    passwordVisible: Boolean,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        AuthTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            leadingIcon = {
                Crossfade(targetState = password.isBlank(), label = "loginLockIcon") { empty ->
                    Icon(
                        imageVector = if (empty) Icons.Default.LockOpen else Icons.Default.Lock,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isPassword = !passwordVisible,
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Ver contraseña"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        GradientActionButton(
            text = "Ingresar →",
            onClick = onSubmit
        )

        TextButton(onClick = { }) {
            Text(text = "¿Olvidaste tu contraseña?", color = AccentGreen)
        }

        Spacer(modifier = Modifier.height(6.dp))
        HorizontalDivider(color = BorderSubtle)
        Spacer(modifier = Modifier.height(18.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = SurfaceItem,
            shape = RoundedCornerShape(14.dp),
            border = BorderStroke(1.dp, BorderSubtle)
        ) {
            Text(
                text = "🌐   Google",
                color = TextSecondary,
                modifier = Modifier.padding(vertical = 14.dp, horizontal = 16.dp)
            )
        }
    }
}

@Composable
private fun RegisterForm(
    imageUri: String?,
    name: String,
    email: String,
    password: String,
    passwordVisible: Boolean,
    onPickImage: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clickable(onClick = onPickImage),
                contentAlignment = Alignment.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(SurfaceItem),
                    contentAlignment = Alignment.Center
                ) {
                    UserAvatarImage(imageUri = imageUri)
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(30.dp)
                        .zIndex(2f),
                    shape = CircleShape,
                    color = AccentGreen,
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AddAPhoto,
                            contentDescription = "Elegir foto",
                            tint = BackgroundDeep,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Toca la foto para cambiarla",
            color = TextSecondary,
            fontSize = 12.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(14.dp))

        AuthTextField(
            value = name,
            onValueChange = onNameChange,
            label = "Nombre",
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = email,
            onValueChange = onEmailChange,
            label = "Correo",
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            )
        )

        Spacer(modifier = Modifier.height(12.dp))

        AuthTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Contraseña",
            leadingIcon = {
                Crossfade(targetState = password.isBlank(), label = "registerLockIcon") { empty ->
                    Icon(
                        imageVector = if (empty) Icons.Default.LockOpen else Icons.Default.Lock,
                        contentDescription = null
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            isPassword = !passwordVisible,
            trailingIcon = {
                IconButton(onClick = onPasswordVisibilityToggle) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                        contentDescription = if (passwordVisible) "Ocultar contraseña" else "Ver contraseña"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        GradientActionButton(
            text = "Crear cuenta →",
            onClick = onSubmit
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    leadingIcon: @Composable () -> Unit,
    keyboardOptions: KeyboardOptions,
    trailingIcon: (@Composable () -> Unit)? = null,
    isPassword: Boolean = false
) {
    var focused by remember { mutableStateOf(false) }
    val borderColor by animateColorAsState(
        targetValue = if (focused) AccentGreen else BorderSubtle,
        animationSpec = tween(250),
        label = "authBorderColor"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { focused = it.isFocused },
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        label = { Text(label) },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = borderColor,
            unfocusedBorderColor = BorderSubtle,
            focusedContainerColor = SurfaceItem,
            unfocusedContainerColor = SurfaceItem,
            focusedTextColor = TextPrimary,
            unfocusedTextColor = TextPrimary,
            focusedLabelColor = AccentGreen,
            unfocusedLabelColor = TextSecondary,
            focusedLeadingIconColor = AccentGreen,
            unfocusedLeadingIconColor = TextSecondary,
            cursorColor = AccentGreen
        ),
        keyboardOptions = keyboardOptions
    )
}

@Composable
private fun GradientActionButton(
    text: String,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = tween(120),
        label = "authButtonScale"
    )

    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clip(RoundedCornerShape(14.dp))
            .background(
                brush = Brush.horizontalGradient(listOf(AccentGreen, AccentGreenDark)),
                shape = RoundedCornerShape(14.dp)
            ),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = BackgroundDeep
        ),
        interactionSource = interactionSource
    ) {
        Text(text = text, fontWeight = FontWeight.ExtraBold)
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun LoginScreenPreview() {
    HabitFlowTheme {
        LoginScreen(
            viewModel = HabitViewModel(),
            onLogin = {}
        )
    }
}


