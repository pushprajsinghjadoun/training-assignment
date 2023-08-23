package com.companyname.PIES.services;

import java.io.InputStream;
import java.sql.Timestamp;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import org.apache.ofbiz.entity.Delegator;
import org.apache.ofbiz.entity.DelegatorFactory;
import org.apache.ofbiz.entity.GenericEntityException;
import org.apache.ofbiz.entity.GenericValue;
import org.apache.ofbiz.entity.util.EntityQuery;
import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.LocalDispatcher;
import org.apache.ofbiz.service.ServiceContainer;
import org.apache.ofbiz.service.ServiceUtil;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;

public class ReadXmlData {

    public static String getNextSequenceId(Delegator delegator, String sequenceName) {
        String nextSeqId = delegator.getNextSeqId(sequenceName);
        return nextSeqId;
    }
    // Method to read XML data
    public static Map<String, Object> readXmlDataService(DispatchContext dctx, Map<String, ? extends Object> context) {
        // Prepare the result map and start tracking time
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        long startCountTime = System.currentTimeMillis();
        try {
            // Create XMLInputFactory instance for XML parsing
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();

            // Open an input stream to the XML file
            InputStream inputStream = new FileInputStream("file/"+context.get("fileName"));

            // Create an XMLStreamReader for parsing the XML content
            XMLStreamReader reader = xmlInputFactory.createXMLStreamReader(inputStream);

            // Create a new instance of XMLDataProcessor to handle XML data extraction
            XMLDataProcessor dataProcessor = new XMLDataProcessor(reader);

            // Invoke the data extraction method using the created instance
            dataProcessor.extractData();

            // Close the XMLStreamReader to release resources
            reader.close();

            // Close the InputStream used for reading the XML file
            inputStream.close();
        } catch (XMLStreamException e) {
            // Handle XML parsing exceptions
            e.printStackTrace();
        } catch (Exception e) {
            // Handle other exceptions
            e.printStackTrace();
        }

        // Calculate the total time taken for XML processing
        long endCountTime = System.currentTimeMillis();
        System.out.println("Time taken for counting: " + (endCountTime - startCountTime) + " ms");

        // Return the result map after XML processing
        return resultMap;
    }

    // A class to encapsulate XML data processing logic
    private static class XMLDataProcessor {
        private final XMLStreamReader reader;

        public XMLDataProcessor(XMLStreamReader reader) {
            this.reader = reader;
        }


