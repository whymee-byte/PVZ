# Tests P2 — 18/11/2026

Se recompilaron los 45 archivos Java actuales en review/p2/classes. No se modificó producción. Ejecución reproducible desde la raíz: `rtk proxy python -X utf8 review/p2/run_p2.py`.

Los seis casos de PlantillaPR1CV/p2 terminan con exit 0 y stderr vacío. Ninguno coincide con el expected. Se comparan líneas normalizando CRLF/LF, sin ignorar espacios, contenido ni líneas vacías. stdout y stderr se fuerzan a UTF-8 para no atribuir al código diferencias de codificación del entorno.

## Causa transversal principal

Position.java:57 declara equals(GameObject), no equals(Position). Cuando isInPosition o Sun.catchObject pasan una Position, Java utiliza equals(Object) heredado: compara identidad, no coordenadas. El tablero se pinta vacío aun teniendo objetos y no funcionan las capturas ni los ataques que buscan una posición equivalente. También deja de detectarse correctamente la ocupación.

El equals(GameObject) nuevo compara hashes: eso tampoco es una igualdad correcta de coordenadas. Un hash igual no garantiza una posición igual. Position no necesita depender de GameObject. Para las llamadas actuales puede recuperarse una comparación explícita fila/columna entre Position sin instanceof.

Otras diferencias comunes:

- Faltan Generated suns y Caught suns en getInfo, desde el estado inicial de todos los casos.
- Los soles se representan como '*' y las referencias los muestran como '*[10]', '*[09]', etc.
- La generación está desplazada: en el caso sin girasoles 01 la referencia crea el primero en ciclo 6; SunManager actual lo crea en ciclo 5. En los casos con girasol, la referencia muestra su primer sol en ciclo 4 y el código lo crea en ciclo 3.
- CatchCommand ignora el resultado y repinta en vez de emitir No sun in (...) cuando falla.
- La ayuda coloca Catch al final en vez de tras CheatPlant. Hay diferencias de líneas vacías en ayuda/listados. ListZombies añade 'Available zombies:', que no figura en la referencia 02.

## 00-easy_25

Configuración ejecutada: EASY 25.

- Tras add sunflower 0 1, referencia muestra S[01] y vuestra salida no muestra la planta, aunque ambas descuentan a 30 monedas.
- c 6 1 en ciclo 4: referencia sube de 10 a 20 monedas, vuestra salida conserva 10. c 7 2 en ciclo 6: referencia llega a 30, vuestra salida sigue en 10. c 5 0 en ciclo 12: referencia llega a 40, vuestra salida sigue en 10.
- Capturas fallidas esperadas No sun in (5, 0), (7, 1), (3, 0), (2, 3) no aparecen: se repinta.
- a p 0 2: referencia emite Not enough suncoins, el programa emite Could not add plant.
- Las trazas divergen en generación y aparición de zombis; por ejemplo al ciclo 11 vuestra salida indica 0 pendientes y la referencia 1.
- Vuestra ejecución termina por Zombies win en ciclo 18.
- IMPORTANTE: el expected está defectuoso: acaba en ciclo 12 con una IllegalArgumentException de StringUtils.centre (13 caracteres para una casilla de 12). No es un final válido que debáis copiar.

## 01-easy_25

Configuración: EASY 25.

- Q y RST se rechazan correctamente. El reinicio R vuelve a ciclo 0 y e termina con Player leaves the game: correcto.
- Faltan los contadores de soles y todos los objetos del tablero quedan invisibles por la comparación de Position.
- La referencia mantiene 0 soles generados hasta ciclo 5 y muestra el primero en ciclo 6; el código genera en ciclo 5.
- En ciclo 10 la referencia dice Remaining zombies: 0, el programa dice 1. En ciclo 11 sigue diciendo 1. La secuencia de aleatoriedad/generación ya ha divergido.
- No es solo formato: aunque el reinicio y final coinciden, los estados intermedios no.

## 02-easy_25

Configuración: EASY 25.

- a y cp sin parámetros dan Missing parameters correctamente. Las compras y trampas avanzan el contador; los costes iniciales coinciden.
- Ayuda y listados difieren en orden, líneas vacías y cabecera extra Available zombies.
- Las plantas, zombis y soles no se dibujan por equals. No se pueden verificar visualmente las posiciones como en la referencia.
- c 6 1 en ciclo 6 debe dar 40 monedas según la referencia; conserva 30.
- c 3 1 debe dar No sun in (3, 1); repinta sin error.
- La cereza de cp c 0 3 figura como C[01] en ciclo 4 y ha desaparecido en ciclo 5 en la referencia. El código empieza cycle=-1, conserva health=2 y no explota hasta su tercera actualización: tampoco coincide con esa representación/cuenta.
- La referencia termina con Player wins en ciclo 11; el programa continúa hasta ciclo 12 y termina por e con Player leaves the game.
- Precaución con esa referencia: todavía dibuja zombis Sporty vivos cuando anuncia Player wins. Se debe confirmar la regla de victoria de zombis añadidos manualmente antes de convertir ese final en un requisito del código.

## 03-hard_17

- El nombre pide HARD 17, pero el expected comienza con Level: EASY y semilla 25. Arranca con 3 zombis pendientes, no 5.
- Ejecutado como HARD 17, vuestra partida pierde en ciclo 13 y el expected en ciclo 19. Esa comparación mezcla configuraciones y no prueba por sí sola un fallo del nivel HARD.
- También se ejecutó con EASY 25 según la cabecera del expected: vuestra partida pierde en ciclo 18, no 19. Persisten tablero vacío, contadores ausentes y generación/aleatoriedad distintas.
- Los a sin argumentos que llegan a ejecutarse dan Missing parameters correctamente. El número procesado cambia porque vuestra partida termina antes.

## 04-insane_360

- El nombre pide INSANE 360, pero el expected también comienza con EASY 25 y 3 pendientes en lugar de 10.
- Como INSANE 360 vuestra partida pierde en ciclo 11; la referencia termina en 19. No son configuraciones equivalentes.
- Repetido como EASY 25, pierde en ciclo 18 frente al 19 de la referencia. Se mantienen los defectos comunes de representación, contadores y secuencia de generación.
- Los errores Missing parameters de los comandos a procesados son correctos; se procesan menos órdenes en INSANE porque la partida termina antes.

## 05-easy_25

- Sus ficheros input y expected son idénticos byte por byte a los de 00. No es un escenario diferente.
- Reproduce exactamente las mismas diferencias de 00: captura sin monedas, tablero vacío, mensajes incorrectos, generación distinta y derrota en ciclo 18.
- También copia el mismo expected inválido con la excepción en ciclo 12.

## Archivos

Por cada ejecución: *-actual.txt, *-stderr.txt y *-diff.txt. Para 03/04 se guardan además las variantes *-expected-header-* con EASY 25. summary.json recoge configuraciones, errores y finales. run_p2.py recompila y ejecuta todo.

Prioridad: recuperar la comparación de coordenadas en Position, después repetir esta batería para aislar los errores secundarios de combate, ocupación, generación y representación. No se ha parcheado el código para fabricar coincidencias con expected defectuosos.
