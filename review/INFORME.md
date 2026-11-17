# Revisión del proyecto — 17/11/2026

Se revisaron los 45 archivos Java bajo PlantillaPR1CV/src. No se modificaron los archivos de producción. Las pruebas y sus salidas se guardan en review/.

## Alcance y fuentes

- practica1.pdf: comportamiento original, parámetros, comandos, aleatoriedad y entrega.
- practica2-1.pdf y su copia practica2-1 (1).pdf: Command, interfaces, herencia, contenedor, factorías y prohibición de identificar tipos directa o indirectamente.
- Explicación CatchCommand y SunManager.pdf: captura, generación, vida del sol, representación y callbacks.
- PR1 manual.pdf: separación MVC, una instancia de Game y normas originales de encapsulación/delegación. Sus detalles de listas separadas se sustituyen en PR2 por el contenedor único; no se exigen ambas arquitecturas a la vez.
- Plan de ataque PR1.pdf: guía inicial; no añade reglas para los objetos nuevos.
- No se localizó el enunciado de PR2 Parte II. No se certifican sus costes, vidas, frecuencias, probabilidades de tipos ni reglas exactas de explosión.

## Ejecución

Comando desde la raíz: `rtk proxy python -X utf8 review/run_review.py`.

Se usa C:/Program Files/Java/jdk-25.0.4/bin, se recompila el código fuente con UTF-8 y -Xlint:all en review/classes, y se ejecutan las pruebas sobre esa compilación, no sobre los .class de Eclipse.

- Compilación correcta, una advertencia this-escape: el constructor de Game llama a reset, que es sobrescribible. No es un error de compilación ni evidencia de fallo en la partida actual.
- 59 comprobaciones: 45 pasan y 14 no cumplen la expectativa del caso. Los casos fallidos incluyen defectos confirmados, mejoras del contrato de utilidades e hipótesis pendientes de Parte II; no son 14 errores independientes.
- Cinco trazas incluidas: todas terminan con código 0, ninguna coincide exactamente con su salida esperada antigua. La anchura de casilla ya difiere (12 frente a 8), y hay cambios funcionales de PR2; esta comparación no sirve por sí sola para declarar incorrecta PR2.
- Cinco ejecuciones de arranque: sin argumentos, nivel inválido, semilla inválida, argumentos sobrantes y partida válida seguida de exit; terminan sin excepciones.

## Defectos confirmados y prioritarios

### 1. CatchCommand no puede procesar una orden válida

Archivo: PlantillaPR1CV/src/pvz/control/CatchCommand.java, líneas 22–29.

Para `catch 2 1`, el array tiene índices 0, 1 y 2. Se lee argumentos[3], que provoca ArrayIndexOutOfBoundsException. El catch de NumberFormatException no la captura. Debe leerse columna de [1] y fila de [2]. También falla `catch x 1` antes de producir un error controlado.

execute ignora el booleano devuelto por el modelo y repinta siempre. Debe mostrar el tablero en éxito y comunicar un error sin repintar cuando la captura falla. No debe avanzar el ciclo.

### 2. Recoger un sol no lo mata y permite cobros repetidos

Archivo: PlantillaPR1CV/src/pvz/logic/gameobjects/Sun.java, líneas 16–27.

catchObject pone health=0, pero isAlive hereda la consulta de alive. En el siguiente update health pasa a -1 y la condición health==0 tampoco lo mata. El objeto queda visible y puede volver a dar monedas en ciclos posteriores. Además, catchObject no comprueba si el sol ya está muerto: incluso un sol caducado retenido en el contenedor acepta la captura.

Solución conceptual: centralizar la muerte, comprobar isAlive antes de capturar y hacer que vida <= 0 implique muerte. Evitar mantener dos estados contradictorios.

### 3. Los soles periódicos no se actualizan al generarse

Archivo: PlantillaPR1CV/src/pvz/logic/Game.java, líneas 75–79.