        public void extractData() throws XMLStreamException, GenericEntityException {
            // Initialize Map and List to store the data of Item
            // Map to store Main product value
            Map<String, Object> mainProductValue = new HashMap<>();
            // flag to check if partNumber is from partInterChange
            Boolean isPartInterchange = false;
            // List of Map to store product Description
            List<Map<String,String>> productDescription = new ArrayList<>();
            // List of Map to store product Extended Information
            List<Map<String,String>> productExtendedInformation = new ArrayList<>();
            // List of Map to store product attributes
            List<Map<String,String>> productAttribute = new ArrayList<>();
            // Map to store Package Information
            Map<String,Object> packageInformation = new HashMap<>();
            // List of Map to store partInterchange values
            List<Map<String,String>> partInterchangeValues = new ArrayList<>();
            // Map to store partInterchange values
            Map<String,String> partInterchangeValueMap = new HashMap<>();
            // List of Map to store product DigitalFileInformation
            List<Map<String, String>> productDigitalFileInformationList = new ArrayList<>();
            // Map to store DigitalFileInformation
            Map<String, String> productFileInformationMap = new HashMap<>();

            // Iterate through XML elements and extract data
            while (reader.hasNext()) {
                int event = reader.next();
                // Extract the appropriate values
                switch (event)
                {
                    // Check if the current event is the start of an XML element
                    case XMLStreamConstants.START_ELEMENT:
                        // Get the local name of the current element
                        String localName = reader.getLocalName();
                        // Get the value of a specific XML element with switch case.
                        switch (localName)
                        {
                            case "Item":
                                break;
                            case "HazardousMaterialCode":
                                // Extract the content of the current XML element as text
                                String hazardousMaterialCode = reader.getElementText();
                                mainProductValue.put("HazardousMaterialCode",hazardousMaterialCode);
                                break;
                            case "ItemLevelGTIN":
                                // Get the value of the "gtinQualifier" attribute from the current XML element
                                String gtinQualifier = reader.getAttributeValue(null, "GTINQualifier");
                                // Extract the content of the current XML element as text
                                String gtin = reader.getElementText();
                                mainProductValue.put("ItemLevelGTIN",gtin);
                                break;
                            case  "Description":
                                String descriptionCode = reader.getAttributeValue(null, "DescriptionCode");
                                String languageCode = reader.getAttributeValue(null, "LanguageCode");
                                String maintenanceType = reader.getAttributeValue(null, "MaintenanceType");
                                String descriptionText = reader.getElementText();
                                if(descriptionCode.equals("DEF"))
                                {
                                    mainProductValue.put("productName",descriptionText);
                                }
                                else if(descriptionCode.equals("DES"))
                                {
                                    mainProductValue.put("description",descriptionText);
                                }
                                else if(descriptionCode.equals("MKT"))
                                {
                                    mainProductValue.put("longDescription",descriptionText);
                                }else
                                {
                                    Map<String,String> productDescriptionValueMap = new HashMap<>();
                                    productDescriptionValueMap.put("description",descriptionText);
                                    productDescriptionValueMap.put("languageCode",languageCode);
                                    productDescriptionValueMap.put("maintenanceType",maintenanceType);
                                    productDescriptionValueMap.put("descriptionCode",descriptionCode);
                                    productDescription.add(productDescriptionValueMap);
                                }
                                break;
                            case  "ACESApplications":
                                String acesApplications = reader.getElementText();
                                mainProductValue.put("acesApplications",acesApplications);
                                break;
                            case  "ItemQuantitySize":
                                String itemQuantitySizeUOM = reader.getAttributeValue(null, "UOM");
                                String itemQuantitySize = reader.getElementText();
                                mainProductValue.put("itemQuantitySize",itemQuantitySize);
                                mainProductValue.put("itemQuantitySizeUOM",itemQuantitySizeUOM);
                                break;
                            case  "ContainerType":
                                String containerType = reader.getElementText();
                                mainProductValue.put("containerType",containerType);
                                break;
                            case  "QuantityPerApplication":
                                String quantityPerApplication = reader.getElementText();
                                mainProductValue.put("quantityPerApplication",quantityPerApplication);
                                break;
                            case  "ItemEffectiveDate":
                                String itemEffectiveDate = reader.getElementText();
                                mainProductValue.put("ItemEffectiveDate",itemEffectiveDate);
                                break;
                            case  "AvailableDate":
                                String availableDate = reader.getElementText();
                                mainProductValue.put("availableDate",availableDate);
                                break;
                            case  "MinimumOrderQuantity":
                                String minimumOrderQuantity = reader.getElementText();
                                mainProductValue.put("minimumOrderQuantity",minimumOrderQuantity);
                                break;
                            case  "AAIAProductCategoryCode":
                                String aaiapProductCategoryCode = reader.getElementText();
                                mainProductValue.put("aaiapProductCategoryCode",aaiapProductCategoryCode);
                                break;
                            case  "PartTerminologyID":
                                String partTerminologyID = reader.getElementText();
                                break;
                            case  "ExtendedProductInformation":
                                String expiCode = reader.getAttributeValue(null, "EXPICode");
                                String extendedProductLanguageCode = reader.getAttributeValue(null, "LanguageCode");
                                String maintenanceTypeExtendedProduct = reader.getAttributeValue(null, "MaintenanceType");
                                String extendedProductInformationValue = reader.getElementText();
                                Map<String,String> productExtendedInformationMap = new HashMap<>();
                                if(!(extendedProductLanguageCode==null))
                                {
                                    productExtendedInformationMap.put("expiCode",expiCode);
                                    productExtendedInformationMap.put("extendedProductLanguageCode",extendedProductLanguageCode);
                                    productExtendedInformationMap.put("maintenanceTypeExtendedProduct",maintenanceTypeExtendedProduct);
                                    productExtendedInformationMap.put("extendedProductInformationValue",extendedProductInformationValue);
                                    productExtendedInformation.add(productExtendedInformationMap);
                                }
                                break;
                            case  "ProductAttribute":
                                String attributeID = reader.getAttributeValue(null, "AttributeID");
                                String maintenanceTypeProductAttributes = reader.getAttributeValue(null, "MaintenanceType");
                                String padbAttribute = reader.getAttributeValue(null, "PADBAttribute");
                                String recordNumber = reader.getAttributeValue(null, "RecordNumber");
                                String productAttributeValue = reader.getElementText();
                                Map<String,String> productAttributeMap = new HashMap<>();
                                productAttributeMap.put("attributeID",attributeID);
                                productAttributeMap.put("maintenanceTypeProductAttributes",maintenanceTypeProductAttributes);
                                productAttributeMap.put("padbAttribute",padbAttribute);
                                productAttributeMap.put("recordNumber",recordNumber);
                                productAttributeMap.put("productAttributeValue",productAttributeValue);
                                productAttribute.add(productAttributeMap);
                                break;
                            case  "PackageBarCodeCharacters":
                                String packageBarCodeCharacters = reader.getElementText();
                                packageInformation.put("shipmentPackageSeqId",packageBarCodeCharacters);
                                break;
                            case  "Dimensions":
                                String dimensionsUOM = reader.getAttributeValue(null, "UOM");
                                packageInformation.put("dimensionUomId",dimensionsUOM);
                                break;
                            case  "Height":
                                String height = reader.getElementText();
                                packageInformation.put("boxHeight",height);
                                break;
                            case  "Width":
                                String width = reader.getElementText();
                                packageInformation.put("boxWeight",width);
                                break;
                            case  "Length":
                                String length = reader.getElementText();
                                packageInformation.put("boxLength",length);
                                break;
                            case  "Weights":
                                String weightsUOM = reader.getAttributeValue(null, "UOM");
                                packageInformation.put("weightUomId",weightsUOM);
                                break;
                            case  "Weight":
                                String weight = reader.getElementText();
                                packageInformation.put("weight",weight);
                                break;
                            case  "TypeCode":
                                String typeCode = reader.getElementText();
                                partInterchangeValueMap.put("typeCode",typeCode);
                                break;
                            case  "PartInterchange":
                                partInterchangeValueMap.clear();
                                break;
                            case  "BrandAAIAID":
                                String brandAAIAID = reader.getElementText();
                                if(!isPartInterchange)
                                {
                                    mainProductValue.put("BrandAAIAID",brandAAIAID);
                                }else{
                                    partInterchangeValueMap.put("brandAAIAID",brandAAIAID);
                                }

                                break;
                            case  "PartNumber":
                                String partNumber = reader.getElementText();

                                if(!isPartInterchange)
                                {
                                    mainProductValue.put("partNumber",partNumber);
                                }else
                                {
                                    partInterchangeValueMap.put("partNumber",partNumber);
                                }
                                break;
                            case  "BrandLabel":
                                String brandLabel = reader.getElementText();
                                if(!isPartInterchange)
                                {
                                    mainProductValue.put("BrandLabel",brandLabel);
                                }else
                                {
                                    partInterchangeValueMap.put("brandLabel",brandLabel);
                                }
                                break;
                            case "PartInterchangeInfo":
                                isPartInterchange = true;
                                break;
                            case  "DigitalFileInformation":
                                productFileInformationMap.clear();
                                String maintenanceTypeDigital = reader.getAttributeValue(null, "MaintenanceType");
                                String languageCodeDigital = reader.getAttributeValue(null, "LanguageCode");
                                productFileInformationMap.put("maintenanceTypeDigital",maintenanceTypeDigital);
                                productFileInformationMap.put("languageCodeDigital",languageCodeDigital);
                                break;
                            case  "AssetType":
                                String assetType = reader.getElementText();
                                productFileInformationMap.put("assetType",assetType);
                                break;
                            case  "FileName":
                                String fileName = reader.getElementText();
                                productFileInformationMap.put("FileName",fileName);
                                break;
                            case  "FileType":
                                String fileType = reader.getElementText();
                                productFileInformationMap.put("fileType",fileType);
                                break;
                            case  "Representation":
                                String representation = reader.getElementText();
                                productFileInformationMap.put("representation",representation);
                                break;
                            case  "Background":
                                String background = reader.getElementText();
                                productFileInformationMap.put("background",background);
                                break;
                            case  "AssetHeight":
                                String assetHeight = reader.getElementText();
                                productFileInformationMap.put("assetHeight",assetHeight);
                                break;
                            case  "AssetWidth":
                                String assetWidth = reader.getElementText();
                                productFileInformationMap.put("assetWidth",assetWidth);
                                break;
                            case  "FilePath":
                                String filePath = reader.getElementText();
                                productFileInformationMap.put("filePath",filePath);
                                break;
                            case  "URI":
                                String uri = reader.getElementText();
                                productFileInformationMap.put("uri",uri);
                                break;
                            case  "AssetDimensions":
                                String assetDimensionsUOM = reader.getAttributeValue(null, "UOM");
                                productFileInformationMap.put("assetDimensionsUOM",assetDimensionsUOM);
                                break;
                            case  "AssetID":
                                String assetID = reader.getElementText();
                                productFileInformationMap.put("assetID",assetID);
                                break;
                            case  "Resolution":
                                String resolution = reader.getElementText();
                                productFileInformationMap.put("resolution",resolution);
                                break;
                            case  "FileSize":
                                String fileSize = reader.getElementText();
                                productFileInformationMap.put("fileSize",fileSize);
                                break;
                        }
                        break;
                    case XMLStreamConstants.END_ELEMENT:
                        String localNameEnd = reader.getLocalName();
                        switch (localNameEnd)
                        {
                            case "PartInterchange":
                                partInterchangeValues.add(new HashMap<>(partInterchangeValueMap));

                                break;
                            case "PartInterchangeInfo":
                                isPartInterchange = false;
                                break;
                            case "DigitalFileInformation":
                                productDigitalFileInformationList.add(new HashMap<>(productFileInformationMap));
                                break;
                            case "Item":
                                // Check if the product ID exists
                                // Create an object to check if the product ID exists
                                CheckIfProductIdExists checkIfProductIdExistsObject = new CheckIfProductIdExists((String) mainProductValue.get("partNumber"));
                                boolean exists;
                                try {
                                    // Perform the product ID existence check
                                    exists = checkIfProductIdExistsObject.checkIfProductIdExists();
                                    // If product exists then do not Insert data
                                    if (exists) {
                                        // Update product details
                                        UpdateProductDetails updateProductDetailsObject = new UpdateProductDetails(mainProductValue);
                                        updateProductDetailsObject.UpdateProductDetails();
                                        // Update product attributes
                                        UpdateProductAttributes updateProductAttributes = new UpdateProductAttributes(productAttribute,(String) mainProductValue.get("partNumber"));
                                        updateProductAttributes.updateProductAttributes();

                                        mainProductValue.clear();
                                        System.out.println("Product Updated.");
                                    } else { // if product do not exist then insert data
                                        // Insert Product details
                                        InsertProductDetails insertProductDetailsObject = new InsertProductDetails(mainProductValue);
                                        insertProductDetailsObject.insertProductDetails();
                                        // Insert Product Content details
                                        InsertProductContent insertProductContentObject = new InsertProductContent(productDescription,productExtendedInformation, (String) mainProductValue.get("partNumber"));
                                        insertProductContentObject.insertProductContent();
                                        // Insert Product attributes details
                                        InsertProductAttributes insertProductAttributes = new InsertProductAttributes(productAttribute,(String) mainProductValue.get("partNumber"));
                                        insertProductAttributes.InsertProductAttributes();
                                        // Insert Product Package details
                                        InsertProductPackage insertPackageDetailsObject = new InsertProductPackage(packageInformation);
                                        insertPackageDetailsObject.insertPackageDetails();
                                        // Insert PartInterChange Details
                                        InsertPartInterchange insertPartInterchange = new InsertPartInterchange(partInterchangeValues,(String) mainProductValue.get("partNumber"));
                                        insertPartInterchange.insertPartInterchange();
                                        // Insert DigitalFileInformation
                                        InsertDigitalFileInformation insertDigitalFileInformationObject = new InsertDigitalFileInformation(productDigitalFileInformationList,(String) mainProductValue.get("partNumber"));
                                        insertDigitalFileInformationObject.insertDigitalInfo();

                                        // Clear List and Map to prepare for the next set of item data
                                        mainProductValue.clear();
                                        productDescription.clear();
                                        productAttribute.clear();
                                        productExtendedInformation.clear();
                                        packageInformation.clear();
                                        productDigitalFileInformationList.clear();
                                        partInterchangeValues.clear();
                                        productAttribute.clear();
                                    }
                                } catch (GenericEntityException e) {
                                    e.printStackTrace();
                                }
                                break;
                        }
                        break;
                }
            }
        }
    }

