package com.example.unifood

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Receipt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Colores ----------
private val FondoHeader = Color(0xFFD9ECF4)
private val FondoPantalla = Color(0xFFFAFAFA)
private val TealMarca = Color(0xFF1F6F7A)
private val DoradoMarca = Color(0xFFC8963E)
private val VerdeAbierta = Color(0xFF1B7F3B)
private val RojoCerrada = Color(0xFFC62828)
private val ChipTeal = Color(0xFF6BB3BA)
private val ChipTealClaro = Color(0xFFA8D8D4)
private val ChipNaranja = Color(0xFFFFB07C)
private val ChipAmarillo = Color(0xFFFFC77D)
private val EstrellaColor = Color(0xFFF5B301)
private val BotonMenu = Color(0xFFCDE7F0)

// ---------- Modelos y datos de ejemplo ----------
data class Cafeteria(
    val nombre: String,
    val calificacion: Double,
    val resenas: Int,
    val direccion: String,
    val abierta: Boolean,
    val color: Color
)

data class Producto(
    val nombre: String,
    val calificacion: Double,
    val etiqueta: String,
    val color: Color
)

private val cafeterias = listOf(
    Cafeteria("Cafetería de Ingeniería", 4.5, 18, "Edificio de Ingeniería, Campus", true, Color(0xFF8D6E63)),
    Cafeteria("Cafetería de Ciencias", 4.5, 18, "Edificio de Ciencias, Campus", true, Color(0xFF6D8B74)),
    Cafeteria("Cafetería Central", 4.0, 31, "Plaza central, Campus", false, Color(0xFF7986CB))
)

private val productos = listOf(
    Producto("Torta Cubana", 4.0, "Más pedido en Ingeniería", Color(0xFFE57373)),
    Producto("Ensalada César", 4.0, "Por precio", Color(0xFF81C784)),
    Producto("Café Americano", 4.5, "Más pedido en Ciencias", Color(0xFFA1887F)),
    Producto("Burrito de Machaca", 5.0, "Favorito de la semana", Color(0xFFFFB74D))
)

// ---------- Pantalla principal ----------
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    var busqueda by remember { mutableStateOf("") }
    var seleccionados by remember {
        mutableStateOf(setOf("Abierta", "Menos de \$100", "Desayuno"))
    }
    var tabActual by remember { mutableIntStateOf(0) }

    val filtradas = cafeterias.filter { it.nombre.contains(busqueda, ignoreCase = true) }

    Scaffold(
        containerColor = FondoPantalla,
        contentWindowInsets = WindowInsets(0.dp),
        bottomBar = { BarraInferior(tabActual) { tabActual = it } }
    ) { innerPadding ->
        Column(modifier = Modifier.fillMaxSize().padding(innerPadding)) {

            Encabezado(
                busqueda = busqueda,
                onBusqueda = { busqueda = it },
                seleccionados = seleccionados,
                onChip = { chip ->
                    seleccionados =
                        if (chip in seleccionados) seleccionados - chip else seleccionados + chip
                }
            )

            if (tabActual != 0) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Próximamente")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(filtradas, key = { it.nombre }) { cafeteria ->
                        CafeteriaCard(cafeteria) {
                            Toast.makeText(context, "Menú de ${cafeteria.nombre}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            "Recomendaciones Inteligentes",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Medium
                        )
                        Text("Productos más pedidos", style = MaterialTheme.typography.bodyMedium)
                    }

                    item {
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            items(productos) { producto -> ProductoCard(producto) }
                        }
                    }
                }
            }
        }
    }
}

// ---------- Encabezado: logo, buscador y chips ----------
@Composable
private fun Encabezado(
    busqueda: String,
    onBusqueda: (String) -> Unit,
    seleccionados: Set<String>,
    onChip: (String) -> Unit
) {
    val fila1 = listOf("Abierta" to ChipTeal, "Menos de \$100" to ChipTealClaro, "Desayuno" to ChipAmarillo)
    val fila2 = listOf("Comida Mexicana (2)" to ChipNaranja, "Entrega Rápida (4)" to ChipTealClaro)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(FondoHeader)
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Logo (reemplaza el ícono por tu logo en res/drawable)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier.size(40.dp).clip(CircleShape).background(Color.White),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Filled.Restaurant, contentDescription = null, tint = TealMarca)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = buildAnnotatedString {
                    withStyle(SpanStyle(color = TealMarca, fontWeight = FontWeight.Bold)) { append("Uni") }
                    withStyle(SpanStyle(color = DoradoMarca, fontWeight = FontWeight.Bold)) { append("Food") }
                },
                fontSize = 28.sp
            )
        }

        // Buscador
        OutlinedTextField(
            value = busqueda,
            onValueChange = onBusqueda,
            placeholder = { Text("Buscar cafetería por nombre...") },
            trailingIcon = { Icon(Icons.Filled.Search, contentDescription = "Buscar") },
            singleLine = true,
            shape = RoundedCornerShape(28.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent
            ),
            modifier = Modifier.fillMaxWidth()
        )

        // Dos filas de chips con scroll horizontal
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(fila1) { (texto, color) ->
                ChipFiltro(texto, color, texto in seleccionados) { onChip(texto) }
            }
        }
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(fila2) { (texto, color) ->
                ChipFiltro(texto, color, texto in seleccionados) { onChip(texto) }
            }
        }
    }
}