container.update ocurre antes de sunManager.update. El PDF justifica la vida inicial 11 porque debe perder un punto en su ciclo de creación. En la generación periódica actual sigue con health=11 al terminar ese ciclo. Los soles creados durante la actualización del girasol sí pueden actualizarse en ese mismo recorrido, porque el bucle del contenedor consulta el tamaño creciente de la lista.

### 4. Un método público anuncia una inserción que nunca realiza

Archivo: PlantillaPR1CV/src/pvz/logic/Game.java, último addGameObject(String, Position).

Devuelve true sin crear ni añadir nada. Además aparece en GameWorld, por lo que otros objetos tienen acceso a una operación engañosa. Implementarlo o eliminarlo de la interfaz y de Game si no se necesita.

### 5. Los objetos muertos siguen ocupando casillas hasta la limpieza

Archivo: PlantillaPR1CV/src/pvz/logic/GameObjectContainer.java, isEmpty y getGameObjectInPosition.

No se comprueba isAlive. Una planta que muere antes de que se actualice el zombi sigue bloqueándolo durante ese mismo ciclo. positionToString sí filtra muertos, de modo que una casilla visualmente vacía puede considerarse ocupada.

## Reglas de soles que necesitan confirmar Parte II

Las pruebas demuestran que un sol bloquea plantar y bloquea avanzar a un zombi, porque se utiliza la misma consulta isEmpty para cualquier objeto. Si Parte II establece que el sol no ocupa espacio, hay que sustituir esa decisión por un comportamiento polimórfico de ocupación/bloqueo; no por isSun, instanceof ni comparaciones de iconos para decidir movimiento.

Un sol, una planta y un zombi añadidos a la misma casilla producen 13 caracteres. StringUtils.centre exige como máximo 12 y lanza IllegalArgumentException. La API pública addGameObject(GameObject) permite reproducirlo. No se ha demostrado que esa combinación sea alcanzable con las órdenes actuales válidas; es un caso límite, no un fallo confirmado de una partida normal.

## Discrepancias literales con la hoja de CatchCommand/SunManager

- Sunflower reinicia cyclesSinceLastCoinGeneration a 0; la hoja pide 1 después de generar. No basta cambiar ese valor sin revisar la condición: con la condición actual, cambiar solo 0 por 1 alteraría la frecuencia a dos actualizaciones entre generaciones. Confirmar el conteo con las trazas de Parte II.
- generatedSuns ya existe, empieza en 0 y se incrementa al añadir soles, incluidos los pedidos por girasoles: corregido respecto a la primera revisión.
- Game ya llama a SunManager.update: corregido, aunque el orden no cumple la explicación del ciclo de creación.
- catchAlreadyExecuted ya se pone a true tras captura exitosa y a false en update: la prueba de una captura exitosa por ciclo pasa. La hoja no aclara completamente si un intento fallido consume la oportunidad.
- La hoja pide onEnter vacío en GameObject. Sigue siendo abstracto y se repite vacío en las clases concretas. No rompe el comportamiento, pero no sigue esa indicación.
- onExit notifica correctamente la muerte del zombi; el explosivo añade su acción. Los tres zombis no explosivos repiten la misma notificación: podría heredarse desde Zombie y el explosivo llamar a super.onExit().
- El contenedor ya concatena iconos con appendIconTo y Sun evita repetir el suyo: la prueba de planta y varios soles pasa.

## Auditoría de polimorfismo y responsabilidades

