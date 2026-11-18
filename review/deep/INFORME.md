# Pruebas ampliadas antes de entrega

Se probaron los 45 archivos Java guardados. No se modificó código de producción ni expected. Ejecución: `rtk proxy python -X utf8 review/run_deep.py` desde C:/Users/ouyan/PVZ. Java utilizado: JDK 25.0.4. El script recompila en review/deep/classes; no utiliza los .class de Eclipse.

## Resultado

- 850 casos del modelo/contratos: 846 pasan, 4 detectan la misma discrepancia de regla.
- 120 comprobaciones de consola: todas pasan. Incluyen 36 escenarios aleatorios de 180 órdenes, repetidos dos veces (72 ejecuciones), sus 36 comparaciones de determinismo y 12 escenarios de arranque/lectura/errores.
- Total propio: 970 comprobaciones, 966 correctas y 4 asociadas a una sola regla pendiente de confirmar.
- Se volvieron a ejecutar los seis pares oficiales de p2: todos terminan sin excepciones y con el mismo ciclo/final de sus referencias. Persisten diferencias de texto de errores, cabeceras, líneas vacías y un comando del input05 que difiere de su expected.
- La compilación tiene una advertencia this-escape por el constructor de Game llamando a reset sobrescribible; no hubo errores de compilación.
- Búsqueda estática en producción: sin instanceof, getClass(), isInstance() ni métodos isZombie/isPlant/isSun/isPeashooter. Esto complementa, no sustituye, la revisión de diseño previa.

## Cobertura

| Área | Casos y alcance |
|---|---|
| Coordenadas | 32 casillas, igualdad/diferencia/null, desplazamiento y ocho vecinos únicos |
| Plantas | Cuatro tipos en todas las casillas, coste, compra, solapamiento, visibilidad |
| Coexistencia con soles | Todos los tipos de planta sobre un sol en las 32 casillas; recogerlo sin desbloquear una planta viva |
| Zombis | Cuatro tipos en las 32 casillas, creación manual, cuota, rechazo de solapamiento, daño letal |
| Disparo | Todas las parejas ordenadas de columnas de cada fila, blanco más cercano, otras filas y objetivos a la izquierda |
| Movimiento | Frecuencias actuales, bloqueo por planta y zombi, paso sobre soles |
| Interacciones | Combinaciones de tipos, inmunidad a ataques del mismo bando y daño letal |
| Explosiones | Origen en las 32 casillas, daño a todos los vecinos válidos para ambos bandos, bordes y esquinas, cereza y muerte de explosivo |
| Acciones | Acción que añade otra acción, limpieza en el mismo ciclo y descarte de acciones al reiniciar |
| Soles | Duración, captura múltiple, no recaptura, una captura exitosa por ciclo, primer icono por antigüedad, generación hasta 30 actualizaciones |
| Comandos | Consultas sin avance, compra/trampas, captura, avance vacío, mayúsculas, espacios y tabuladores, salida sin salto final, parámetros inválidos y desbordamiento numérico |
| Reinicio | Contadores, saldo, bandera de captura y salida, nivel, misma semilla y repetición del estado |
| Partidas generadas | 60 sesiones del modelo, tres niveles y 20 semillas; hasta 250 órdenes por sesión, detenidas al finalizar. Cada paso verifica saldo/cuota no negativos, iconos vivos y anchura de casillas |
| Consola completa | 36 escenarios adicionales, 12 semillas por nivel y 180 órdenes por escenario; cada uno se ejecutó dos veces. Invariantes de tableros y finales; las órdenes tras una finalización natural no se procesan |
| Fin de partida | Llegada a casa por cada fila y victoria tras agotar y eliminar zombis |

Los casos de velocidad y la primera explosión de cereza verifican los valores y cadencias actuales compatibles con los expected disponibles; no certifican por sí solos todos los requisitos de PR2 Parte II, cuyo enunciado completo no se ha localizado.

## Hallazgo importante: movimiento y ataque en una actualización

Zombie.java:46–54 llama a attack() fuera del if/else de movimiento. Por ello, cuando un zombi avanza hasta quedar junto a una planta, también la daña en esa misma actualización.

Reproducción aislada, sin azar: Wallnut en (columna 0, fila 0); zombi en (2,0). Actualizar ese zombi hasta su primer movimiento. Termina en (1,0) y el Wallnut pasa de 10 a 9 en esa misma actualización. Se reproduce con los cuatro tipos.

La regla de practica1.pdf, sección 2.4, dice que en un ciclo el zombi avanza o ataca, nunca ambas acciones. Sin embargo, el expected actual del test02 requiere P[02] al quedar un explosivo adyacente tras moverse; antes del cambio a ataque incondicional el código mostraba P[03]. Existe una contradicción entre la regla original y el comportamiento de esa referencia. No se recomienda quitar o añadir daño para forzar una referencia sin confirmar qué regla rige en Parte II.

Estas cuatro pruebas se clasifican como auditoría de regla: no como cuatro defectos independientes ni como errores confirmados contra un enunciado de Parte II que no se posee.

## Diferencias de salida que afectan una comparación automática

- CatchCommand utiliza ahora un mensaje largo genérico de captura fallida en lugar de No sun in (col, row).
- AddPlantCommand da Could not add plant cuando la referencia exige Not enough suncoins.
- ListZombies añade Available zombies: y persisten diferencias de líneas vacías.
- En test05, el input aún ejecuta c 3 0 donde el expected muestra c 3 2. No se deben modificar datos de prueba para encubrir fallos; solicitar el par consistente o acordar qué entrada se evalúa.

## Límites de la conclusión

No se encontraron cierres, anchuras inválidas, monedas negativas, duplicación de iconos de sol ni pérdida de determinismo en los casos probados. Eso no demuestra ausencia absoluta de errores. La batería no es una certificación del corrector oculto y no reemplaza revisar el enunciado completo y el formato de entrega. No se han probado entradas API imposibles desde los comandos, como referencias null para el mundo o posiciones intencionalmente absurdas añadidas saltándose sus validaciones.

Antes de entregar: resolver la regla de movimiento/ataque y los mensajes que compare automáticamente la asignatura. La funcionalidad recorrida por los seis casos oficiales y las sesiones generadas se mantiene estable.

Artefactos: results.txt (modelo), cli-summary.json (120 resultados y hashes de los fuentes probados), compile.txt (compilación). Código de pruebas: ../DeepChecks.java y ../run_deep.py. Las pruebas viven fuera de src de producción.
