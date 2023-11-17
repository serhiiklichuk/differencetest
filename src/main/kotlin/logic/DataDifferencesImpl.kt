package logic

import kotlin.reflect.full.memberProperties

class DataDifferencesImpl : IDataDifferences {

    override fun findDifferences(
        oldValue: Any?,
        newValue: Any?,
        propertyName: String,
    ): List<Map<String, String>> {
        val differences = mutableListOf<Map<String, String>>()

        when {
            oldValue == null || newValue == null -> {
                // One is null, the other is not, consider as difference
                addDifference(
                    differences,
                    propertyName,
                    oldValue,
                    newValue
                )
            }
            oldValue.javaClass.kotlin.isData && newValue.javaClass.kotlin.isData -> {
                // Compare data class properties
                compareDataClasses(differences, oldValue, newValue)
            }
            oldValue is List<*> && newValue is List<*> -> {
                // Compare lists
                compareLists(differences, propertyName, oldValue, newValue)
            }
            oldValue::class == newValue::class -> {
                // Compare primitive types or other objects
                if (oldValue != newValue) {
                    addDifference(
                        differences,
                        propertyName,
                        oldValue,
                        newValue
                    )
                }
            }
        }

        return differences
    }

    private fun compareLists(
        map: MutableList<Map<String, String>>,
        propName: String = "",
        oldValue: List<*>,
        newValue: List<*>
    ) {
        val maxIndex = maxOf(oldValue.size, newValue.size)

        for (i in 0 until maxIndex) {
            val oldElement = oldValue.getOrNull(i)
            val newElement = newValue.getOrNull(i)

            if (oldElement != null || newElement != null) {
                if (i < oldValue.size && i < newValue.size) {
                    // Both lists have this element, compare them
                    map.addAll(findDifferences(oldElement, newElement, propName))
                } else if (i >= oldValue.size) {
                    // New element added in newValue
                    addDifference(map, "$propName[$i]", null, newElement)
                } else {
                    // Old element removed from oldValue
                    addDifference(map, "$propName[$i]", oldElement, null)
                }
            }
        }
    }

    private fun compareDataClasses(
        map: MutableList<Map<String, String>>,
        oldValue: Any,
        newValue: Any
    ) {
        oldValue::class.memberProperties.forEach { property ->
            val propName  =  property.name
            val oldPropValue = property.getter.call(oldValue)
            val newPropValue = property.getter.call(newValue)
            map.addAll(findDifferences(oldPropValue, newPropValue, propName))
        }
    }

    private fun addDifference(
        map: MutableList<Map<String, String>>,
        propName: String = "",
        oldValue: Any? = null,
        newValue: Any? = null
    ) {
        map.add(
            mapOf(
                "field" to propName,
                "prev" to (oldValue ?: "null").toString(),
                "curr" to (newValue ?: "null").toString()
            )
        )
    }

}