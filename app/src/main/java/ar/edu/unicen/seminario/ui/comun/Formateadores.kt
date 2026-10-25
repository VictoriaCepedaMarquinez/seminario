package ar.edu.unicen.seminario.ui.comun

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

fun formatearFecha(fecha: String?): String? {
    if (fecha.isNullOrBlank()) return null
    return runCatching {
        LocalDate.parse(fecha).format(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG))
    }.getOrDefault(fecha)
}

fun obtenerAnio(fecha: String?): String? {
    if (fecha.isNullOrBlank()) return null
    return runCatching { LocalDate.parse(fecha).year.toString() }.getOrNull()
}
