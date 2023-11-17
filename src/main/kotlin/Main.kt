import data.Address
import data.Person
import logic.DataDifferencesImpl
import logic.IDataDifferences

fun main(args: Array<String>) {

//    {
//        "name": "wenwei",
//        "addresses": [
//        {
//            "street": "7400 gateway blvd",
//            "city": "newark"
//        },
//        {
//            "street": "3500 Rutledge",
//            "city": "fremont"
//        }
//        ]
//    }

    val oldRecord = Person(
        "wenwei",
        listOf(
            Address("7400 gateway blvd", "newark"),
            Address("3500 Rutledge", "fremont")
        )
    )

//    {
//        "name": "wenwei",
//        "addresses": [
//        {
//            "street": "7400 gateway blvd",
//            "city": "newark"
//        },
//        {
//            "street": "100 main",
//            "city": "milpitas"
//        }
//        ]
//    }

    val newRecord = Person(
        "wenwei",
        listOf(
            Address("7400 gateway blvd", "newark"),
            Address("100 main", "milpitas")
        )
    )

    val diffExecutor: IDataDifferences = DataDifferencesImpl()
    val differences = diffExecutor.findDifferences(oldRecord, newRecord)
    println(differences)
//    [
//        {
//            "field": "street",
//            "prev": "3500 Rutledge",
//            "curr": "100 main"
//        },
//        {
//            "field": "city",
//            "prev": "fremont",
//            "curr": "milpitas"
//        }
//    ]

//    Result
//    [{field=city, prev=fremont, curr=milpitas}, {field=street, prev=3500 Rutledge, curr=100 main}]

}