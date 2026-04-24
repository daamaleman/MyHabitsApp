package ni.edu.uam.myhabitsapp.ui

import android.content.res.Configuration
import android.net.Uri
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ni.edu.uam.myhabitsapp.data.ProfileImageStorage
import ni.edu.uam.myhabitsapp.data.StoredUser
import ni.edu.uam.myhabitsapp.data.UserLocalStorage
import ni.edu.uam.myhabitsapp.ui.components.UserAvatarImage
import ni.edu.uam.myhabitsapp.ui.theme.AccentGreen
import ni.edu.uam.myhabitsapp.ui.theme.AccentGreenDark
import ni.edu.uam.myhabitsapp.ui.theme.BackgroundDeep
import ni.edu.uam.myhabitsapp.ui.theme.BorderSubtle
import ni.edu.uam.myhabitsapp.ui.theme.HabitFlowTheme
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceItem
import ni.edu.uam.myhabitsapp.ui.theme.TextPrimary
import ni.edu.uam.myhabitsapp.ui.theme.TextSecondary

private enum class AuthMode { LOGIN, REGISTER }

@Composable
fun LoginScreen(
    viewModel: HabitViewModel,
    onLogin: () -> Unit
) {
    var mode by remember { mutableStateOf(AuthMode.LOGIN) }
    var isLoggingIn by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var loginEmail by remember { mutableStateOf("") }
    var loginPassword by remember { mutableStateOf("") }
    var loginPasswordVisible by remember { mutableStateOf(false) }

    var registerImageUri by remember { mutableStateOf<String?>(null) }
    var registerName by remember { mutableStateOf("") }
    var registerEmail by remember { mutableStateOf("") }
    var registerPassword by remember { mutableStateOf("") }
    var registerPasswordVisible by remember { mutableStateOf(false) }
    val layoutDirection = LocalLayoutDirection.current
    val contentMaxWidth = 520.dp
    val horizontalScreenPadding = 24.dp

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
        val emailForCurrentIp = UserLocalStorage.loadLoginEmailForCurrentIp(context)

        loginEmail = emailForCurrentIp.ifBlank { storedUser?.email.orEmpty() }
        loginPassword = ""

        if (storedUser != null) {
            viewModel.applyRegisteredUser(
                name = storedUser.name,
                email = storedUser.email,
                password = storedUser.password,
                imageUri = storedUser.imageUri
            )
        }
    }

    Scaffold(containerColor = BackgroundDeep) { paddingValues ->
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = contentMaxWidth)
                    .verticalScroll(rememberScrollState())
                    .padding(
                        PaddingValues(
                            start = horizontalScreenPadding + paddingValues.calculateLeftPadding(layoutDirection),
                            top = 18.dp + paddingValues.calculateTopPadding(),
                            end = horizontalScreenPadding + paddingValues.calculateRightPadding(layoutDirection),
                            bottom = 24.dp + paddingValues.calculateBottomPadding()
                        )
                    ),
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
                        color = TextPrimary,
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

                AuthBubbleNav(selectedMode = mode, onModeChange = { mode = it })

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateContentSize(animationSpec = tween(420, easing = FastOutSlowInEasing))
                ) {
                    AnimatedContent(
                        targetState = mode,
                        transitionSpec = {
                            val enterDirection = if (targetState == AuthMode.REGISTER) 1 else -1
                            val exitDirection = if (targetState == AuthMode.REGISTER) -1 else 1
                            (
                                slideInHorizontally(
                                    animationSpec = tween(450, easing = FastOutSlowInEasing)
                                ) { width -> width / 20 * enterDirection } +
                                    fadeIn(animationSpec = tween(400))
                                ).togetherWith(
                                slideOutHorizontally(
                                    animationSpec = tween(350, easing = FastOutSlowInEasing)
                                ) { width -> width / 24 * exitDirection } +
                                    fadeOut(animationSpec = tween(300))
                            )
                        },
                        label = "authMode"
                    ) {
selectedMode ->
                        when (selectedMode) {
                            AuthMode.LOGIN -> LoginForm(
                                email = loginEmail,
                                password = loginPassword,
                                passwordVisible = loginPasswordVisible,
                                isAnimating = isLoggingIn,
                                onEmailChange = { loginEmail = it },
                                onPasswordChange = { loginPassword = it },
                                onPasswordVisibilityToggle = { loginPasswordVisible = !loginPasswordVisible },
                                onSubmit = {
                                    val normalizedEmail = loginEmail.trim()
                                    val normalizedPassword = loginPassword
                                    val storedUser = UserLocalStorage.loadUser(context)

                                    when {
                                        storedUser == null -> {
                                            Toast.makeText(
                                                context,
                                                "No hay usuario registrado. Crea una cuenta primero",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        normalizedEmail.isBlank() || normalizedPassword.isBlank() -> {
                                            Toast.makeText(
                                                context,
                                                "Ingresa correo y contrasena",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        !normalizedEmail.equals(storedUser.email, ignoreCase = true) ||
                                            normalizedPassword != storedUser.password -> {
                                            Toast.makeText(
                                                context,
                                                "Correo o contrasena incorrectos",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        else -> {
                                            UserLocalStorage.saveLoginEmailForCurrentIp(
                                                context = context,
                                                email = normalizedEmail
                                            )
                                            viewModel.applyRegisteredUser(
                                                name = storedUser.name,
                                                email = storedUser.email,
                                                password = storedUser.password,
                                                imageUri = storedUser.imageUri
                                            )
                                            loginPassword = ""
                                            coroutineScope.launch {
                                                isLoggingIn = true
                                                delay(1200)
                                                onLogin()
                                            }
                                        }
                                    }
                                }
                            )

                            AuthMode.REGISTER -> RegisterForm(
                                imageUri = registerImageUri,
                                name = registerName,
                                email = registerEmail,
                                password = registerPassword,
                                passwordVisible = registerPasswordVisible,
                                isAnimating = isLoggingIn,
                                onPickImage = {
                                    imagePickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                },
                                onNameChange = { registerName = capitalizeFirstLetter(it) },
                                onEmailChange = { registerEmail = it },
                                onPasswordChange = { registerPassword = it },
                                onPasswordVisibilityToggle = { registerPasswordVisible = !registerPasswordVisible },
                                onSubmit = {
                                    val normalizedName = capitalizeFirstLetter(registerName)
                                    val normalizedEmail = registerEmail.trim().lowercase()
                                    val normalizedPassword = registerPassword.trim()
                                    val storedUser = UserLocalStorage.loadUser(context)

                                    when {
                                        normalizedName.isBlank() -> {
                                            Toast.makeText(context, "Escribe tu nombre", Toast.LENGTH_SHORT).show()
                                        }

                                        !isValidEmail(normalizedEmail) -> {
                                            Toast.makeText(context, "Ingresa un correo valido", Toast.LENGTH_SHORT).show()
                                        }

                                        !isValidRegisterPassword(normalizedPassword) -> {
                                            Toast.makeText(
                                                context,
                                                "La contrasena debe ser alfanumerica y tener al menos 8 caracteres",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        storedUser != null && normalizedEmail == storedUser.email.lowercase() -> {
                                            Toast.makeText(
                                                context,
                                                "Ese correo ya fue registrado",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }

                                        else -> {
                                            val user = StoredUser(
                                                name = normalizedName,
                                                email = normalizedEmail,
                                                password = normalizedPassword,
                                                imageUri = registerImageUri
                                            )
                                            UserLocalStorage.saveUser(context, user)
                                            UserLocalStorage.saveLoginEmailForCurrentIp(
                                                context = context,
                                                email = user.email
                                            )
                                            viewModel.applyRegisteredUser(
                                                name = user.name,
                                                email = user.email,
                                                password = user.password,
                                                imageUri = user.imageUri
                                            )
                                            loginEmail = user.email
                                            loginPassword = ""
                                            registerName = ""
                                            registerEmail = ""
                                            registerPassword = ""
                                            registerImageUri = null
                                            registerPasswordVisible = false
                                            coroutineScope.launch {
                                                isLoggingIn = true
                                                delay(1200)
                                                mode = AuthMode.LOGIN
                                                onLogin()
                                            }
                                        }
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
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .padding(6.dp)
        ) {
            val tabSpacing = 8.dp
            val pillWidth = (maxWidth - tabSpacing) / 2
            val pillOffset by animateDpAsState(
                targetValue = if (selectedMode == AuthMode.LOGIN) 0.dp else pillWidth + tabSpacing,
                animationSpec = tween(320, easing = FastOutSlowInEasing),
                label = "authPillOffset"
            )

            Box {
                Surface(
                    modifier = Modifier
                        .offset(x = pillOffset)
                        .size(width = pillWidth, height = 44.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = Color.Transparent,
                    border = BorderStroke(1.dp, BorderSubtle)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Brush.horizontalGradient(listOf(AccentGreen, AccentGreenDark)))
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(tabSpacing)
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

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(22.dp))
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
    isAnimating: Boolean,
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
            placeholderText = "ejemplo@correo.com",
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
            label = "Contrasena",
            placeholderText = "Ej: Abc12345",
            leadingIcon = {
                Crossfade(targetState = password.isBlank(), animationSpec = tween(400), label = "loginLockIcon") { isBlank ->
                    Icon(
                        imageVector = if (isBlank) Icons.Default.LockOpen else Icons.Default.Lock,
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
                        contentDescription = if (passwordVisible) "Ocultar contrasena" else "Ver contrasena"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        GradientActionButton(text = "Ingresar", isAnimating = isAnimating, onClick = onSubmit)

        TextButton(onClick = { }) {
            Text(text = "Olvidaste tu contrasena?", color = AccentGreen)
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
                text = "Google",
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
    isAnimating: Boolean,
    onPickImage: () -> Unit,
    onNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onPasswordVisibilityToggle: () -> Unit,
    onSubmit: () -> Unit
) {
    Column {
        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
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
            placeholderText = "Ej: Ana",
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
            placeholderText = "ejemplo@correo.com",
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
            label = "Contrasena",
            placeholderText = "Ej: Abc12345",
            leadingIcon = {
                Crossfade(targetState = password.isBlank(), animationSpec = tween(400), label = "registerLockIcon") { isBlank ->
                    Icon(
                        imageVector = if (isBlank) Icons.Default.LockOpen else Icons.Default.Lock,
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
                        contentDescription = if (passwordVisible) "Ocultar contrasena" else "Ver contrasena"
                    )
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        GradientActionButton(text = "Crear cuenta", isAnimating = isAnimating, onClick = onSubmit)

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "El nombre inicia en mayuscula automaticamente. Correo valido. Contrasena alfanumerica de al menos 8 caracteres.",
            color = TextSecondary.copy(alpha = 0.65f),
            fontSize = 10.sp,
            lineHeight = 12.sp,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholderText: String? = null,
    leadingIcon: @Composable () -> Unit,
    keyboardOptions: KeyboardOptions,
    trailingIcon: (@Composable () -> Unit)? = null,
    isPassword: Boolean = false
) {
    var isFocused by remember { mutableStateOf(false) }
    val animatedBorderColor by animateColorAsState(
        targetValue = if (isFocused) AccentGreen else BorderSubtle,
        animationSpec = tween(250),
        label = "authBorderColor"
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        shape = RoundedCornerShape(14.dp),
        singleLine = true,
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        label = { Text(label) },
        placeholder = {
            if (!placeholderText.isNullOrBlank()) {
                Text(text = placeholderText, color = TextSecondary.copy(alpha = 0.7f))
            }
        },
        visualTransformation = if (isPassword) PasswordVisualTransformation() else androidx.compose.ui.text.input.VisualTransformation.None,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = animatedBorderColor,
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
    isAnimating: Boolean,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val pressed by interactionSource.collectIsPressedAsState()
    val scale by animateFloatAsState(
        targetValue = if (pressed) 0.98f else 1f,
        animationSpec = tween(120),
        label = "authButtonScale"
    )

    val pigeonX = remember { Animatable(-40f) }

    LaunchedEffect(isAnimating) {
        if (isAnimating) {
            pigeonX.animateTo(
                targetValue = 350f,
                animationSpec = tween(1000, easing = LinearOutSlowInEasing)
            )
        } else {
            pigeonX.snapTo(-40f)
        }
    }

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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.graphicsLayer(alpha = if (isAnimating) 0f else 1f)
            )
            
            if (isAnimating) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = BackgroundDeep,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .size(24.dp)
                        .offset(x = pigeonX.value.dp)
                )
            }
        }
    }
}

private fun capitalizeFirstLetter(value: String): String {
    val trimmed = value.trimStart()
    if (trimmed.isBlank()) return ""
    return trimmed.replaceFirstChar { char ->
        if (char.isLowerCase()) char.titlecase() else char.toString()
    }
}

private fun isValidEmail(value: String): Boolean = Patterns.EMAIL_ADDRESS.matcher(value).matches()

private fun isValidRegisterPassword(value: String): Boolean {
    return value.length >= 8 && value.matches(Regex("^[A-Za-z0-9]+$"))
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
private fun LoginScreenPreview() {
    HabitFlowTheme {
        LoginScreen(viewModel = HabitViewModel(), onLogin = {})
    }
}
