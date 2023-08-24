import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.condition.EntityOperator
import org.apache.ofbiz.entity.util.EntityQuery

String partyIdFrom = parameters.get('partyId')

// Create a map mapOfRelatedParties to hold input parameters
Map<String, Object> mapOfRelatedParties = [:]
mapOfRelatedParties.partyIdFrom = partyIdFrom

// Execute the "getRelatedParties" service and retrieve the list of related party IDs
Map<String, Object> result = runService("getRelatedParties", mapOfRelatedParties)
List<String> relatedPartyIdList = result.get("relatedPartyIdList")

// Create the condition to check if "partyId" is in the relatedPartyIdList
EntityCondition conForPerson = EntityCondition.makeCondition("partyId", EntityOperator.IN, relatedPartyIdList)

// Find parties that satisfy the given condition
Delegator delegator = DelegatorFactory.getDelegator("default")
List<GenericValue> personList = EntityQuery.use(delegator).from("Person").where(conForPerson).queryList()



Map<String, String> partyRoleMap = [:]
List<GenericValue> partyRole = EntityQuery.use(delegator).from("PartyRole").where(conForPerson).queryList()

partyRole.each { partyRoleData ->
    if (!partyRoleMap.containsKey(partyRoleData.partyId)) {
        partyRoleMap[partyRoleData.partyId] = partyRoleData.roleTypeId
    }
}

List<Map> personListPartyRoleAndRelationship = []

EntityCondition conForRelationship = EntityCondition.makeCondition("partyIdFrom", EntityOperator.EQUALS, partyIdFrom)
List<GenericValue> partyRelationshipList = EntityQuery.use(delegator).from("PartyRelationship").where(conForRelationship).queryList()

Map<String,String> firstNameMap = [:]
Map<String,String> middleNameMap = [:]
Map<String,String> lastNameMap = [:]

personList.each { personData ->
    firstNameMap[personData.partyId] = personData.firstName
    lastNameMap[personData.partyId] = personData.lastName
    middleNameMap[personData.partyId] = personData.middleName
}

partyRelationshipList.each{relationshipData ->
    String roleTypeId = partyRoleMap[relationshipData.partyIdTo]
    String firstName = firstNameMap[relationshipData.partyIdTo]
    String middleName = middleNameMap[relationshipData.partyIdTo]
    String lastName = lastNameMap[relationshipData.partyIdTo]
    personListPartyRoleAndRelationship.add([roleTypeId: roleTypeId, firstName: firstName, middleName: middleName, lastName: lastName, partyId: relationshipData.partyIdTo, partyIdFrom: relationshipData.partyIdFrom, roleTypeIdFrom: relationshipData.roleTypeIdFrom, roleTypeIdTo: relationshipData.roleTypeIdTo, roleTypeIdFrom: relationshipData.roleTypeIdFrom, fromDate: relationshipData.fromDate, partyRelationshipTypeId: relationshipData.partyRelationshipTypeId])
}

context.personList = personListPartyRoleAndRelationship
