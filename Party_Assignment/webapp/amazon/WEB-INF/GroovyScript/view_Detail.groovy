import org.apache.ofbiz.entity.condition.EntityCondition
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.condition.EntityOperator
import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.util.EntityQuery

// Obtain the delegator
Delegator delegator = DelegatorFactory.getDelegator("default")

// Get parameters
String partyId = parameters.partyIdx
String roleTypeId = parameters.roleTypeId

// Initialize the mainPageDetailList to store party details
List<Map<String, Object>> mainPageDetailList = []

// Create the condition to fetch party by partyId
EntityCondition conForParty = EntityCondition.makeCondition(
        EntityCondition.makeCondition("partyId", partyId)
)

// Find party by the given condition
List<GenericValue> partyList = EntityQuery.use(delegator).from("Party").where(conForParty).queryList()
context.partyList = partyList

EntityCondition conForPerson = EntityCondition.makeCondition(
        EntityCondition.makeCondition("partyId", partyId)
)

// Find party by the given condition
List<GenericValue> personList = EntityQuery.use(delegator).from("Person").where(conForPerson).queryList()

String personFirstName = personList[0].firstName
String personMiddleName = personList[0].middleName
String personLastName = personList[0].lastName
// Get the partyTypeId from the partyList and add it to the mainPageDetailList
String partyTypeId = partyList[0].partyTypeId
mainPageDetailList.add([partyId: partyId, roleTypeId: roleTypeId, firstName: personFirstName, middleName: personMiddleName, lastName: personLastName, partyTypeId: partyTypeId])

context.mainPageDetailList = mainPageDetailList

// Create the condition to fetch party status by partyId
EntityCondition partyStatusCon = EntityCondition.makeCondition(
        EntityCondition.makeCondition("partyId", partyId)
)

// Find party status by the given condition
List<GenericValue> partyStatusList = EntityQuery.use(delegator).from("PartyStatus").where(partyStatusCon).queryList()
context.partyStatusList = partyStatusList

// Create the condition to fetch contact purposes by partyId
EntityCondition conForPurpose = EntityCondition.makeCondition(
        EntityCondition.makeCondition("partyId", partyId)
)

// Find contact purposes by the given condition
List<GenericValue> contactPurposeList = EntityQuery.use(delegator).from("PartyContactMechPurpose").where(conForPurpose).queryList()

// Create maps to store contact mech purposes and their from dates
Map<String, String> contactMap = [:]
Map<String, String> contactMapFromDate = [:]

contactPurposeList.each { purpose ->
    contactMap[purpose.contactMechId] = purpose.contactMechPurposeTypeId
    contactMapFromDate[purpose.contactMechId] = purpose.fromDate
}

// Create the condition to fetch contact mechs by contactMechId from the contact purposes list
EntityCondition conForContactMech = EntityCondition.makeCondition(
        EntityCondition.makeCondition("contactMechId", EntityOperator.IN, contactPurposeList.contactMechId)
)

// Find contact mechs by the given condition
List<GenericValue> contactMech = EntityQuery.use(delegator).from("ContactMech").where(conForContactMech).queryList()

// Create a list to store joint purposes (contact mech purposes with their details)
List<Map<String, Object>> jointPurpose = []

contactMech.each { contact ->
    String contactMechPurposeTypeId = contactMap[contact.contactMechId]
    String fromDate = contactMapFromDate[contact.contactMechId]
    jointPurpose.add([contactMechId: contact.contactMechId, contactMechPurposeTypeId: contactMechPurposeTypeId, contactMechTypeId: contact.contactMechTypeId, fromDate: fromDate])
}

context.jointPurpose = jointPurpose

// Create the condition to fetch postal addresses by contactMechId from the contact purposes list
EntityCondition conForPostal = EntityCondition.makeCondition(
        EntityCondition.makeCondition("contactMechId", EntityOperator.IN, contactPurposeList.contactMechId)
)

// Find postal addresses by the given condition
List<GenericValue> postalAddress = EntityQuery.use(delegator).from("PostalAddress").where(conForPostal).queryList()
context.joinPostalList = postalAddress

// Create the condition to fetch telecom numbers by contactMechId from the contact purposes list
EntityCondition conForTele = EntityCondition.makeCondition(
        EntityCondition.makeCondition("contactMechId", EntityOperator.IN, contactPurposeList.contactMechId)
)

// Find telecom numbers by the given condition
List<GenericValue> teleNumber = EntityQuery.use(delegator).from("TelecomNumber").where(conForTele).queryList()
context.teleNumber = teleNumber

// Create the condition to fetch party relationships by partyIdTo
EntityCondition conForRel = EntityCondition.makeCondition(
        EntityCondition.makeCondition("partyIdTo", partyId)
)

// Find party relationships by the given condition
List<GenericValue> partyRelList = EntityQuery.use(delegator).from("PartyRelationship").where(conForRel).queryList()
context.partyRelList = partyRelList

// Get the ContactMech Purpose Type List
List<GenericValue> ContactMechPurposeTypeList = EntityQuery.use(delegator).from("ContactMechPurposeType").queryList()
context.ContactMechPurposeTypeList = ContactMechPurposeTypeList
