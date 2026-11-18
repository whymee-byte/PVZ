# Revisión con los expected nuevos

Producción compilada sin modificaciones. Ejecutados los seis pares actuales con nivel y semilla del nombre; ahora las cabeceras expected coinciden. Se ignoraron espacios dentro de casillas para localizar las diferencias de estado.

## Primera diferencia funcional por caso

Las coordenadas indicadas son (columna, fila).

| Caso | Comando/ciclo | Expected | Actual | Localización |
|---|---|---|---|---|
| 00-easy_25 | Primer comando vacío tras add sunflower 0 1; ciclo 2 | 1 pendiente; ningún zombi visible | 2 pendientes; Ez[05] en (7,2) | Zombie.java:12,52 y ZombiesManager.java:47 |
| 01-easy_25 | Segundo comando vacío; ciclo 2 | 1 pendiente; ningún zombi visible | 2 pendientes; Ez[05] en (7,2) | Mismas causas que 00 |
| 02-easy_25 | cp p 0 1; ciclo 2 | 1 pendiente; ningún zombi visible | 2 pendientes; Ez[05] en (7,2) | Mismas causas que 00 |
| 03-hard_17 | Comando vacío que lleva a ciclo 5 | 4 pendientes; ningún zombi visible | 4 pendientes; Sz[02] en (7,3) | Movimiento inicial en Zombie.java:12,52 |
| 04-insane_360 | Comando vacío que lleva a ciclo 3 | 9 pendientes; ningún zombi visible | 9 pendientes; Sz[02] en (7,1) | Movimiento inicial en Zombie.java:12,52 |
| 05-easy_25 | Primer comando vacío tras add sunflower 0 1; ciclo 2 | 1 pendiente; ningún zombi visible | 2 pendientes; Ez[05] en (7,2) | Mismas causas que 00 |

## Causas aisladas en copia de diagnóstico

Se copió el fuente a review/p2/diagnostic/src. Los cambios de este directorio NO se aplicaron a PlantillaPR1CV/src.

1. **Movimiento adelantado de zombis aleatorios.** cyclesSinceLastMovement empieza implícitamente en 0 y se incrementa antes de comprobar la frecuencia. La referencia deja pasar un update inicial. Los zombis manuales sí usan startMovementNextCycle(), pero los aleatorios no. Una variante con contador inicial -1 elimina el adelanto inicial de todos los casos.
2. **Zombi rechazado al aparecer en casilla ocupada.** ZombiesManager.addZombie(int), línea 47, exige gw.isEmpty(zombiePosition). En EASY/25 la referencia añade un segundo zombi a la posición de entrada aunque el primero todavía está allí. La variante que, además del retraso, elimina esa condición en la generación automática hace coincidir todos los estados numéricos y de casillas de 00 y 01. No se ha eliminado la validación de posición del comando manual.
3. **Soles que bloquean movimiento.** Zombie.canMove usa isEmpty, que considera los soles vivos como ocupación. Con las dos causas anteriores aisladas, 03 diverge en ciclo 8: la referencia mueve Sz a (5,3) junto al sol, pero el código lo mantiene en (6,3). En 04, ciclo 11, Ez debe entrar en (6,2) junto al sol y permanece en (7,2). Una variante adicional con comportamiento polimórfico de bloqueo permite terminar 03 en ciclo 14 y 04 en 12, y hace coincidir todos sus estados numéricos y de casillas con las referencias. No se usó instanceof ni identificación de clases.

No se afirma coincidencia exacta de todos los mensajes de estas variantes: se compararon comandos, cinco contadores y contenido de casillas sin espacios.

## Inconsistencias entre input y expected todavía presentes

### 02

- Input línea 15: c 3 1. El expected ejecuta c 7 2.
- El input contiene dos avances vacíos después de cp p 3 1; el expected pasa directamente a e.
- Por ello la referencia captura otro sol, tiene 50 monedas y 2 capturas, y termina en ciclo 10. El input real deja el sol sin recoger y contiene órdenes para avanzar hasta 12.
- Incluso tras aislar movimiento/aparición/bloqueo en la copia, persiste una diferencia: en ciclo 10 la referencia muestra P[02] en (3,1) y la copia P[03]. Esto necesita una comprobación específica de orden de ataques/explosión; no se atribuye automáticamente a la cereza ni se modifica producción para forzarla.

### 05

- Input línea 31: c 3 0. El expected ejecuta c 3 2.
- El input tiene órdenes posteriores que no aparecen en la referencia porque esta ya alcanza su final de partida. Eso por sí solo puede ser legítimo; la captura distinta en línea 31 sí es una incompatibilidad del par.
- La referencia además permite a p 0 0 sobre una casilla que contiene un sol. Game.addGameObject comprueba isEmpty y actualmente lo rechaza. Se requiere una política polimórfica de ocupación para plantas, además del bloqueo del movimiento del zombi.

## Finales de las ejecuciones actuales, sin cambios de diagnóstico

| Caso | Esperado | Actual |
|---|---|---|
| 00 | Victoria, ciclo 18 | Continúa hasta 22 y agota entrada (NoSuchElementException) |
| 01 | Reset y salida | Reset y salida, pero estados intermedios distintos |
| 02 | Salida en ciclo 10 | Salida en ciclo 12 |
| 03 | Derrota, ciclo 14 | Derrota, ciclo 23 |
| 04 | Derrota, ciclo 12 | Derrota, ciclo 11 |
| 05 | Derrota, ciclo 19 | Continúa hasta 21 y agota entrada (NoSuchElementException) |

## Otros errores

- CatchCommand muestra Could not catch sun en lugar de No sun in (col, row).
- AddPlantCommand agrupa todos los fallos como Could not add plant, sin distinguir dinero insuficiente.
- NoSuchElementException en 00/05 ocurre al intentar leer después del último comando, en GamePrinter.getPrompt. Es la consecuencia visible de que la partida no finalizó con la secuencia suministrada; no implica un fallo de representación como en ejecuciones antiguas.

Archivos actualizados: summary.json, *-actual.txt, *-stderr.txt, *-diff.txt. Las variantes de diagnóstico están separadas en diagnostic/. No se modificó el código de producción.
