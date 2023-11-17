package logic

interface IDataDifferences {
    fun findDifferences(
        oldValue: Any?,
        newValue: Any?,
        propertyName : String = ""
    ): List<Map<String, String>>
}