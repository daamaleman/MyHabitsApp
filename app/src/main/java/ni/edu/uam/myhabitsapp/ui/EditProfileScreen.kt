package ni.edu.uam.myhabitsapp.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ni.edu.uam.myhabitsapp.ui.theme.AccentGreen
import ni.edu.uam.myhabitsapp.ui.theme.BackgroundDeep
import ni.edu.uam.myhabitsapp.ui.theme.BorderSubtle
import ni.edu.uam.myhabitsapp.ui.theme.HabitFlowTheme
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceCard
import ni.edu.uam.myhabitsapp.ui.theme.SurfaceItem
import ni.edu.uam.myhabitsapp.ui.theme.TextPrimary

@Composable
fun EditProfileScreen(
    viewModel: HabitViewModel,
    onBack: () -> Unit
) {
    val profile by viewModel.userProfile.collectAsStateWithLifecycle()
    var name by remember { mutableStateOf(profile.name) }
    var email by remember { mutableStateOf(profile.email) }
    var password by remember { mutableStateOf(profile.password) }
    var passwordVisible by remember { mutableStateOf(false) }
    
    val layoutDirection = LocalLayoutDirection.current
    val contentMaxWidth = 520.dp
    val horizontalScreenPadding = 24.dp

    Scaffold(containerColor = BackgroundDeep) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundDeep),
            contentAlignment = Alignment.TopCenter
        ) {
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = SurfaceCard,
                        border = BorderStroke(1.dp, BorderSubtle)
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Volver",
                                tint = TextPrimary
                            )
                        }
                    }
                    Text(
                        text = "Editar Perfil",
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.padding(start = 10.dp)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nombre Completo") },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = SurfaceItem,
                        unfocusedContainerColor = SurfaceItem,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = AccentGreen
                    )
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Correo Electrónico") },
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = SurfaceItem,
                        unfocusedContainerColor = SurfaceItem,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = AccentGreen
                    )
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("Nueva Contraseña") },
                    shape = RoundedCornerShape(14.dp),
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = if (passwordVisible) "Ocultar contraseña" else "Ver contraseña",
                                tint = TextPrimary
                            )
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = AccentGreen,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = SurfaceItem,
                        unfocusedContainerColor = SurfaceItem,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary,
                        cursorColor = AccentGreen
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        if (name.isNotBlank() && email.isNotBlank() && password.isNotBlank()) {
                            viewModel.updateProfile(name.trim(), email.trim(), password)
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = AccentGreen,
                        contentColor = BackgroundDeep
                    )
                ) {
                    Text(text = "Guardar Cambios", fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun EditProfilePreview() {
    HabitFlowTheme {
        EditProfileScreen(
            viewModel = HabitViewModel(),
            onBack = {}
        )
    }
}
