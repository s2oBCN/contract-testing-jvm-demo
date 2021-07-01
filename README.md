# Ejemplo de proyecto ContractTesting en Java

Siguiendo las ramas de git del proyecto podemos ver como construir la solución

## Escenario

Hay dos componentes desarrollados por dos equipos:

1. Aplicación de catálogo de productos (consumidor). Proporciona una interfaz de consola para consultar el servicio del producto para obtener información sobre el producto.
1. Servicio de producto (proveedor). Proporciona información útil sobre los productos, como enumerar todos los productos y obtener los detalles de un producto individual.

## Step 1
Los equipos desarrollan en paralelo la solución

## Step 2
Añadimos PACT

## Step 3
Ampliamos el contrato verificando más "estados":
- No existe ningún producto
- Un producto específico no existe

## Step 4
Añadimos Docker -> openjdk:12-alpine

## Step 5
Añadimos Docker compose:

## Step 6
- Añadimos https://github.com/pact-foundation/pact-broker-docker
- Publicamos en Pact-broker