@Composable
private fun ChipFiltro(texto: String, color: Color, seleccionado: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (seleccionado) color else color.copy(alpha = 0.4f)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (seleccionado) {
                Icon(Icons.Filled.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(texto, style = MaterialTheme.typography.labelLarge)
        }
    }
}

// ---------- Tarjeta de cafetería ----------
@Composable
private fun CafeteriaCard(cafeteria: Cafeteria, onVerMenu: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(modifier = Modifier.padding(8.dp)) {
            // Reemplaza por la foto real (ver instrucciones abajo)
            FotoPlaceholder(
                color = cafeteria.color,
                icono = Icons.Filled.Restaurant,
                modifier = Modifier.size(width = 96.dp, height = 108.dp).clip(RoundedCornerShape(12.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    cafeteria.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Estrellas(cafeteria.calificacion)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        "${cafeteria.calificacion.toString().replace('.', ',')} (${cafeteria.resenas})",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Filled.Place, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(cafeteria.direccion, style = MaterialTheme.typography.bodySmall, maxLines = 2)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        if (cafeteria.abierta) "Abierta" else "Cerrada",
                        color = if (cafeteria.abierta) VerdeAbierta else RojoCerrada,
                        fontWeight = FontWeight.Medium
                    )
                    Button(
                        onClick = onVerMenu,
                        colors = ButtonDefaults.buttonColors(containerColor = BotonMenu, contentColor = Color.Black),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text("Ver Menú")
                    }
                }
            }
        }
    }
}

// ---------- Tarjeta de producto ----------
@Composable
private fun ProductoCard(producto: Producto) {
    Card(
        modifier = Modifier.width(150.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column {
            FotoPlaceholder(
                color = producto.color,
                icono = Icons.Filled.Fastfood,
                modifier = Modifier.fillMaxWidth().height(90.dp)
            )
            Column(modifier = Modifier.padding(8.dp)) {
                Text(
                    producto.nombre,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1
                )
                Estrellas(producto.calificacion, tamano = 14.dp)
                Text(producto.etiqueta, style = MaterialTheme.typography.labelSmall, maxLines = 1)
            }
        }
    }
}

// ---------- Componentes reutilizables ----------
@Composable
private fun Estrellas(calificacion: Double, tamano: Dp = 16.dp) {
    Row {
        for (i in 1..5) {
            val icono = when {
                calificacion >= i -> Icons.Filled.Star
                calificacion >= i - 0.5 -> Icons.Filled.StarHalf
                else -> Icons.Filled.StarBorder
            }
            Icon(icono, contentDescription = null, tint = EstrellaColor, modifier = Modifier.size(tamano))
        }
    }
}

@Composable
private fun FotoPlaceholder(color: Color, icono: ImageVector, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(Brush.linearGradient(listOf(color, color.copy(alpha = 0.6f)))),
        contentAlignment = Alignment.Center
    ) {
        Icon(icono, contentDescription = null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(32.dp))
    }
}

@Composable
private fun BarraInferior(tabActual: Int, onTab: (Int) -> Unit) {
    val items = listOf(
        "Inicio" to Icons.Filled.Home,
        "Pedidos" to Icons.Outlined.Receipt,
        "Favoritos" to Icons.Outlined.FavoriteBorder,
        "Perfil" to Icons.Outlined.Person
    )
    NavigationBar(containerColor = Color.White) {
        items.forEachIndexed { index, (etiqueta, icono) ->
            NavigationBarItem(
                selected = tabActual == index,
                onClick = { onTab(index) },
                icon = { Icon(icono, contentDescription = etiqueta) },
                label = { Text(etiqueta) },
                colors = NavigationBarItemDefaults.colors(indicatorColor = FondoHeader)
            )
        }
    }
}