    // Nested class to insert product details
    public static class InsertProductDetails{
        // Store the main product values in a map
        private Map<String,Object> mainProductValue = new HashMap<>();
        // Constructor to initialize main product values
        InsertProductDetails(Map<String,Object> mainProductValue){
            this.mainProductValue = mainProductValue;
        }
        // Method to insert product details
        private void insertProductDetails()
        {
            try
            {
                // Get the Delegator instance
                Delegator delegator = DelegatorFactory.getDelegator("default");
                // Get the LocalDispatcher instance
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                // Get the UserLogin for system operations
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                // Prepare input parameters for creating the product
                Map<String, Object> inputParameters = new HashMap<>();
                Map<String, Object> goodIdentificationInputParameters = new HashMap<>();

                inputParameters.put("productId", mainProductValue.get("partNumber"));
                inputParameters.put("productName", mainProductValue.get("productName"));
                inputParameters.put("internalName", mainProductValue.get("BrandAAIAID"));
                inputParameters.put("brandName", mainProductValue.get("BrandLabel"));
                inputParameters.put("productTypeId", "GOOD");
                inputParameters.put("releaseDate", mainProductValue.get("availableDate"));
                inputParameters.put("introductionDate", mainProductValue.get("ItemEffectiveDate"));
                inputParameters.put("quantityUomId", mainProductValue.get("itemQuantitySizeUOM"));
                inputParameters.put("quantityIncluded", mainProductValue.get("itemQuantitySize"));
                inputParameters.put("piecesIncluded", mainProductValue.get("quantityPerApplication"));
                inputParameters.put("configId", mainProductValue.get("acesApplications"));
                inputParameters.put("comments", mainProductValue.get("containerType"));
                inputParameters.put("description", mainProductValue.get("description"));
                inputParameters.put("longDescription", mainProductValue.get("longDescription"));
                inputParameters.put("autoCreateKeywords",mainProductValue.get("HazardousMaterialCode"));
                inputParameters.put("billOfMaterialLevel",mainProductValue.get("aaiapProductCategoryCode"));
                inputParameters.put("userLogin", permUserLogin);

                // Call the service to create the product
                Map<String, Object> result = dispatcher.runSync("createProduct", inputParameters);

                // Check the result of the service call
                if (ServiceUtil.isSuccess(result)) {
                    System.out.println(result);
                } else {
                    System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(result));
                }
                // prepare good identification values
                goodIdentificationInputParameters.put("productId", mainProductValue.get("partNumber"));
                goodIdentificationInputParameters.put("goodIdentificationTypeId","OTHER_ID");
                goodIdentificationInputParameters.put("idValue",mainProductValue.get("ItemLevelGTIN"));
                goodIdentificationInputParameters.put("userLogin", permUserLogin);

                // Call the service to create good identification
                Map<String, Object> goodIdentificationResult = dispatcher.runSync("createGoodIdentification", goodIdentificationInputParameters);

                // Check the result of the service call
                if (ServiceUtil.isSuccess(goodIdentificationResult)) {
                    System.out.println(goodIdentificationResult);
                } else {
                    System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(goodIdentificationResult));
                }

            }
            catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }

    // Nested class to insert product content
    public static class InsertProductContent{
        // Lists to store product descriptions, extended information, and attributes
        private List<Map<String,String>> productDescription;
        private List<Map<String,String>> productExtendedInformation;
        private String productId;

        // Constructor to initialize product content data and product ID
        InsertProductContent(List<Map<String,String>> productDescription, List<Map<String,String>> productExtendedInformation, String productId)
        {
            this.productDescription = productDescription;
            this.productExtendedInformation = productExtendedInformation;
            this.productId = productId;
        }

        // Method to insert product content
        private void insertProductContent()
        {
            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                Map<String, Object> descriptionInput = new HashMap<>();
                // Loop through each description and insert content
                for (Map<String,String> descriptionMap : productDescription) {
                    Map<String, Object> productContentInput = new HashMap<>();
                    // Prepare content description values to insert
                    String contentId = getNextSequenceId(delegator, "ContentSeqId");
                    descriptionInput.put("contentId", contentId);
                    descriptionInput.put("contentName", descriptionMap.get("descriptionCode"));
                    descriptionInput.put("localeString", descriptionMap.get("languageCode"));
                    descriptionInput.put("description", descriptionMap.get("description"));
                    descriptionInput.put("serviceName", descriptionMap.get("maintenanceType"));
                    descriptionInput.put("userLogin", permUserLogin);
                    // Call service to create content
                    Map<String, Object> resultContent = dispatcher.runSync("createContent", descriptionInput);
                    if (ServiceUtil.isSuccess(resultContent)) {
                        // Process the result as needed
                        System.out.println(resultContent);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultContent));
                    }
                    // Insert product content association
                    productContentInput.put("contentId", contentId);
                    productContentInput.put("productId", productId);
                    productContentInput.put("productContentTypeId", "DESCRIPTION");
                    Timestamp currentTimestamp = new Timestamp(new Date().getTime());
                    productContentInput.put("fromDate", currentTimestamp);
                    productContentInput.put("userLogin", permUserLogin);
                    // Call service to create product content
                    Map<String, Object> resultProductContent = dispatcher.runSync("createProductContent", productContentInput);
                    if (ServiceUtil.isSuccess(resultProductContent)) {
                        // Process the result as needed
                        System.out.println(resultProductContent);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductContent));
                    }
                }
                // Loop through each product extended information and insert content
                for ( Map<String,String> productExtendedInformationMap: productExtendedInformation) {
                    Map<String, Object> productExtendedInformationInput = new HashMap<>();
                    Map<String, Object> productContentInput = new HashMap<>();

                    String contentId = getNextSequenceId(delegator, "ContentSeqId");
                    // Prepare product extended information  values to insert
                    productExtendedInformationInput.put("contentId", contentId);
                    productExtendedInformationInput.put("contentName", productExtendedInformationMap.get("expiCode"));
                    productExtendedInformationInput.put("localeString", productExtendedInformationMap.get("extendedProductLanguageCode"));
                    productExtendedInformationInput.put("serviceName", productExtendedInformationMap.get("maintenanceTypeExtendedProduct"));
                    productExtendedInformationInput.put("description", productExtendedInformationMap.get("extendedProductInformationValue"));
                    productExtendedInformationInput.put("userLogin", permUserLogin);
                    // Call service to create content
                    Map<String, Object> resultProductExtended = dispatcher.runSync("createContent", productExtendedInformationInput);
                    if (ServiceUtil.isSuccess(resultProductExtended)) {
                        // Process the result as needed
                        System.out.println(resultProductExtended);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductExtended));
                    }
                    productContentInput.put("contentId", contentId);
                    productContentInput.put("productId", productId);
                    productContentInput.put("productContentTypeId", "LONG_DESCRIPTION");
                    productContentInput.put("userLogin", permUserLogin);
                    // Call service to create product content
                    Map<String, Object> resultProductContent = dispatcher.runSync("createProductContent", productContentInput);
                    if (ServiceUtil.isSuccess(resultProductContent)) {
                        // Process the result as needed
                        System.out.println(resultProductContent);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductContent));
                    }

                }
            }
            catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }

    // Nested class to insert Product Attributes
    private static class InsertProductAttributes
    {
        private List<Map<String,String>> productAttribute;
        private String productId;

        InsertProductAttributes(List<Map<String,String>> productAttribute,String productId)
        {
            this.productAttribute = productAttribute;
            this.productId = productId;
        }
        // Method to insert product attributes

        private void InsertProductAttributes()
        {
            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                // prepare product attributes values
                for (Map<String,String> productAttributeMap : productAttribute) {
                    Map<String, Object> productAttributeInput = new HashMap<>();
                    productAttributeInput.put("productId", productId);
                    productAttributeInput.put("attrName", productAttributeMap.get("attributeID"));
                    productAttributeInput.put("attrValue", productAttributeMap.get("maintenanceTypeProductAttributes"));
                    productAttributeInput.put("attrType", productAttributeMap.get("padbAttribute"));
                    productAttributeInput.put("attrDescription", productAttributeMap.get("productAttributeValue"));
                    productAttributeInput.put("userLogin", permUserLogin);

                    // Call service to create product attributes
                    Map<String, Object> resultProductAttribute = dispatcher.runSync("createProductAttribute", productAttributeInput);
                    if (ServiceUtil.isSuccess(resultProductAttribute)) {
                        // Process the result as needed
                        System.out.println(resultProductAttribute);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductAttribute));
                    }
                }
            }catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }

    }

    // Nested class to insert product package details
    public static class InsertProductPackage{
        // Store package information in a map
        private Map<String, Object> packageInformation;
        // Constructor to initialize package information
        InsertProductPackage(Map<String, Object> packageInformation)
        {
            this.packageInformation = packageInformation;
        }
        // Method to insert package details
        private void insertPackageDetails()
        {
            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                Map<String, Object> packageInfoInput = new HashMap<>();
                Map<String, Object> shipmentInput = new HashMap<>();

                // Create a shipment
                String shipmentId = getNextSequenceId(delegator, "ShipmentSeqId");
                shipmentInput.put("shipmentId",shipmentId);
                shipmentInput.put("userLogin", permUserLogin);
                // call createShipment service to insert the data
                Map<String, Object> resultShipment = dispatcher.runSync("createShipment", shipmentInput);
                if (ServiceUtil.isSuccess(resultShipment)) {
                    // Process the result as needed
                    System.out.println(resultShipment);
                } else {
                    System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultShipment));
                }
                // Prepare input for creating shipment package
                packageInfoInput.put("shipmentId",shipmentId);
                packageInfoInput.put("shipmentPackageSeqId",packageInformation.get("shipmentPackageSeqId"));
                packageInfoInput.put("weightUomId",packageInformation.get("weightUomId"));
                packageInfoInput.put("dimensionUomId",packageInformation.get("dimensionUomId"));
                packageInfoInput.put("boxLength",packageInformation.get("boxLength"));
                packageInfoInput.put("boxWeight",packageInformation.get("boxWeight"));
                packageInfoInput.put("boxHeight",packageInformation.get("boxHeight"));
                packageInfoInput.put("weight",packageInformation.get("weight"));
                packageInfoInput.put("insuredValue",packageInformation.get("insuredValue"));
                packageInfoInput.put("userLogin", permUserLogin);

                // call createShipmentPackage service to insert the data.
                Map<String, Object> resultPackage = dispatcher.runSync("createShipmentPackage", packageInfoInput);
                if (ServiceUtil.isSuccess(resultPackage)) {
                    // Process the result as needed
                    System.out.println(resultPackage);
                } else {
                    System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultPackage));
                }
            }
            catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }

    // Nested class to insert part interchange values
    private static class InsertPartInterchange{
        // Lists to store part interchange values and parent product ID
        private List<Map<String,String>> partInterchangeValues;
        private String productIdParent;
        // Constructor to initialize part interchange values and parent product ID
        InsertPartInterchange(List<Map<String,String>> partInterchangeValues, String productIdParent)
        {
            this.partInterchangeValues = partInterchangeValues;
            this.productIdParent = productIdParent;
        }
        // Method to insert part interchange values
        private void insertPartInterchange()
        {
            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                Map<String, Object> productPartInput = new HashMap<>();
                Map<String, Object> productPartAssocInput = new HashMap<>();
                // Insert part interchange values
                for (Map<String,String> partInterchangeValueMap : partInterchangeValues) {
                    String productId = getNextSequenceId(delegator, "ProductSeqId");
                    productPartInput.put("productId", productId);
                    productPartInput.put("brandName", partInterchangeValueMap.get("brandLabel"));
                    productPartInput.put("internalName", partInterchangeValueMap.get("partNumber"));
                    productPartInput.put("productName", partInterchangeValueMap.get("brandAAIAID"));
                    productPartInput.put("comments", partInterchangeValueMap.get("typeCode"));
                    productPartInput.put("productTypeId", "SUBASSEMBLY");
                    productPartInput.put("userLogin",permUserLogin);
                    // Call the service to create the new product
                    Map<String, Object> resultProduct = dispatcher.runSync("createProduct", productPartInput);
                    if (ServiceUtil.isSuccess(resultProduct)) {
                        // Process the result as needed
                        System.out.println(resultProduct);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProduct));
                    }

                    // Create a product association between the new part and the parent product
                    productPartAssocInput.put("productIdTo", productId);
                    productPartAssocInput.put("productId",productIdParent);
                    long currentTimeMillis = System.currentTimeMillis();
                    Timestamp timestamp = new Timestamp(currentTimeMillis);
                    productPartAssocInput.put("fromDate",timestamp);
                    productPartAssocInput.put("productAssocTypeId","PRODUCT_ACCESSORY");
                    productPartAssocInput.put("userLogin",permUserLogin);
                    // Call the service to create the product association
                    Map<String, Object> resultProductAssoc = dispatcher.runSync("createProductAssoc", productPartAssocInput);
                    if (ServiceUtil.isSuccess(resultProductAssoc)) {
                        System.out.println(resultProductAssoc);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductAssoc));
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }

    // Nested class to insert digital file information
    private static class InsertDigitalFileInformation
    {
        // Lists to store digital file information and product ID
        private List<Map<String,String>> digitalValues;
        private String productId;
        // Constructor to initialize digital file information and product ID
        InsertDigitalFileInformation(List<Map<String,String>> digitalValues, String productId)
        {
            this.digitalValues = digitalValues;
            this.productId = productId;
        }
        // Method to insert digital file information
        private void insertDigitalInfo()
        {

            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();

                // Map's to hold the Digital File information data
                Map<String, Object> digitalInfoInput = new HashMap<>();
                Map<String, Object> digitalInfoInputAssetId = new HashMap<>();
                Map<String, Object> digitalInfoInputRepresentation = new HashMap<>();
                Map<String, Object> digitalInfoInputBackground= new HashMap<>();
                Map<String, Object> digitalInfoInputMaintenanceType= new HashMap<>();
                Map<String, Object> digitalInfoInputLanguageCode= new HashMap<>();
                Map<String, Object> digitalInfoInputResolution= new HashMap<>();
                Map<String, Object> digitalInfoInputFileSize= new HashMap<>();
                Map<String, Object> digitalInfoInputAssetHeight= new HashMap<>();
                Map<String, Object> digitalInfoInputAssetWeight= new HashMap<>();
                Map<String, Object> digitalInfoInputFilePath= new HashMap<>();
                Map<String, Object> productContentInput = new HashMap<>();
                // Prepare digital file information
                for ( Map<String, String> digitalValue : digitalValues) {
                    String contentId = getNextSequenceId(delegator, "ContentSeqId");

                    // Store data which will be store in content entity
                    digitalInfoInput.put("userLogin", permUserLogin);
                    digitalInfoInput.put("contentId",contentId);
                    digitalInfoInput.put("contentName",digitalValue.get("assetType"));
                    digitalInfoInput.put("serviceName",digitalValue.get("FileName"));
                    digitalInfoInput.put("description",digitalValue.get("uri"));
                    digitalInfoInput.put("localeString",digitalValue.get("fileType"));
                    // Store Asset data which will be store in contentAttribute entity
                    digitalInfoInputAssetId.put("userLogin", permUserLogin);
                    digitalInfoInputAssetId.put("contentId",contentId);
                    digitalInfoInputAssetId.put("attrDescription",digitalValue.get("assetID"));
                    digitalInfoInputAssetId.put("attrName","Asset ID");
                    // Store Maintenance data which will be store in contentAttribute entity
                    digitalInfoInputMaintenanceType.put("userLogin", permUserLogin);
                    digitalInfoInputMaintenanceType.put("contentId",contentId);
                    digitalInfoInputMaintenanceType.put("attrDescription",digitalValue.get("maintenanceTypeDigital"));
                    digitalInfoInputMaintenanceType.put("attrName","Maintenance Type");
                    // Store Background data which will be store in contentAttribute entity
                    digitalInfoInputBackground.put("userLogin", permUserLogin);
                    digitalInfoInputBackground.put("contentId",contentId);
                    digitalInfoInputBackground.put("attrDescription",digitalValue.get("background"));
                    digitalInfoInputBackground.put("attrName","Background");
                    // Store Representation data which will be store in contentAttribute entity
                    digitalInfoInputRepresentation.put("userLogin", permUserLogin);
                    digitalInfoInputRepresentation.put("contentId",contentId);
                    digitalInfoInputRepresentation.put("attrDescription",digitalValue.get("representation"));
                    digitalInfoInputRepresentation.put("attrName","Representation");
                    // Store LanguageCode data which will be store in contentAttribute entity
                    digitalInfoInputLanguageCode.put("userLogin", permUserLogin);
                    digitalInfoInputLanguageCode.put("contentId",contentId);
                    digitalInfoInputLanguageCode.put("attrDescription",digitalValue.get("languageCodeDigital"));
                    digitalInfoInputLanguageCode.put("attrName","Language Code");
                    // Store Resolution data which will be store in contentAttribute entity
                    digitalInfoInputResolution.put("userLogin", permUserLogin);
                    digitalInfoInputResolution.put("contentId",contentId);
                    digitalInfoInputResolution.put("attrDescription",digitalValue.get("resolution"));
                    digitalInfoInputResolution.put("attrName","Resolution");
                    // Store FileSize data which will be store in contentAttribute entity
                    digitalInfoInputFileSize.put("userLogin", permUserLogin);
                    digitalInfoInputFileSize.put("contentId",contentId);
                    digitalInfoInputFileSize.put("attrDescription",digitalValue.get("fileSize"));
                    digitalInfoInputFileSize.put("attrName","File Size");
                    // Store Asset Height data which will be store in contentAttribute entity
                    digitalInfoInputAssetHeight.put("userLogin", permUserLogin);
                    digitalInfoInputAssetHeight.put("contentId",contentId);
                    digitalInfoInputAssetHeight.put("attrDescription",digitalValue.get("assetHeight"));
                    digitalInfoInputAssetHeight.put("attrName","Asset Height");
                    // Store Asset Width data which will be store in contentAttribute entity
                    digitalInfoInputAssetWeight.put("userLogin", permUserLogin);
                    digitalInfoInputAssetWeight.put("contentId",contentId);
                    digitalInfoInputAssetWeight.put("attrDescription",digitalValue.get("assetWidth"));
                    digitalInfoInputAssetWeight.put("attrName","Asset Weight");
                    // Store file path data which will be store in contentAttribute entity
                    digitalInfoInputFilePath.put("userLogin", permUserLogin);
                    digitalInfoInputFilePath.put("contentId",contentId);
                    digitalInfoInputFilePath.put("attrDescription",digitalValue.get("filePath"));
                    digitalInfoInputFilePath.put("attrName","File Path");

                    // Call the service to create the new content
                    Map<String, Object> resultDigital = dispatcher.runSync("createContent", digitalInfoInput);
                    if (ServiceUtil.isSuccess(resultDigital)) {
                        // Process the result as needed
                        System.out.println(resultDigital);
                        Map<String, Object> resultDigitalAssetId = dispatcher.runSync("createContentAttribute", digitalInfoInputAssetId);
                        if (ServiceUtil.isSuccess(resultDigitalAssetId)) {
                            // Process the result as needed
                            System.out.println(resultDigitalAssetId);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalAssetId));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalMaintenance = dispatcher.runSync("createContentAttribute",digitalInfoInputMaintenanceType);
                        if (ServiceUtil.isSuccess(resultDigitalMaintenance)) {
                            // Process the result as needed
                            System.out.println(resultDigitalMaintenance);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalMaintenance));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalBackground = dispatcher.runSync("createContentAttribute",digitalInfoInputBackground);
                        if (ServiceUtil.isSuccess(resultDigitalBackground)) {
                            // Process the result as needed
                            System.out.println(resultDigitalBackground);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalBackground));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalRepresentation = dispatcher.runSync("createContentAttribute",digitalInfoInputRepresentation);
                        if (ServiceUtil.isSuccess(resultDigitalRepresentation)) {
                            // Process the result as needed
                            System.out.println(resultDigitalRepresentation);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalRepresentation));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalLanguageCode = dispatcher.runSync("createContentAttribute",digitalInfoInputLanguageCode);
                        if (ServiceUtil.isSuccess(resultDigitalLanguageCode)) {
                            // Process the result as needed
                            System.out.println(resultDigitalLanguageCode);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalLanguageCode));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalResolution = dispatcher.runSync("createContentAttribute",digitalInfoInputResolution);
                        if (ServiceUtil.isSuccess(resultDigitalResolution)) {
                            // Process the result as needed
                            System.out.println(resultDigitalResolution);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalResolution));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalFileSize = dispatcher.runSync("createContentAttribute",digitalInfoInputFileSize);
                        if (ServiceUtil.isSuccess(resultDigitalFileSize)) {
                            // Process the result as needed
                            System.out.println(resultDigitalFileSize);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalFileSize));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalAssetHeight = dispatcher.runSync("createContentAttribute",digitalInfoInputAssetHeight);
                        if (ServiceUtil.isSuccess(resultDigitalAssetHeight)) {
                            // Process the result as needed
                            System.out.println(resultDigitalAssetHeight);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalAssetHeight));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalAssetWeight = dispatcher.runSync("createContentAttribute",digitalInfoInputAssetWeight);
                        if (ServiceUtil.isSuccess(resultDigitalAssetWeight)) {
                            // Process the result as needed
                            System.out.println(resultDigitalAssetWeight);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalAssetWeight));
                        }
                        // call the service to create the new content attributes
                        Map<String, Object> resultDigitalFilePath = dispatcher.runSync("createContentAttribute",digitalInfoInputFilePath);
                        if (ServiceUtil.isSuccess(resultDigitalFilePath)) {
                            // Process the result as needed
                            System.out.println(resultDigitalFilePath);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalFilePath));
                        }
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigital));
                    }
                    // Prepare a product content association for digital file information
                    productContentInput.put("contentId", contentId);
                    productContentInput.put("productId", productId);
                    productContentInput.put("productContentTypeId", "DIGITAL_DOWNLOAD");
                    productContentInput.put("userLogin", permUserLogin);
                    // Call the service to create the product content association
                    Map<String, Object> resultProductContent = dispatcher.runSync("createProductContent", productContentInput);
                    if (ServiceUtil.isSuccess(resultProductContent)) {
                        // Process the result as needed
                        System.out.println(resultProductContent);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductContent));
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }

    // Nested class to check if a product ID exists
    private static class CheckIfProductIdExists{
        // Store the product ID
        private String productId;
        // Constructor to initialize the product ID
        CheckIfProductIdExists(String productId)
        {
            this.productId = productId;
        }
        // Method to check if the product ID exists
        private Boolean checkIfProductIdExists() throws GenericEntityException {
            Delegator delegator = DelegatorFactory.getDelegator("default");
            // Query the database to find a product with the given product ID
            GenericValue productValue = EntityQuery.use(delegator).from("Product").where("productId", productId).queryOne();
            // Return true if the product exists, otherwise false
            if (productValue != null) {
                return true;
            }
            return false;
        }
    }
    // Nested class to update product
    public static class UpdateProductDetails{
        // Store the main product values in a map
        private Map<String,Object> mainProductValue = new HashMap<>();
        // Constructor to initialize main product values
        UpdateProductDetails(Map<String,Object> mainProductValue){
            this.mainProductValue = mainProductValue;
        }
        // Method to insert product details
        private void UpdateProductDetails()
        {
            try
            {
                // Get the Delegator instance
                Delegator delegator = DelegatorFactory.getDelegator("default");
                // Get the LocalDispatcher instance
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                // Get the UserLogin for system operations
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                // Prepare input parameters for updating the product
                Map<String, Object> inputParameters = new HashMap<>();

                inputParameters.put("productId", mainProductValue.get("partNumber"));
                inputParameters.put("productName", mainProductValue.get("productName"));
                inputParameters.put("internalName", mainProductValue.get("BrandAAIAID"));
                inputParameters.put("brandName", mainProductValue.get("BrandLabel"));
                inputParameters.put("productTypeId", "GOOD");
                inputParameters.put("releaseDate", mainProductValue.get("availableDate"));
                inputParameters.put("introductionDate", mainProductValue.get("ItemEffectiveDate"));
                inputParameters.put("quantityUomId", mainProductValue.get("itemQuantitySizeUOM"));
                inputParameters.put("quantityIncluded", mainProductValue.get("itemQuantitySize"));
                inputParameters.put("piecesIncluded", mainProductValue.get("quantityPerApplication"));
                inputParameters.put("configId", mainProductValue.get("acesApplications"));
                inputParameters.put("comments", mainProductValue.get("containerType"));
                inputParameters.put("description", mainProductValue.get("description"));
                inputParameters.put("longDescription", mainProductValue.get("longDescription"));
                inputParameters.put("autoCreateKeywords",mainProductValue.get("HazardousMaterialCode"));
                inputParameters.put("billOfMaterialLevel",mainProductValue.get("aaiapProductCategoryCode"));
                inputParameters.put("userLogin", permUserLogin);

                // Call the service to update the product
                Map<String, Object> result = dispatcher.runSync("updateProduct", inputParameters);

                // Check the result of the service call
                if (ServiceUtil.isSuccess(result)) {
                    System.out.println(result);
                } else {
                    System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(result));
                }

            }
            catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }

    // Nested class to update Product Attributes
    private static class UpdateProductAttributes
    {
        private List<Map<String,String>> productAttribute;
        private String productId;

        UpdateProductAttributes(List<Map<String,String>> productAttribute,String productId)
        {
            this.productAttribute = productAttribute;
            this.productId = productId;
        }
        // Method to update product attributes

        private void updateProductAttributes()
        {
            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                // prepare product attributes values
                for (Map<String,String> productAttributeMap : productAttribute) {
                    Map<String, Object> productAttributeInput = new HashMap<>();
                    productAttributeInput.put("productId", productId);
                    productAttributeInput.put("attrName", productAttributeMap.get("attributeID"));
                    productAttributeInput.put("attrValue", productAttributeMap.get("maintenanceTypeProductAttributes"));
                    productAttributeInput.put("attrType", productAttributeMap.get("padbAttribute"));
                    productAttributeInput.put("attrDescription", productAttributeMap.get("productAttributeValue"));
                    productAttributeInput.put("userLogin", permUserLogin);
                    // Call service to update product attributes
                    Map<String, Object> resultProductAttribute = dispatcher.runSync("updateProductAttribute", productAttributeInput);
                    if (ServiceUtil.isSuccess(resultProductAttribute)) {
                        // Process the result as needed
                        System.out.println(resultProductAttribute);
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductAttribute));
                    }
                }
            }catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }

    }
}

