package data

import data.Address

data class Person(
    val name: String,
    val addresses: List<Address>
)