| Clases / grupo | Evaluación |
|---|---|
| PlantsVsZombies | Un main, una creación de Game y una de Controller. No se instancia PlantsVsZombies ni se llama main desde otras clases. Crear Game y Controller en main es lo exigido. |
| Command | Registro de prototipos y despacho por matchCommand/create/execute. No hay switch por comando en Controller. Correcto en lo esencial. |
| AddPlantCommand, AddPlantCheatCommand | Delegan a GameModel. Repetición de construcción/ejecución y mensajes literales fuera de Messages; mejora de mantenimiento, no fallo de polimorfismo. |
| AddZombieCommand | Crea parámetros y delega; admite nombres de varias palabras. Verificar con Parte II si debe consumir ciclo. |
| CatchCommand | Polimórfico estructuralmente; los fallos están en índices y tratamiento del resultado. |
| ResetCommand, UpdateCommand, ExitCommand | Reinicio, avance y salida probados. |
| HelpCommand, ListPlantsCommand, ListZombiesCommand | Consulta al registro/factorías y salida por GameView. No avanzan ciclo. |
| UnknownCommand, IllegalParametersCommand | Errores encapsulados como comandos. La hoja propone null para errores de create; vuestra variante funciona para errores normales, pero no sigue literalmente ese contrato. |
| Controller | Ejecuta Command sin conocer subclases. Guarda Game concreto y construye GamePrinter; menor desacoplamiento que inyectar las interfaces, pero no reidentifica tipos. |
| Level | Enum y búsqueda insensible a mayúsculas; parámetros originales correctos. |
| GameModel, GameStatus, GameWorld | Separación principal presente. GameStatus sirve a la vista y GameModel a comandos. GameWorld tiene métodos sobrantes y el método de inserción sin implementar. |
| Game | Coordina y delega. No guarda objetos concretos por subtipo; el local Plant que recibe de la factoría coincide con el ejemplo de PR2. Tiene bucles de gestión de acciones, a contrastar con la norma original de no poner bucles en Game. |
| GameObject | Base común con callbacks de interacción que por defecto no hacen nada; correcto. health y alive pueden contradecirse. setAlive público expone una mutación que conviene restringir si solo se utiliza internamente. |
| GameObjectContainer | Una lista privada de GameObject y despacho polimórfico. Sin ramas por clase. getGameObjectInPosition público expone más de lo necesario y no filtra muertos; preferible privado si solo se usa internamente. |
| GameItem | Contrato de ataques, implementado por GameObject y especializado por Plant/Zombie; correcto. |
| Plant, Peashooter | Formato/recepción de ataques comunes heredados. Peashooter repite isInPosition y redefine isAlive por salud mientras otros usan alive: unificar el criterio. Disparo al objetivo más cercano probado. |
| Sunflower | Genera mediante GameWorld. Primera generación al tercer update probada; revisar contador según hoja. GENERATED_SUN_COINS queda sin utilizar. |
| Wallnut | Hereda recibir ataques y permanece sin acción en update; estructura correcta. Valores pendientes de Parte II. |
| CherryBomb | Especializa icono y encola ExplosionAction; prueba de explosión sin ser devorada antes pasa. Retardo/iconos/daño exactos pendientes de Parte II. |
| Sun | Especializa captura y representación sin averiguar tipos ajenos. appendIconTo comprueba duplicación visual de su propio símbolo; no se usa para clasificar objetos ni para decidir combate. Acoplado a que ningún otro icono contenga '*'. |
| Zombie | Movimiento y ataque comunes en la base. receivePlantAttack responde polimórficamente. canMove usa ocupación genérica; revisar soles. Import de Game sin utilizar. |
| NormalZombie, BucketHeadZombie, SportyZombie | Construcción de datos y creación por prototipos; sin ramas por tipo. Velocidades actuales comprobadas contra sus constantes, no certificadas contra Parte II. onExit repetido. |
| ExplosiveZombie | Especialización por onExit y acción diferida; la prueba de daño tras morir pasa. |
| PlantFactory, ZombieFactory | Selección de prototipos por nombres de entrada y create virtual. Identificar el nombre pedido al crear es legítimo y distinto de averiguar el tipo de un objeto ya almacenado. No exponen sus listas. |
| SunManager | Genera y cuenta, ya no lleva saldo separado. Su contador no tiene acceso de lectura para mostrarlo. Depende de Game.NUM_ROWS/COLS por constantes. |
| ZombiesManager | Gestiona pendientes/vivos. Guarda Game concreto, podría limitarse por interfaz. Crea un tipo aleatorio antes de decidir si aparece y omite shouldAddZombie cuando ya no quedan pendientes: contrastar orden RNG con Parte II. No se debe afirmar que las trazas de PR1 sigan siendo válidas sin adaptar reglas. |
| GameAction, ExplosionAction | Acción por interfaz; el booleano elige la operación de ataque, no identifica la clase de un objeto. Casillas vecinas válidas probadas. Centro excluido por adjacentPositions y solo primer zombi alcanzado por casilla: confirmar reglas en Parte II. |
| GameView, GamePrinter | Vista por interfaces. Formato centralizado en Messages; getInfo no muestra generatedSuns ni caughtSuns aunque existen las cadenas. Confirmar exigencia de esos campos en Parte II. |
| Messages | Constantes de texto/iconos; no debe contener lógica de clasificación de objetos. Hay constantes antiguas sin uso y mensajes literales en comandos. |
| Position | equals(Position) sobrecarga, no sobrescribe equals(Object). Dos posiciones iguales comparadas como Object dan false. Falta hashCode coherente; toString devuelve vacío. No se ha detectado fallo actual por esto en el recorrido del juego, que usa referencias Position tipadas. |
| StringUtils | Centrado y repetición; comportamiento de exceso de anchura lanza excepción y debe tenerse en cuenta. |

