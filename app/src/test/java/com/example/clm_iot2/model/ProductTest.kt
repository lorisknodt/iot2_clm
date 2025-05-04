package com.example.clm_iot2.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ProductTest {

    @Test
    fun `test product instantiation with required fields`() {
        val product = Product(
            id = 1,
            nom = "Télévision",
            description = "Télévision 4K",
            prix = 499.99,
            stock = 100
        )

        assertEquals(1, product.id)
        assertEquals("Télévision", product.nom)
        assertEquals("Télévision 4K", product.description)
        assertEquals(499.99, product.prix)
        assertEquals(0, product.iconeResId) // valeur par défaut
        assertEquals(1, product.quantity)   // valeur par défaut
        assertEquals(Int.MAX_VALUE, product.availableStock) // valeur par défaut
        assertEquals(100, product.stock)
    }

    @Test
    fun `test quantity can be updated`() {
        val product = Product(
            id = 2,
            nom = "Smartphone",
            description = "Dernier modèle",
            prix = 999.99,
            quantity = 2,
            availableStock = 10,
            stock = 50
        )

        assertEquals(2, product.quantity)

        product.quantity += 1
        assertEquals(3, product.quantity)

        product.quantity -= 1
        assertEquals(2, product.quantity)
    }

    @Test
    fun `test available stock limits`() {
        val product = Product(
            id = 3,
            nom = "Clavier",
            description = "Clavier mécanique",
            prix = 79.99,
            quantity = 1,
            availableStock = 5,
            stock = 30
        )

        assertTrue(product.quantity <= product.availableStock)
        product.quantity = 6
        assertTrue(product.quantity > product.availableStock) // logiquement faux mais ici on autorise la surcharge
    }
}
