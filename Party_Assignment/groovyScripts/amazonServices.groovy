import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.util.EntityQuery


// to update the basic details of Person
String updateBasicDetails() {
    String partyId = context['partyId']
    String firstName = context['firstName']
    String middleName = context['middleName']
    String lastName = context['lastName']
    String partyType = context['partyType']
    println(partyId)
    boolean success = true
    try {
        // Update Person table
        GenericValue personEntity = EntityQuery.use(delegator).from("Person").where("partyId",partyId).queryOne()
        if (personEntity) {
            personEntity.firstName = firstName
            personEntity.middleName = middleName
            personEntity.lastName = lastName
            success = success && delegator.store(personEntity)
        } else {
            success = false
        }

        // Update Party table
        GenericValue partyEntity = EntityQuery.use(delegator).from("Party").where("partyId",partyId).queryOne()
        if (partyEntity) {
            partyEntity.partyTypeId = partyType ?: partyEntity.partyTypeId
            success = success && delegator.store(partyEntity)
        } else {
            success = false
        }

        if (success) {
            return "Records updated successfully in Person, PartyRole, and Party tables."
        } else {
            return "Failed to update records in Person, PartyRole, or Party tables."
        }
    } catch (Exception e) {
        // Handle any exceptions that might occur during the database operations
        e.printStackTrace()
        return "An error occurred while updating records."
    }
}


// update the postal address of person
String updatePostalAddress() {
    String toName = context['toName']
    String address1 = context['address1']
    Long houseNumber = context['houseNumber'] as Long
    String city = context['city']
    String contactMechId = context['contactMechId']
    String postalCode = context['postalCode']

    boolean success = true

    try {
        // Find the PostalAddress entity by contactMechId
        GenericValue postalAddressEntity =  EntityQuery.use(delegator).from("PostalAddress").where("contactMechId",contactMechId).queryOne()

        if (postalAddressEntity) {
            // Update postal address properties if corresponding values are present in the context
            postalAddressEntity.toName = toName ?: postalAddressEntity.toName
            postalAddressEntity.address1 = address1 ?: postalAddressEntity.address1
            postalAddressEntity.houseNumber = houseNumber ?: postalAddressEntity.houseNumber
            postalAddressEntity.city = city ?: postalAddressEntity.city
            postalAddressEntity.postalCode = postalCode ?: postalAddressEntity.postalCode

            // Store the updated postal address entity in the database
            success = success && delegator.store(postalAddressEntity)
        } else {
            // If the postal address entity is not found, set success to false
            success = false
        }
    } catch (Exception e) {
        // Catch any exceptions that occur during the database operations
        success = false
        println("Exception occurred while updating postal address: ${e.message}")
    }

    // Provide a success or failure message
    if (success) {
        return "Records updated successfully in PostalAddress entity."
    } else {
        return "Failed to update records in PostalAddress entity."
    }
}




// update the telePhone number of person
String updateTelePhone()
{
    String countryCode = context['countryCode']
    String contactNumber = context['contactNumber']
    String contactMechId = context['contactMechId']
    println(countryCode + " " + contactNumber + " " + contactMechId)

    boolean success = true

    try {
        // Find the TelecomNumber entity by contactMechId
        GenericValue telePhoneEntity = EntityQuery.use(delegator).from("TelecomNumber").where("contactMechId",contactMechId).queryOne()

        if (telePhoneEntity) {
            // Update telecom number properties if corresponding values are present in the context
            telePhoneEntity.countryCode = countryCode ?: telePhoneEntity.countryCode
            telePhoneEntity.contactNumber = contactNumber ?: telePhoneEntity.contactNumber

            // Store the updated telecom number entity in the database
            success = success && delegator.store(telePhoneEntity)
        } else {
            // If the telecom number entity is not found, set success to false
            success = false
        }
    } catch (Exception e) {
        // Catch any exceptions that occur during the database operations
        success = false
        println("Exception occurred while updating telecom number: ${e.message}")
    }

    // Provide a success or failure message
    if (success) {
        println()
        return "Records updated successfully in TelecomNumber entity."
    } else {
        return "Failed to update records in TelecomNumber entity."
    }
}


// delete the contactMech of that person
Boolean deleteContactMech()
{
    String contactMechId = context['contactMechId']

    println(contactMechId)

    try {
        // Find the ContactMech entity by contactMechId
        GenericValue mydelegator = EntityQuery.use(delegator).from("ContactMech").where("contactMechId",contactMechId).queryOne()
        println(mydelegator)
        if (mydelegator) {
            // If the ContactMech entity exists, remove it from the database
            delegator.removeByAnd("ContactMech", [contactMechId: contactMechId])
            println("Success: ContactMech entity deleted.")
            return true
        } else {
            println("Error: ContactMech entity not found for the given contactMechId.")
            return  false
        }
    } catch (Exception e) {
        // Catch any exceptions that occur during the database operations
        println("Exception occurred while deleting ContactMech: ${e.message}")
        return false
    }
}



// delete the person entity and party entity
Boolean deletePerson()
{
    String partyId = context['partyId']
    println(partyId)

    String statusId = context['statusId']
    String statusDateStr = context["statusDate"]
    java.sql.Timestamp statusDate = java.sql.Timestamp.valueOf(statusDateStr)

    try {
        // Find the PartyStatus entity by partyId, statusId, and statusDate
        GenericValue partyStatus =  EntityQuery.use(delegator).from("PartyStatus").where(partyId: partyId, statusId: statusId, statusDate: statusDate).queryOne()

        if (partyStatus) {
            // If the PartyStatus entity exists, remove it from the database
            delegator.removeByAnd("PartyStatus", [partyId: partyId, statusId: statusId, statusDate: statusDate])
            println("Status deleted")

            // Find the Person entity by partyId
            GenericValue personEntity = delegator.findOne("Person", [partyId: partyId], false)
            if (personEntity) {
                // If the Person entity exists, remove it from the database
                delegator.removeByAnd("Person", [partyId: partyId])
                println("Person entity deleted")

                // Find the Party entity by partyId
                GenericValue partyEntity = delegator.findOne("Party", [partyId: partyId], false)
                println(partyEntity)
                if (partyEntity) {
                    // If the Party entity exists, remove it from the database
                    delegator.removeByAnd("Party", [partyId: partyId])
                    println("Party deleted")
                    return  true
                }
            }
        } else {
            println("Error: PartyStatus entity not found for the given partyId, statusId, and statusDate.")
            return false
        }
    } catch (Exception e) {
        // Catch any exceptions that occur during the database operations
        println("Exception occurred while deleting Person and related entities: ${e.message}")
        return  false
    }
}