No se encontraron instanceof, getClass, isInstance, reflexión ni métodos isZombie/isPlant/isSun para clasificar objetos en el código fuente actual. Tampoco casts a subtipos concretos de objetos de juego. Los métodos isAlive, isEmpty e isInPosition consultan estado/posición, no son identificadores de clase.

## Encapsulación y norma de Game sin bucles

PR1 manual prohíbe bucles en Game; actualmente executePendingActions y update tienen while/do-while. Son bucles de acciones y limpieza, no de clasificación, pero conviene mover la responsabilidad a un gestor/contenedor si se mantiene la norma en PR2. Esto es distinto de prohibir todos los bucles del programa.

Position es mutable internamente pero no tiene setters públicos; getPosition devuelve una referencia cuya API actual no permite cambiar fila/columna. No debe confundirse automáticamente con devolver una lista mutable. GameItem es la interfaz explícitamente requerida para interacciones, no una violación por devolver un objeto.

## Cobertura y límites

Se probaron saldo, compras, trampas, límites, ocupación, reinicio, cambio de nivel, repetibilidad, comandos, nombres y atajos, factorías, ataques, movimiento de todos los tipos, captura múltiple, límite por ciclo, duración y generación de soles, renderizado, eliminación, callbacks, explosiones y final de partida por victoria/derrota.

Las 14 expectativas fallidas se explican por varios casos del mismo defecto (especialmente captura). Position, compartir soles y anchura extrema no equivalen todos a incumplimientos confirmados del enunciado. Las pruebas de velocidades y explosiones comprueban comportamiento concreto del código, pero los números no se pueden certificar sin Parte II. Ninguna batería finita permite asegurar ausencia de todos los errores.

## Prioridad sugerida

1. Índices de CatchCommand y resultado/errores.
2. Muerte y no recaptura de Sun; coherencia health/alive.
3. Orden de actualización y política de ocupación de soles.
4. Eliminar/implementar el addGameObject vacío y filtrar muertos.
5. Contrastar Parte II: cadencias, datos, aleatoriedad, estadísticas y explosiones.
6. Limpieza de encapsulación, duplicación de callbacks, Position y mensajes.

Resultados completos: results.txt. Compilación: compile.txt. Integración y CLI: integration.json. Salidas y diferencias de las cinco trazas: *-actual.txt y *-diff.txt.
