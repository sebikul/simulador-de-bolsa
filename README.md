Simulador de Bolsa
==================

![alt tag](https://raw.github.com/sebikul/simulador-de-bolsa/master/Informe_TPE_POO.png)

[javadoc](http://rawgit.com/sebikul/simulador-de-bolsa/master/help/index.html)

Simulador
---------
Representa una simulación. Almacena los agentes de bolsa que operaran
y el mercado sobre el cual se realizarán las operaciones.
Se encarga de entregar las notificaciones de los diferentes estados de la simulación.

Mercado
-------
Almacena los títulos sobre los cuales se puede operar.

Titulo
------
Representa un elemento que se puede comprar o vender dentro de la simulación.

Agente de Bolsa
---------------
Representa un agente de bolsa que se encargara de administrar la cartera
de un subconjunto de inversores. Es el encargado de administrar su capital, realizar las operaciones y consultar a los inversores qué transacciones desean realizar con su cartera.

Inversor
--------
Representa a un inversor dentro de la simulación.
Mantiene una referencia con su agente de bolsa y almacena los datos de su cartera.


Resultados
----------
Fueron agregados en una clase aparte para que al
serializarlo no se incluyan todas las instancias innecesarias
para analizar los datos históricos y para abstraer un objeto
activo (como es el simulador) de los resultados que arroja.

Datos Historicos
----------------
Almacena una lista con valores variables en cada ciclo.
