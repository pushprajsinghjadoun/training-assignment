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
import org.apache.ofbiz.entity.condition.EntityCondition;
import org.apache.ofbiz.entity.condition.EntityOperator;
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
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ReadXmlData {

        public static String getNextSequenceId(Delegator delegator, String sequenceName) {
            synchronized (delegator) {
                String nextSeqId = delegator.getNextSeqId(sequenceName);
                return nextSeqId;
            }
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
            // List of Map to store partInterchange values
            List<Map<String,String>> partInterchangeValues = new ArrayList<>();
            // Map to store partInterchange values
            Map<String,String> partInterchangeValueMap = new HashMap<>();
            // List of Map to store product DigitalFileInformation
            List<Map<String, String>> productDigitalFileInformationList = new ArrayList<>();
            // Map to store DigitalFileInformation
            Map<String, String> productFileInformationMap = new HashMap<>();
            // Map to store Product category related data
            Map<String,Object> productCategoryMap = new HashMap<>();

            // Create an executor service with a fixed number of threads
            ExecutorService executor = Executors.newFixedThreadPool(8);

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
                                Map<String,String> productDescriptionValueMap = new HashMap<>();
                                if(descriptionCode.equals("DEF"))
                                {
                                    mainProductValue.put("productName",descriptionText);
                                    productDescriptionValueMap.put("description",descriptionText);
                                    productDescriptionValueMap.put("languageCode",languageCode);
                                    productDescriptionValueMap.put("maintenanceType",maintenanceType);
                                    productDescriptionValueMap.put("descriptionCode",descriptionCode);
                                    productDescription.add(productDescriptionValueMap);
                                }
                                else if(descriptionCode.equals("DES"))
                                {
                                    productDescriptionValueMap.put("description",descriptionText);
                                    productDescriptionValueMap.put("languageCode",languageCode);
                                    productDescriptionValueMap.put("maintenanceType",maintenanceType);
                                    productDescriptionValueMap.put("descriptionCode",descriptionCode);
                                    mainProductValue.put("description",descriptionText);
                                    productDescription.add(productDescriptionValueMap);
                                }else
                                {
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
                                productCategoryMap.put("aaiapProductCategoryCode",aaiapProductCategoryCode);
                                break;
                            case  "PartTerminologyID":
                                String partTerminologyID = reader.getElementText();
                                mainProductValue.put("partTerminologyID",partTerminologyID);
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
                            case  "Dimensions":
                                String dimensionsUOM = reader.getAttributeValue(null, "UOM");
                                mainProductValue.put("dimensionUomId",dimensionsUOM);
                                break;
                            case  "Height":
                                String height = reader.getElementText();
                                mainProductValue.put("boxHeight",height);
                                break;
                            case  "Width":
                                String width = reader.getElementText();
                                mainProductValue.put("boxWidth",width);
                                break;
                            case  "Length":
                                String length = reader.getElementText();
                                mainProductValue.put("boxLength",length);
                                break;
                            case  "Weights":
                                String weightsUOM = reader.getAttributeValue(null, "UOM");
                                mainProductValue.put("weightUomId",weightsUOM);
                                break;
                            case  "Weight":
                                String weight = reader.getElementText();
                                mainProductValue.put("weight",weight);
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
                                    productCategoryMap.put("BrandAAIAID",brandAAIAID);
                                }else{
                                    partInterchangeValueMap.put("brandAAIAID",brandAAIAID);
                                }

                                break;
                            case  "PartNumber":
                                String partNumber = reader.getElementText();

                                if(!isPartInterchange)
                                {
                                    mainProductValue.put("partNumber",partNumber);
                                    productCategoryMap.put("partNumber",partNumber);
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
                                // Create copies of the data before clearing the original data structures
                                Map<String, Object> mainProductValueCopy = new HashMap<>(mainProductValue);
                                List<Map<String,String>> productDescriptionCopy = new  ArrayList<Map<String, String>>(productDescription);
                                List<Map<String,String>> productExtendedInformationCopy = new ArrayList<Map<String, String>>(productExtendedInformation);
                                List<Map<String,String>> productAttributeCopy =  new ArrayList<Map<String, String>>(productAttribute);
                                String partNumberCopy = (String) mainProductValue.get("partNumber");
                                List<Map<String,String>> partInterchangeValuesCopy = new ArrayList<Map<String, String>>(partInterchangeValues);
                                List<Map<String,String>> productDigitalFileInformationListCopy = new ArrayList<Map<String, String>>(productDigitalFileInformationList);
                                Map<String, Object> productCategoryMapCopy = new HashMap<>(productCategoryMap);

                                // Create the task instance with the copied data and submit it to the executor
                                InsertXMLDataUsingMultiThread insertXMLDataUsingMultiThread = new InsertXMLDataUsingMultiThread(mainProductValueCopy, productDescriptionCopy, productExtendedInformationCopy, partNumberCopy, productAttributeCopy, partInterchangeValuesCopy, productDigitalFileInformationListCopy, productCategoryMapCopy);
                                executor.submit(insertXMLDataUsingMultiThread);

                                // Clear the original data structures
                                mainProductValue.clear();
                                productDescription.clear();
                                productAttribute.clear();

                                productExtendedInformation.clear();
                                productDigitalFileInformationList.clear();
                                partInterchangeValues.clear();
                                productCategoryMap.clear();

                                break;
                        }
                        break;
                }
            }

            // Shutdown the executor when tasks are no longer needed
            executor.shutdown();
        }
    }

    public static class InsertXMLDataUsingMultiThread implements Runnable
    {
        private Map<String,Object> mainProductValue;
        private List<Map<String,String>> productDescription;
        private List<Map<String,String>> productExtendedInformation;
        private String productId;
        private List<Map<String,String>> productAttribute;

        private List<Map<String,String>> partInterchangeValues;

        private List<Map<String,String>> productDigitalFileInformationList;
        private Map<String,Object> productCategoryMap;

        InsertXMLDataUsingMultiThread(Map<String,Object> mainProductValue,List<Map<String,String>> productDescription, List<Map<String,String>> productExtendedInformation, String productId,List<Map<String,String>> productAttribute,List<Map<String,String>> partInterchangeValues,List<Map<String,String>> productDigitalFileInformationList,Map<String,Object> productCategoryMap)
        {
            this.mainProductValue = mainProductValue;
            this.productDescription = productDescription;
            this.productExtendedInformation = productExtendedInformation;
            this.productId = productId;
            this.productAttribute = productAttribute;
            this.partInterchangeValues = partInterchangeValues;
            this.productDigitalFileInformationList = productDigitalFileInformationList;
            this.productCategoryMap =productCategoryMap;
        }
        public void run()
        {
            // Record the current time in milliseconds
            long startCountTime = System.currentTimeMillis();
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

                    // Create or update the product category (If product category is already exist then update the product category and if not then create a product category)
                    CreateProductCategory createProductCategory = new CreateProductCategory(productCategoryMap);
                    createProductCategory.createProductCategory();

                    // Update the product content like product description and product extended information
                    UpdateProductContent updateProductContent = new UpdateProductContent(productDescription,productExtendedInformation,(String) mainProductValue.get("partNumber"));
                    updateProductContent.UpdateProductContent();

                    // Update the digital file information
                    UpdateDigitalFileInformation updateDigitalFileInformation = new UpdateDigitalFileInformation(productDigitalFileInformationList,(String) mainProductValue.get("partNumber"));
                    updateDigitalFileInformation.updateDigitalInfo();

                    // Clear the map to release memory.
                    mainProductValue.clear();
                    productDescription.clear();
                    productAttribute.clear();
                    productExtendedInformation.clear();
                    productDigitalFileInformationList.clear();
                    partInterchangeValues.clear();
                    productAttribute.clear();
                    mainProductValue.clear();
                    System.out.println("Product Updated.");
                } else { // if product do not exist then insert data
                    // Create product category
                    CreateProductCategory createProductCategory = new CreateProductCategory(productCategoryMap);
                    createProductCategory.createProductCategory();
                    // Insert Product details
                    InsertProductDetails insertProductDetailsObject = new InsertProductDetails(mainProductValue);
                    insertProductDetailsObject.insertProductDetails();
                    // Insert Product Content details
                    InsertProductContent insertProductContentObject = new InsertProductContent(productDescription,productExtendedInformation, (String) mainProductValue.get("partNumber"));
                    insertProductContentObject.insertProductContent();
                    //Insert Product attributes details
                    InsertProductAttributes insertProductAttributes = new InsertProductAttributes(productAttribute,(String) mainProductValue.get("partNumber"));
                    insertProductAttributes.InsertProductAttributes();
                    // Insert PartInterChange Details
                    InsertPartInterchange insertPartInterchange = new InsertPartInterchange(partInterchangeValues,(String) mainProductValue.get("partNumber"));
                    insertPartInterchange.insertPartInterchange();
                    // Insert DigitalFileInformation
                    InsertDigitalFileInformation insertDigitalFileInformationObject = new InsertDigitalFileInformation(productDigitalFileInformationList,(String) mainProductValue.get("partNumber"));
                    insertDigitalFileInformationObject.insertDigitalInfo();
                    // Associate Product to a category
                    AddProductToCategory addProductToCategory = new AddProductToCategory((String) mainProductValue.get("partNumber"),(String) mainProductValue.get("aaiapProductCategoryCode"));
                    addProductToCategory.addProductToCategory();

                    // Clear List and Map to prepare for the next set of item data
                    mainProductValue.clear();
                    productDescription.clear();
                    productAttribute.clear();
                    productExtendedInformation.clear();
                    productDigitalFileInformationList.clear();
                    partInterchangeValues.clear();
                    productAttribute.clear();
                    mainProductValue.clear();
                }
            } catch (GenericEntityException e) {
                e.printStackTrace();
            }
            // Record the current time in milliseconds
            long endCountTime = System.currentTimeMillis();
            // Calculate and display the time taken for counting by the current thread
            System.out.println("Time taken for counting Thread : "+ Thread.currentThread().getId() + "----------" + (endCountTime - startCountTime) + " ms");
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
                // Prepare data for product entity
                inputParameters.put("productId", mainProductValue.get("partNumber"));
                inputParameters.put("productName", mainProductValue.get("productName"));
                inputParameters.put("internalName", mainProductValue.get("partTerminologyID"));
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
                inputParameters.put("autoCreateKeywords",mainProductValue.get("HazardousMaterialCode"));
                inputParameters.put("primaryProductCategoryId",mainProductValue.get("aaiapProductCategoryCode"));
                inputParameters.put("shippingHeight",mainProductValue.get("boxHeight"));
                inputParameters.put("shippingWidth",mainProductValue.get("boxWidth"));
                inputParameters.put("shippingDepth",mainProductValue.get("boxLength"));
                inputParameters.put("shippingWeight",mainProductValue.get("weight"));
                inputParameters.put("heightUomId",mainProductValue.get("dimensionUomId"));
                inputParameters.put("widthUomId",mainProductValue.get("dimensionUomId"));
                inputParameters.put("depthUomId",mainProductValue.get("dimensionUomId"));
                inputParameters.put("weightUomId",mainProductValue.get("weightUomId"));
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
                    Map<String,Object> productDataResource = new HashMap<>();
                    // Prepare content description values to insert
                    String contentId = getNextSequenceId(delegator, "ContentSeqId");
                    descriptionInput.put("contentId", contentId);
                    descriptionInput.put("contentName", descriptionMap.get("descriptionCode"));
                    descriptionInput.put("localeString", descriptionMap.get("languageCode"));
                    descriptionInput.put("serviceName", descriptionMap.get("maintenanceType"));
                    descriptionInput.put("userLogin", permUserLogin);

                    // insert product description in a ElectronicText entity
                    // In productDataResource map, stored value which will be passed to createElectronicText service.
                    productDataResource.put("textData",descriptionMap.get("description"));
                    productDataResource.put("userLogin", permUserLogin);
                    // calling createElectronicText service
                    Map<String, Object> resultDataResource = dispatcher.runSync("createElectronicText", productDataResource);
                    if (ServiceUtil.isSuccess(resultDataResource)) {
                        // Process the result as needed
                        descriptionInput.put("dataResourceId",resultDataResource.get("dataResourceId"));
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
                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDataResource));
                    }

                }
                // Loop through each product extended information and insert content
                for ( Map<String,String> productExtendedInformationMap: productExtendedInformation) {
                    Map<String, Object> productExtendedInformationInput = new HashMap<>();
                    Map<String, Object> productContentInput = new HashMap<>();
                    Map<String, Object> productDataResource = new HashMap<>();

                    String contentId = getNextSequenceId(delegator, "ContentSeqId");
                    // Prepare product extended information  values to insert
                    productExtendedInformationInput.put("contentId", contentId);
                    productExtendedInformationInput.put("contentName", productExtendedInformationMap.get("expiCode"));
                    productExtendedInformationInput.put("localeString", productExtendedInformationMap.get("extendedProductLanguageCode"));
                    productExtendedInformationInput.put("serviceName", productExtendedInformationMap.get("maintenanceTypeExtendedProduct"));
                    productExtendedInformationInput.put("userLogin", permUserLogin);

                    productDataResource.put("textData",productExtendedInformationMap.get("extendedProductInformationValue"));
                    productDataResource.put("userLogin", permUserLogin);

                    Map<String, Object> resultDataResource = dispatcher.runSync("createElectronicText", productDataResource);
                    if (ServiceUtil.isSuccess(resultDataResource)) {
                        productExtendedInformationInput.put("dataResourceId",resultDataResource.get("dataResourceId"));
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
                        productContentInput.put("productContentTypeId", "PRODUCT_EXTENDED_IN");
                        productContentInput.put("userLogin", permUserLogin);
                        // Call service to create product content
                        Map<String, Object> resultProductContent = dispatcher.runSync("createProductContent", productContentInput);
                        if (ServiceUtil.isSuccess(resultProductContent)) {
                            // Process the result as needed
                            System.out.println(resultProductContent);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductContent));
                        }
                    }else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDataResource));
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
                Map<String, Object> productContentInput = new HashMap<>();
                Map<String, Object> productDataResource = new HashMap<>();
                // Prepare digital file information
                for ( Map<String, String> digitalValue : digitalValues) {
                    String contentId = getNextSequenceId(delegator, "ContentSeqId");

                    //productDataResource Map  stored value in a map so that this values passed to createElectronicText service
                    productDataResource.put("userLogin", permUserLogin);
                    productDataResource.put("objectInfo",digitalValue.get("uri"));
                    productDataResource.put("localeString",digitalValue.get("filePath"));
                    // productContentInput Map to stored value in a map so that this values passed to createContent
                    productContentInput.put("userLogin", permUserLogin);
                    productContentInput.put("contentId",contentId);
                    productContentInput.put("contentName",digitalValue.get("assetType"));
                    productContentInput.put("serviceName",digitalValue.get("maintenanceTypeDigital"));
                    productContentInput.put("localeString",digitalValue.get("fileType"));
                    productContentInput.put("description",digitalValue.get("FileName"));

                    // Call the service createElectronicText to insert values in dataResource
                    Map<String, Object> resultProductDataResource= dispatcher.runSync("createElectronicText", productDataResource);
                    if (ServiceUtil.isSuccess(resultProductDataResource)) {
                        System.out.println(resultProductDataResource);
                        // Map's to hold the Digital File information data
                        Map<String, Object> digitalInfoInputRepresentation = new HashMap<>();
                        Map<String, Object> digitalInfoInputBackground= new HashMap<>();
                        Map<String, Object> digitalInfoInputLanguageCode= new HashMap<>();
                        Map<String, Object> digitalInfoInputResolution= new HashMap<>();
                        Map<String, Object> digitalInfoInputFileSize= new HashMap<>();
                        Map<String, Object> digitalInfoInputAssetHeight= new HashMap<>();
                        Map<String, Object> digitalInfoInputAssetWeight= new HashMap<>();
                        Map<String, Object> digitalInfoInputAssetID= new HashMap<>();
                        // Get dataResourceId when createElectronicText service successfully insert data in a entity.
                        String dataResourceId = (String) resultProductDataResource.get("dataResourceId");
                        // Insert dataResourceId in a productContentInput
                        productContentInput.put("dataResourceId",dataResourceId);
                        // Store Representation data which will be store in DataResourceAttribute entity
                        digitalInfoInputRepresentation.put("userLogin", permUserLogin);
                        digitalInfoInputRepresentation.put("dataResourceId",dataResourceId);
                        digitalInfoInputRepresentation.put("attrName","Representation");
                        digitalInfoInputRepresentation.put("attrValue",digitalValue.get("representation"));
                        // Store Background data which will be store in DataResourceAttribute entity
                        digitalInfoInputBackground.put("userLogin", permUserLogin);
                        digitalInfoInputBackground.put("dataResourceId",dataResourceId);
                        digitalInfoInputBackground.put("attrName","Background");
                        digitalInfoInputBackground.put("attrValue",digitalValue.get("background"));
                        // Store AssetID data which will be store in DataResourceAttribute entity
                        digitalInfoInputAssetID.put("userLogin", permUserLogin);
                        digitalInfoInputAssetID.put("dataResourceId",dataResourceId);
                        digitalInfoInputAssetID.put("attrName","Asset ID");
                        digitalInfoInputAssetID.put("attrValue",digitalValue.get("assetID"));
                        // Store Resolution data which will be store in DataResourceAttribute entity
                        digitalInfoInputLanguageCode.put("userLogin", permUserLogin);
                        digitalInfoInputLanguageCode.put("dataResourceId",dataResourceId);
                        digitalInfoInputLanguageCode.put("attrValue",digitalValue.get("languageCodeDigital"));
                        digitalInfoInputLanguageCode.put("attrName","Language Code");

                        // Store Resolution data which will be store in DataResourceAttribute entity
                        digitalInfoInputResolution.put("userLogin", permUserLogin);
                        digitalInfoInputResolution.put("dataResourceId",dataResourceId);
                        digitalInfoInputResolution.put("attrValue",digitalValue.get("resolution"));
                        digitalInfoInputResolution.put("attrName","Resolution");

                        // Store FileSize data which will be store in DataResourceAttribute entity
                        digitalInfoInputFileSize.put("userLogin", permUserLogin);
                        digitalInfoInputFileSize.put("dataResourceId",dataResourceId);
                        digitalInfoInputFileSize.put("attrValue",digitalValue.get("fileSize"));
                        digitalInfoInputFileSize.put("attrName","File Size");
                        // Store Asset Height data which will be store in DataResourceAttribute entity
                        digitalInfoInputAssetHeight.put("userLogin", permUserLogin);
                        digitalInfoInputAssetHeight.put("dataResourceId",dataResourceId);
                        digitalInfoInputAssetHeight.put("attrValue",digitalValue.get("assetHeight"));
                        digitalInfoInputAssetHeight.put("attrName","Asset Height");
                        // Store Asset Width data which will be store in DataResourceAttribute entity
                        digitalInfoInputAssetWeight.put("userLogin", permUserLogin);
                        digitalInfoInputAssetWeight.put("dataResourceId",dataResourceId);
                        digitalInfoInputAssetWeight.put("attrValue",digitalValue.get("assetWidth"));
                        digitalInfoInputAssetWeight.put("attrName","Asset Weight");

                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultDigitalAssetHeight = dispatcher.runSync("createDataResourceAttribute",digitalInfoInputAssetHeight);
                        if (ServiceUtil.isSuccess(resultDigitalAssetHeight)) {
                            // Process the result as needed
                            System.out.println(resultDigitalAssetHeight);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalAssetHeight));
                        }
                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultDigitalAssetWeight = dispatcher.runSync("createDataResourceAttribute",digitalInfoInputAssetWeight);
                        if (ServiceUtil.isSuccess(resultDigitalAssetWeight)) {
                            // Process the result as needed
                            System.out.println(resultDigitalAssetWeight);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalAssetWeight));
                        }

                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultDigitalFileSize = dispatcher.runSync("createDataResourceAttribute",digitalInfoInputFileSize);
                        if (ServiceUtil.isSuccess(resultDigitalFileSize)) {
                            // Process the result as needed
                            System.out.println(resultDigitalFileSize);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalFileSize));
                        }

                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultDigitalResolution = dispatcher.runSync("createDataResourceAttribute",digitalInfoInputResolution);
                        if (ServiceUtil.isSuccess(resultDigitalResolution)) {
                            // Process the result as needed
                            System.out.println(resultDigitalResolution);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalResolution));
                        }
                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultDigitalLanguageCode = dispatcher.runSync("createDataResourceAttribute",digitalInfoInputLanguageCode);
                        if (ServiceUtil.isSuccess(resultDigitalLanguageCode)) {
                            // Process the result as needed
                            System.out.println(resultDigitalLanguageCode);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalLanguageCode));
                        }
                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultDigitalBackground = dispatcher.runSync("createDataResourceAttribute",digitalInfoInputBackground);
                        if (ServiceUtil.isSuccess(resultDigitalBackground)) {
                            // Process the result as needed
                            System.out.println(resultDigitalBackground);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalBackground));
                        }
                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultDigitalRepresentation = dispatcher.runSync("createDataResourceAttribute",digitalInfoInputRepresentation);
                        if (ServiceUtil.isSuccess(resultDigitalRepresentation)) {
                            // Process the result as needed
                            System.out.println(resultDigitalRepresentation);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalRepresentation));
                        }
                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultDigitalAssetId = dispatcher.runSync("createDataResourceAttribute", digitalInfoInputAssetID);
                        if (ServiceUtil.isSuccess(resultDigitalAssetId)) {
                            System.out.println(resultDigitalAssetId);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultDigitalAssetId));
                        }
                        // call the service to create the new Data Resource attributes
                        Map<String, Object> resultProductContentInput = dispatcher.runSync("createContent", productContentInput);
                        if (ServiceUtil.isSuccess(resultProductContentInput)) {
                            System.out.println(resultProductContentInput);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductDataResource));
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

                    } else {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultProductDataResource));
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
        // Method to update product details
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
                // inputParameters map to store the product related values
                inputParameters.put("productId", mainProductValue.get("partNumber"));
                inputParameters.put("productName", mainProductValue.get("productName"));
                inputParameters.put("internalName",mainProductValue.get("partTerminologyID"));
                inputParameters.put("brandName", mainProductValue.get("BrandLabel"));
                inputParameters.put("productTypeId", "GOOD");
                inputParameters.put("releaseDate", mainProductValue.get("availableDate"));
                inputParameters.put("introductionDate", mainProductValue.get("ItemEffectiveDate"));
                inputParameters.put("quantityUomId", mainProductValue.get("itemQuantitySizeUOM"));
                inputParameters.put("quantityIncluded", mainProductValue.get("itemQuantitySize"));
                inputParameters.put("piecesIncluded", mainProductValue.get("quantityPerApplication"));
                inputParameters.put("configId", mainProductValue.get("acesApplications"));
                inputParameters.put("comments", mainProductValue.get("containerType"));
                inputParameters.put("autoCreateKeywords",mainProductValue.get("HazardousMaterialCode"));
                inputParameters.put("description", mainProductValue.get("description"));
                inputParameters.put("primaryProductCategoryId",mainProductValue.get("aaiapProductCategoryCode"));
                inputParameters.put("shippingHeight",mainProductValue.get("boxHeight"));
                inputParameters.put("shippingWidth",mainProductValue.get("boxWidth"));
                inputParameters.put("shippingDepth",mainProductValue.get("boxLength"));
                inputParameters.put("shippingWeight",mainProductValue.get("weight"));
                inputParameters.put("heightUomId",mainProductValue.get("dimensionUomId"));
                inputParameters.put("widthUomId",mainProductValue.get("dimensionUomId"));
                inputParameters.put("depthUomId",mainProductValue.get("dimensionUomId"));
                inputParameters.put("weightUomId",mainProductValue.get("weightUomId"));
                inputParameters.put("userLogin", permUserLogin);

                // Call the service to update the product
                Map<String, Object> result = dispatcher.runSync("updateProduct", inputParameters);

                // Check the result of the service call
                if (ServiceUtil.isSuccess(result)) {
                    System.out.println(result);
                } else {
                    System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(result));
                }
                Map<String, Object> goodIdentificationInputParameters = new HashMap<>();
                // prepare good identification values to update
                goodIdentificationInputParameters.put("productId", mainProductValue.get("partNumber"));
                goodIdentificationInputParameters.put("goodIdentificationTypeId","OTHER_ID");
                goodIdentificationInputParameters.put("idValue",mainProductValue.get("ItemLevelGTIN"));
                goodIdentificationInputParameters.put("userLogin", permUserLogin);

                // Call the service to update good identification
                Map<String, Object> goodIdentificationResult = dispatcher.runSync("updateGoodIdentification", goodIdentificationInputParameters);

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
                    EntityCondition conditionToGetProductAttributesWithProductId = EntityCondition.makeCondition(
                            EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId),
                            EntityOperator.AND,
                            EntityCondition.makeCondition("attrName", EntityOperator.EQUALS, productAttributeMap.get("attributeID"))
                    );
                    // Query ProductAttribute entities to retrieve values based on the given condition
                    GenericValue productAttributeValueForEachProductAttributeMap  = EntityQuery.use(delegator).from("ProductAttribute").where(conditionToGetProductAttributesWithProductId).queryOne();
                    // Check if productAttribute already exist, if exist then update the value of product attribute.
                    if(productAttributeValueForEachProductAttributeMap!=null)
                    {
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
                    }else // If product Attributes does not exist then create new product attribute
                    {
                        // List to store productAttributeMap
                        List<Map<String,String>> productAttributesList = new ArrayList<>();
                        productAttributesList.add(productAttributeMap);
                        // Insert the product attributes values via creating object of InsertProductAttributes and calling method
                        InsertProductAttributes insertProductAttributes = new InsertProductAttributes(productAttributesList,productId);
                        insertProductAttributes.InsertProductAttributes();
                    }

                }
            }catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }

    }

    // Nested class to insert product content
    public static class UpdateProductContent{
        // Lists to store product descriptions, extended information, and attributes
        private List<Map<String,String>> productDescription;
        private List<Map<String,String>> productExtendedInformation;
        private String productId;

        // Constructor to initialize product content data and product ID
        UpdateProductContent(List<Map<String,String>> productDescription, List<Map<String,String>> productExtendedInformation,String productId)
        {
            this.productDescription = productDescription;
            this.productExtendedInformation = productExtendedInformation;
            this.productId = productId;
        }

        // Method to insert product content
        private void UpdateProductContent()
        {
            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                Map<String, Object> descriptionInput = new HashMap<>();

                // Condition to find the content id in which product id is equal to item product id and productContentTypeId is equal to DESCRIPTION
                EntityCondition conditionToFindContentIdForDescription = EntityCondition.makeCondition(
                        EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId),
                        EntityOperator.AND,
                        EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "DESCRIPTION")
                );
                // Query ProductContent entities to retrieve contentIds based on the given condition
                List<GenericValue> contentIds = EntityQuery.use(delegator).from("ProductContent").where(conditionToFindContentIdForDescription).select("contentId").queryList();
                // List to store the contentId's
                List<String> contentIdList = new ArrayList<>();
                Map<String,String> descriptionCodeAndContentId = new HashMap<>();
                Map<String,String> descriptionCodeAndDataSourceId = new HashMap<>();
                // Traverse to each content Id
                for (GenericValue eachContentId : contentIds)
                {
                    contentIdList.add((String) eachContentId.get("contentId"));
                }
                // Create a condition to find data resources with matching contentId and contentName
                EntityCondition conditionToFindDataResourceFormContentId = EntityCondition.makeCondition(
                        EntityCondition.makeCondition("contentId", EntityOperator.IN, contentIdList)
                );
                // Query for a specific content using the condition
                List<GenericValue> contentValues = EntityQuery.use(delegator).from("Content").where(conditionToFindDataResourceFormContentId).queryList();

                for(GenericValue contentValue: contentValues)
                {
                    descriptionCodeAndContentId.put((String) contentValue.get("contentName"),(String) contentValue.get("contentId"));
                    descriptionCodeAndDataSourceId.put((String) contentValue.get("contentName"),(String) contentValue.get("dataResourceId"));
                }
                // Loop through each description and insert content
                for (Map<String,String> descriptionMap : productDescription) {
                    // Prepare content description values to update
                    descriptionInput.put("contentName", descriptionMap.get("descriptionCode"));
                    descriptionInput.put("localeString", descriptionMap.get("languageCode"));
                    descriptionInput.put("serviceName", descriptionMap.get("maintenanceType"));
                    descriptionInput.put("userLogin", permUserLogin);
                    // If product description exist then update the product description
                    if (descriptionCodeAndContentId.get(descriptionMap.get("descriptionCode")) != null && descriptionCodeAndDataSourceId.get(descriptionMap.get("descriptionCode"))!=null) {
                        // store contentId and descriptionCode in a map
                        descriptionInput.put("contentId",descriptionCodeAndContentId.get(descriptionMap.get("descriptionCode")));
                        // Update the product description content
                        Map<String, Object> resultUpdateProductDescriptionContent = dispatcher.runSync("updateContent", descriptionInput);
                        if (ServiceUtil.isSuccess(resultUpdateProductDescriptionContent)) {
                            // Process the result as needed
                            System.out.println(resultUpdateProductDescriptionContent);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateProductDescriptionContent));
                        }
                        // Prepare and update electronic text input
                        Map<String,Object> updateElectronicTextInput = new HashMap<>();
                        updateElectronicTextInput.put("userLogin", permUserLogin);
                        updateElectronicTextInput.put("textData",descriptionMap.get("description"));
                        updateElectronicTextInput.put("dataResourceId",descriptionCodeAndDataSourceId.get(descriptionMap.get("descriptionCode")));
                        // Update the electronic text in which product description stored.
                        Map<String, Object> resultUpdateProductDescription = dispatcher.runSync("updateElectronicText", updateElectronicTextInput);
                        if (ServiceUtil.isSuccess(resultUpdateProductDescription)) {
                            // Process the result as needed
                            System.out.println(resultUpdateProductDescription);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateProductDescription));
                        }
                    }else // If product description does not exist then update it.
                    {
                        // List to store the value of product description in list
                        List<Map<String,String>> productDescriptionList = new ArrayList<>();
                        List<Map<String,String>> productExtendedList = new ArrayList<>();
                        // InsertProductContent object to insert product description
                        InsertProductContent insertProductContent = new InsertProductContent(productDescriptionList,productExtendedList,productId);
                        insertProductContent.insertProductContent();
                    }

                }

                // Create a condition to find ProductContent entities with the given productId and productContentTypeId
                EntityCondition conditionToFindContentIdForExtendedProductInformation = EntityCondition.makeCondition(
                        EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId),
                        EntityOperator.AND,
                        EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "PRODUCT_EXTENDED_IN")
                );
                // Query ProductContent entities to retrieve contentIds based on the given condition
                List<GenericValue> contentIdsForExtendedProductInformation = EntityQuery.use(delegator).from("ProductContent").where(conditionToFindContentIdForExtendedProductInformation).select("contentId").queryList();
                // List to store the contentId's
                List<String> contentIdListForExtendedProductInformation = new ArrayList<>();
                Map<String,String> extendedProductInformationEXPICodeAndContentId = new HashMap<>();
                Map<String,String> extendedProductInformationEXPICodeAndDataSourceId = new HashMap<>();
                // Traverse to each content Id
                for (GenericValue eachContentId : contentIdsForExtendedProductInformation)
                {
                    contentIdListForExtendedProductInformation.add((String) eachContentId.get("contentId"));
                }
                // Create a condition to find data resources with matching contentId and contentName
                EntityCondition conditionToFindDataResourceFromContentId = EntityCondition.makeCondition(
                        EntityCondition.makeCondition("contentId", EntityOperator.IN, contentIdListForExtendedProductInformation)
                );
                // Query for a specific content using the condition
                List<GenericValue> extendedProductInformationContentValues = EntityQuery.use(delegator).from("Content").where(conditionToFindDataResourceFromContentId).queryList();
                // Traverse to each content value in a extendedProductInformationContentValues
                for(GenericValue contentValue: extendedProductInformationContentValues)
                {
                    // Put contentId and dataResourceId with key contentName
                    extendedProductInformationEXPICodeAndContentId.put((String) contentValue.get("contentName"),(String) contentValue.get("contentId"));
                    extendedProductInformationEXPICodeAndDataSourceId.put((String) contentValue.get("contentName"),(String) contentValue.get("dataResourceId"));
                }

                // Loop through each product extended information and update content
                for ( Map<String,String> productExtendedInformationMap: productExtendedInformation) {
                    Map<String, Object> productExtendedInformationInput = new HashMap<>();

                    // Prepare product extended information  values to insert
                    productExtendedInformationInput.put("contentName", productExtendedInformationMap.get("expiCode"));
                    productExtendedInformationInput.put("localeString", productExtendedInformationMap.get("extendedProductLanguageCode"));
                    productExtendedInformationInput.put("serviceName", productExtendedInformationMap.get("maintenanceTypeExtendedProduct"));
                    productExtendedInformationInput.put("userLogin", permUserLogin);
                    // if product extended information exist then update the value
                    if ( extendedProductInformationEXPICodeAndContentId.get(productExtendedInformationMap.get("expiCode"))!= null  && extendedProductInformationEXPICodeAndDataSourceId.get(productExtendedInformationMap.get("expiCode"))!=null) {
                        // Put contentId in a productExtendedInformationInput map
                        productExtendedInformationInput.put("contentId",extendedProductInformationEXPICodeAndContentId.get(productExtendedInformationMap.get("expiCode")));
                        // Update the extended product content
                        Map<String, Object> resultUpdateProductExtendedContent = dispatcher.runSync("updateContent", productExtendedInformationInput);
                        if (ServiceUtil.isSuccess(resultUpdateProductExtendedContent)) {
                            // Process the result as needed
                            System.out.println(resultUpdateProductExtendedContent);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateProductExtendedContent));
                        }
                        // Prepare and update electronic text input
                        Map<String,Object> updateElectronicTextInput = new HashMap<>();
                        updateElectronicTextInput.put("userLogin", permUserLogin);
                        updateElectronicTextInput.put("textData",productExtendedInformationMap.get("extendedProductInformationValue"));
                        updateElectronicTextInput.put("dataResourceId",extendedProductInformationEXPICodeAndDataSourceId.get(productExtendedInformationMap.get("expiCode")));

                        // Update the electronic text
                        Map<String, Object> resultUpdateProductExtendedInfoDescription = dispatcher.runSync("updateElectronicText", updateElectronicTextInput);
                        if (ServiceUtil.isSuccess(resultUpdateProductExtendedInfoDescription)) {
                            // Process the result as needed
                            System.out.println(resultUpdateProductExtendedInfoDescription);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateProductExtendedInfoDescription));
                        }

                    }else // If product extended information does not exist then add new product extended information.
                    {
                        // List to store product description value
                        List<Map<String,String>> productDescriptionList = new ArrayList<>();
                        List<Map<String,String>> productExtendedList = new ArrayList<>();
                        // Add productExtendedInformationMap in a List
                        productExtendedList.add(productExtendedInformationMap);
                        // Create InsertProductContent object to insert product extended information
                        InsertProductContent insertProductContent = new InsertProductContent(productDescriptionList,productExtendedList,productId);
                        insertProductContent.insertProductContent();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }

    // A private class to update product digital file information
    private static class UpdateDigitalFileInformation
    {
        // Lists to store digital file information and product ID
        private List<Map<String,String>> digitalValues;
        private String productId;
        // Constructor to initialize digital file information and product ID
        UpdateDigitalFileInformation(List<Map<String,String>> digitalValues, String productId)
        {
            this.digitalValues = digitalValues;
            this.productId = productId;
        }
        // Method to insert digital file information
        private void updateDigitalInfo()
        {

            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();

                // Map's to hold the Digital File information data for content entity
                Map<String, Object> productContentInput = new HashMap<>();
                // Create a condition to find ProductContent entities with the given productId and productContentTypeId
                EntityCondition conditionToFindContentIdForDigitalFileInformation = EntityCondition.makeCondition(
                        EntityCondition.makeCondition("productId", EntityOperator.EQUALS, productId),
                        EntityOperator.AND,
                        EntityCondition.makeCondition("productContentTypeId", EntityOperator.EQUALS, "DIGITAL_DOWNLOAD")
                );
                // Query ProductContent entities to retrieve contentIds based on the given condition
                List<GenericValue> digitalFileInformationContentIds = EntityQuery.use(delegator).from("ProductContent").where(conditionToFindContentIdForDigitalFileInformation).select("contentId").distinct().queryList();
                // List to store contentId
                List<String> digitalIdList = new ArrayList<>();
                for (GenericValue digitalFileInformationContentId : digitalFileInformationContentIds)
                {
                    digitalIdList.add((String) digitalFileInformationContentId.get("contentId"));
                }
                // Create a condition to find file name and data resourceId in a content entities.
                EntityCondition conditionToFindDigitalFileInformationFormContentId = EntityCondition.makeCondition(
                        EntityCondition.makeCondition("contentId", EntityOperator.IN, digitalIdList)
                );
                // Query Content entities to retrieve contentId and dataResourceId based on the given condition
                List<GenericValue> contentValueList = EntityQuery.use(delegator).from("Content").where(conditionToFindDigitalFileInformationFormContentId).queryList();
                // Map to store contentId and a dataResourceId as a value and file name as a key.
                Map<String,String> mapToStoreContentIdAndFileName = new HashMap<>();
                Map<String,String> mapToStoreContentIdAndDataResourceId = new HashMap<>();
                // Traverse contentId list
                for(GenericValue contentValue : contentValueList)
                {
                    // Store value in a map
                    mapToStoreContentIdAndFileName.put((String) contentValue.get("description"), (String) contentValue.get("contentId"));
                    mapToStoreContentIdAndDataResourceId.put((String) contentValue.get("description"), (String) contentValue.get("dataResourceId"));
                }
                // Traverse Digital file information related values
                for ( Map<String, String> digitalValue : digitalValues)
                {
                    // If contentId and dataResourceId found with file name then simply update the value
                    if(mapToStoreContentIdAndFileName.get(digitalValue.get("FileName"))!=null && mapToStoreContentIdAndDataResourceId.get(digitalValue.get("FileName"))!=null)
                    {
                        // Prepare input for updating product content
                        productContentInput.put("userLogin", permUserLogin);
                        productContentInput.put("contentId",mapToStoreContentIdAndFileName.get(digitalValue.get("FileName")));
                        productContentInput.put("contentName",digitalValue.get("assetType"));
                        productContentInput.put("serviceName",digitalValue.get("maintenanceTypeDigital"));
                        productContentInput.put("localeString",digitalValue.get("fileType"));
                        productContentInput.put("description",digitalValue.get("FileName"));
                        // Prepare input for updating data resource
                        Map<String, Object> digitalFileDataResourceInput = new HashMap<>();
                        digitalFileDataResourceInput.put("userLogin", permUserLogin);
                        digitalFileDataResourceInput.put("objectInfo",digitalValue.get("uri"));
                        digitalFileDataResourceInput.put("localeString",digitalValue.get("filePath"));
                        digitalFileDataResourceInput.put("dataResourceId",mapToStoreContentIdAndDataResourceId.get(digitalValue.get("FileName")));
                        // Update the product content
                        Map<String, Object> resultUpdateDigitalFileInformationContent = dispatcher.runSync("updateContent", productContentInput);
                        if (ServiceUtil.isSuccess(resultUpdateDigitalFileInformationContent)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDigitalFileInformationContent);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDigitalFileInformationContent));
                        }
                        // Update the data resource
                        Map<String, Object> resultUpdateDataResource = dispatcher.runSync("updateDataResource", digitalFileDataResourceInput);
                        if (ServiceUtil.isSuccess(resultUpdateDataResource)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResource);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResource));
                        }
                        // Map to store the values which will be inserted in a ProductAttribute Entity.
                        Map<String, Object> digitalInfoInputRepresentation = new HashMap<>();
                        Map<String, Object> digitalInfoInputBackground= new HashMap<>();
                        Map<String, Object> digitalInfoInputLanguageCode= new HashMap<>();
                        Map<String, Object> digitalInfoInputResolution= new HashMap<>();
                        Map<String, Object> digitalInfoInputFileSize= new HashMap<>();
                        Map<String, Object> digitalInfoInputAssetHeight= new HashMap<>();
                        Map<String, Object> digitalInfoInputAssetWeight= new HashMap<>();
                        Map<String, Object> digitalInfoInputAssetID= new HashMap<>();
                        // dataResourceId from exactContentId.
                        String dataResourceId = (String) mapToStoreContentIdAndDataResourceId.get(digitalValue.get("FileName"));
                        // Store Representation data which will be store in DataResourceAttribute entity
                        digitalInfoInputRepresentation.put("userLogin", permUserLogin);
                        digitalInfoInputRepresentation.put("dataResourceId",dataResourceId);
                        digitalInfoInputRepresentation.put("attrName","Representation");
                        digitalInfoInputRepresentation.put("attrValue",digitalValue.get("representation"));
                        // Store Background data which will be store in DataResourceAttribute entity
                        digitalInfoInputBackground.put("userLogin", permUserLogin);
                        digitalInfoInputBackground.put("dataResourceId",dataResourceId);
                        digitalInfoInputBackground.put("attrName","Background");
                        digitalInfoInputBackground.put("attrValue",digitalValue.get("background"));
                        // Store AssetID data which will be store in DataResourceAttribute entity
                        digitalInfoInputAssetID.put("userLogin", permUserLogin);
                        digitalInfoInputAssetID.put("dataResourceId",dataResourceId);
                        digitalInfoInputAssetID.put("attrName","Asset ID");
                        digitalInfoInputAssetID.put("attrValue",digitalValue.get("assetID"));
                        // Store LanguageCode data which will be store in DataResourceAttribute entity
                        digitalInfoInputLanguageCode.put("userLogin", permUserLogin);
                        digitalInfoInputLanguageCode.put("dataResourceId",dataResourceId);
                        digitalInfoInputLanguageCode.put("attrValue",digitalValue.get("languageCodeDigital"));
                        digitalInfoInputLanguageCode.put("attrName","Language Code");

                        // Store Resolution data which will be store in DataResourceAttribute entity
                        digitalInfoInputResolution.put("userLogin", permUserLogin);
                        digitalInfoInputResolution.put("dataResourceId",dataResourceId);
                        digitalInfoInputResolution.put("attrValue",digitalValue.get("resolution"));
                        digitalInfoInputResolution.put("attrName","Resolution");

                        // Store FileSize data which will be store in DataResourceAttribute entity
                        digitalInfoInputFileSize.put("userLogin", permUserLogin);
                        digitalInfoInputFileSize.put("dataResourceId",dataResourceId);
                        digitalInfoInputFileSize.put("attrValue",digitalValue.get("fileSize"));
                        digitalInfoInputFileSize.put("attrName","File Size");
                        // Store AssetHeight data which will be store in DataResourceAttribute entity
                        digitalInfoInputAssetHeight.put("userLogin", permUserLogin);
                        digitalInfoInputAssetHeight.put("dataResourceId",dataResourceId);
                        digitalInfoInputAssetHeight.put("attrValue",digitalValue.get("assetHeight"));
                        digitalInfoInputAssetHeight.put("attrName","Asset Height");
                        // Store Asset Width data which will be store in DataResourceAttribute entity
                        digitalInfoInputAssetWeight.put("userLogin", permUserLogin);
                        digitalInfoInputAssetWeight.put("dataResourceId",dataResourceId);
                        digitalInfoInputAssetWeight.put("attrValue",digitalValue.get("assetWidth"));
                        digitalInfoInputAssetWeight.put("attrName","Asset Weight");
                        // Update the attribute related to asset weight using the updateDataResourceAttribute service
                        Map<String, Object> resultUpdateDataResourceAttributeWeight = dispatcher.runSync("updateDataResourceAttribute", digitalInfoInputAssetWeight);
                        if (ServiceUtil.isSuccess(resultUpdateDataResourceAttributeWeight)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResourceAttributeWeight);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResourceAttributeWeight));
                        }
                        // Update the attribute related to asset height using the updateDataResourceAttribute service
                        Map<String, Object> resultUpdateDataResourceAttributeHeight = dispatcher.runSync("updateDataResourceAttribute", digitalInfoInputAssetHeight);
                        if (ServiceUtil.isSuccess(resultUpdateDataResourceAttributeHeight)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResourceAttributeHeight);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResourceAttributeHeight));
                        }
                        // Update the attribute related to asset file size using the updateDataResourceAttribute service
                        Map<String, Object> resultUpdateDataResourceAttributeFileSize = dispatcher.runSync("updateDataResourceAttribute", digitalInfoInputFileSize);
                        if (ServiceUtil.isSuccess(resultUpdateDataResourceAttributeFileSize)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResourceAttributeFileSize);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResourceAttributeFileSize));
                        }
                        // Update the attribute related to Resolution using the updateDataResourceAttribute service
                        Map<String, Object> resultUpdateDataResourceAttributeResolution = dispatcher.runSync("updateDataResourceAttribute", digitalInfoInputResolution);
                        if (ServiceUtil.isSuccess(resultUpdateDataResourceAttributeResolution)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResourceAttributeResolution);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResourceAttributeResolution));
                        }
                        // Update the attribute related to language code using the updateDataResourceAttribute service
                        Map<String, Object> resultUpdateDataResourceAttributeLanguageCode = dispatcher.runSync("updateDataResourceAttribute", digitalInfoInputLanguageCode);
                        if (ServiceUtil.isSuccess(resultUpdateDataResourceAttributeLanguageCode)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResourceAttributeLanguageCode);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResourceAttributeLanguageCode));
                        }
                        // Update the attribute related to AssetID using the updateDataResourceAttribute service
                        Map<String, Object> resultUpdateDataResourceAttributeAssetID = dispatcher.runSync("updateDataResourceAttribute", digitalInfoInputAssetID);
                        if (ServiceUtil.isSuccess(resultUpdateDataResourceAttributeAssetID)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResourceAttributeAssetID);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResourceAttributeAssetID));
                        }
                        // Update the attribute related to Background using the updateDataResourceAttribute service
                        Map<String, Object> resultUpdateDataResourceBackground = dispatcher.runSync("updateDataResourceAttribute", digitalInfoInputBackground);
                        if (ServiceUtil.isSuccess(resultUpdateDataResourceBackground)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResourceBackground);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResourceBackground));
                        }
                        // Update the attribute related to Representation using the updateDataResourceAttribute service
                        Map<String, Object> resultUpdateDataResourceAttributeRepresentation = dispatcher.runSync("updateDataResourceAttribute", digitalInfoInputRepresentation);
                        if (ServiceUtil.isSuccess(resultUpdateDataResourceAttributeRepresentation)) {
                            // Process the result as needed
                            System.out.println(resultUpdateDataResourceAttributeRepresentation);
                        } else {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultUpdateDataResourceAttributeRepresentation));
                        }
                    }
                    // Insert the digitalFileInformation data
                    else
                    {
                        // Prepare the value to insert digital file information
                        List<Map<String,String>> digitalValuesFromUpdateDigitalFileInformation = new ArrayList<>();
                        digitalValuesFromUpdateDigitalFileInformation.add(digitalValue);
                        // Creating a object of InsertDigitalFileInformation class.
                        InsertDigitalFileInformation insertDigitalFileInformation = new InsertDigitalFileInformation(digitalValuesFromUpdateDigitalFileInformation,productId);
                        insertDigitalFileInformation.insertDigitalInfo();;
                        // Clear the list
                        digitalValuesFromUpdateDigitalFileInformation.clear();
                    }
                }
            }
            catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }

    // A private class for creating and update product categories based on provided values
    private static class CreateProductCategory
    {
        private Map<String,Object> productCategoryAAIAValue = new HashMap<>();
        // Constructor to initialize with product category values
        CreateProductCategory(Map<String,Object> productCategoryAAIAValue)
        {
            this.productCategoryAAIAValue = productCategoryAAIAValue;
        }
        // Method for creating and updating the product category
        private void createProductCategory()
        {
            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                // Find the user login information for the system user
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                // Check if the primary category already exists
                GenericValue checkIfPrimaryCategoryExits = EntityQuery.use(delegator).from("ProductCategory").where("productCategoryId",productCategoryAAIAValue.get("aaiapProductCategoryCode")).queryOne();
                if(checkIfPrimaryCategoryExits!=null) // If primary category found
                {
                    // Build the condition to find a specific product category
                    EntityCondition conditionToFindProductCategory = EntityCondition.makeCondition(
                            EntityCondition.makeCondition("categoryName", EntityOperator.EQUALS,productCategoryAAIAValue.get("BrandAAIAID") ),
                            EntityOperator.AND,
                            EntityCondition.makeCondition("primaryParentCategoryId", EntityOperator.EQUALS, productCategoryAAIAValue.get("aaiapProductCategoryCode"))
                    );
                    // Check if the specific category exists
                    GenericValue checkIfCategoryExits = EntityQuery.use(delegator).from("ProductCategory").where(conditionToFindProductCategory).queryOne();
                    if(checkIfCategoryExits==null) // If primary category found but category not found, then update a child category
                    {
                        // Build the condition to find the exact product category
                        EntityCondition conditionToFindExactProductCategory = EntityCondition.makeCondition(
                                EntityCondition.makeCondition("primaryParentCategoryId", EntityOperator.EQUALS, productCategoryAAIAValue.get("aaiapProductCategoryCode"))
                        );
                        // Get the category ID of the existing category
                        GenericValue getCategoryId = EntityQuery.use(delegator).from("ProductCategory").where(conditionToFindExactProductCategory).queryOne();
                        // Prepare data for updating the product category
                        Map<String,Object> productCategory= new HashMap<>();
                        productCategory.put("userLogin", permUserLogin);
                        productCategory.put("productCategoryId",getCategoryId.get("productCategoryId"));
                        productCategory.put("productCategoryTypeId","BRAND_CATEGORY");
                        productCategory.put("categoryName",productCategoryAAIAValue.get("BrandAAIAID"));
                        // Update the product category
                        Map<String, Object> resultCategory = dispatcher.runSync("updateProductCategory", productCategory);
                        if (ServiceUtil.isSuccess(resultCategory))
                        {
                            System.out.println(resultCategory);
                        }else
                        {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultCategory));
                        }
                    }
                }else { // Creating primary category and product category if it is not exits
                    // Prepare data for creating the primary product category
                    Map<String,Object> primaryProductCategory = new HashMap<>();
                    primaryProductCategory.put("productCategoryId",productCategoryAAIAValue.get("aaiapProductCategoryCode"));
                    primaryProductCategory.put("productCategoryTypeId","AAIA_PRODUCTCATEGORY");
                    primaryProductCategory.put("userLogin", permUserLogin);
                    // Create the primary product category
                    Map<String, Object> resultPrimaryCategory = dispatcher.runSync("createProductCategory", primaryProductCategory);
                    if (ServiceUtil.isSuccess(resultPrimaryCategory)) {
                        String productCategoryId = getNextSequenceId(delegator, "ProductCategorySeqId");
                        // Prepare data for creating the product category
                        Map<String,Object> productCategory= new HashMap<>();
                        productCategory.put("userLogin", permUserLogin);
                        productCategory.put("productCategoryId",productCategoryId);
                        productCategory.put("productCategoryTypeId","BRAND_CATEGORY");
                        productCategory.put("categoryName",productCategoryAAIAValue.get("BrandAAIAID"));
                        productCategory.put("primaryParentCategoryId",productCategoryAAIAValue.get("aaiapProductCategoryCode"));
                        // Create the product category
                        Map<String, Object> resultCategory = dispatcher.runSync("createProductCategory", productCategory);
                        if (ServiceUtil.isSuccess(resultCategory))
                        {
                            System.out.println("Created product category");
                            System.out.println(resultCategory);
                        }else
                        {
                            System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultCategory));
                        }
                    }else
                    {
                        System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultPrimaryCategory));
                    }
                }

            }catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }


    // A private class for adding product to category
    private static class AddProductToCategory
    {
        private String productId;
        private String primaryCategoryId;
        // Constructor to initialize with productId and primaryCategoryId values
        AddProductToCategory(String productId,String primaryCategoryId)
        {
            this.productId = productId;
            this.primaryCategoryId = primaryCategoryId;
        }
        // Method for creating and updating the product category
        private void addProductToCategory()
        {
            try {
                Delegator delegator = DelegatorFactory.getDelegator("default");
                LocalDispatcher dispatcher = ServiceContainer.getLocalDispatcher("default", delegator);
                // Find the user login information for the system user
                GenericValue permUserLogin = EntityQuery.use(delegator).from("UserLogin").where("userLoginId", "system").queryOne();
                // Build the condition to find the exact product category
                EntityCondition conditionToFindExactProductCategory = EntityCondition.makeCondition(
                        EntityCondition.makeCondition("primaryParentCategoryId", EntityOperator.EQUALS, primaryCategoryId)
                );
                // Get the category ID of the existing category
                GenericValue getCategoryId = EntityQuery.use(delegator).from("ProductCategory").where(conditionToFindExactProductCategory).queryOne();
                // Prepare data for adding product to category
                Map<String,Object> productCategoryMember= new HashMap<>();
                productCategoryMember.put("userLogin", permUserLogin);
                productCategoryMember.put("productId",productId);
                productCategoryMember.put("productCategoryId",getCategoryId.get("productCategoryId"));
                // Add the product to the category by calling addProductToCategory service
                Map<String, Object> resultCategoryMember = dispatcher.runSync("addProductToCategory", productCategoryMember);

                if (ServiceUtil.isSuccess(resultCategoryMember)) {
                    System.out.println(resultCategoryMember);
                }else {
                    System.out.println("Error calling service: " + ServiceUtil.getErrorMessage(resultCategoryMember));
                }
            }catch (Exception e) {
                System.out.println("Error calling service: " + e.getMessage());
            }
        }
    }
    }


