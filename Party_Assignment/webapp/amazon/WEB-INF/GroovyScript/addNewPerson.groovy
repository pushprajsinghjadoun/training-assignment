import org.apache.ofbiz.entity.GenericValue
import org.apache.ofbiz.entity.Delegator
import org.apache.ofbiz.entity.DelegatorFactory
import org.apache.ofbiz.entity.util.EntityQuery

// Obtain the delegator
Delegator delegator = DelegatorFactory.getDelegator("default")

// RoleType

// Fetch the list of RoleTypes from the database
List<GenericValue> RoleType = EntityQuery.use(delegator).from("RoleType").queryList()
context.RoleType = RoleType

// ContactMechPurposeType

// Fetch the list of ContactMechPurposeTypes from the database
List<GenericValue> ContactMechPurposeTypeList = EntityQuery.use(delegator).from("ContactMechPurposeType").queryList()
context.ContactMechPurposeTypeList = ContactMechPurposeTypeList

// PartyRelationshipType

// Fetch the list of PartyRelationshipTypes from the database
List<GenericValue> PartyRelationshipTypeList = EntityQuery.use(delegator).from("PartyRelationshipType").queryList()
context.PartyRelationshipTypeList = PartyRelationshipTypeList
