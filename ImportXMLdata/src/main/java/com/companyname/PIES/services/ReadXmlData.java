package com.companyname.PIES.services;


import java.io.InputStream;
import java.util.Map;


import org.apache.ofbiz.service.DispatchContext;
import org.apache.ofbiz.service.ServiceUtil;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;


public class ReadXmlData {
    // Method to read XML data
    public static Map<String, Object> readXmlDataService(DispatchContext dctx, Map<String, ? extends Object> context) {
       // Prepare the result map and start tracking time
        Map<String, Object> resultMap = ServiceUtil.returnSuccess();
        long startCountTime = System.currentTimeMillis();
        try {
            // Create XMLInputFactory instance for XML parsing
            XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();


           // Open an input stream to the XML file
            InputStream inputStream = new FileInputStream("file/data2.xml");


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


        public void extractData() throws XMLStreamException {
           // Iterate through XML elements and extract data
            while (reader.hasNext()) {
                int event = reader.next();
                switch (event)
                {
                   // Check if the current event is the start of an XML element
                    case XMLStreamConstants.START_ELEMENT:
                        // Get the local name of the current element
                        String localName = reader.getLocalName();
                        // Get the value of a specific XML element with switch case.
                        switch (localName)
                        {
                            case "HazardousMaterialCode":
                                // Extract the content of the current XML element as text
                                String hazardousMaterialCode = reader.getElementText();
                                System.out.println("Hazardous Material Code: " + hazardousMaterialCode);
                                break;
                            case "ItemLevelGTIN":
                               // Get the value of the "gtinQualifier" attribute from the current XML element
                                String gtinQualifier = reader.getAttributeValue(null, "GTINQualifier");
                               // Extract the content of the current XML element as text
                                String gtin = reader.getElementText();
                                System.out.println("GTIN: " + gtin);
                                System.out.println("GTINQualifier: "+gtinQualifier);
                                break;
                            case "Description":
                                String descriptionCode = reader.getAttributeValue(null, "DescriptionCode");
                                String languageCode = reader.getAttributeValue(null, "LanguageCode");
                                String maintenanceType = reader.getAttributeValue(null, "MaintenanceType");
                                System.out.println(languageCode);
                                System.out.println(maintenanceType);
                                String descriptionText = reader.getElementText();
                                if(descriptionCode.equals("DEF"))
                                {
                                    System.out.println(descriptionText);
                                }
                                else if(descriptionCode.equals("DES"))
                                {
                                    System.out.println(descriptionText);
                                }
                                else if(descriptionCode.equals("MKT"))
                                {
                                    System.out.println(descriptionText);
                                }else
                                {
                                    System.out.println(descriptionText);
                                }
                                break;
                            case "ACESApplicationselse":
                                String acesApplicationselse = reader.getElementText();
                                System.out.println("ACESApplicationselse: " + acesApplicationselse);
                                break;
                            case "ItemQuantitySize":
                                String itemQuantitySize = reader.getElementText();
                                System.out.println("ItemQuantitySize: " + itemQuantitySize);
                                break;
                            case "ContainerType":
                                String containerType = reader.getElementText();
                                System.out.println("ContainerType: " + containerType);
                                break;
                            case "QuantityPerApplication":
                                String quantityUOMid = reader.getAttributeValue(null, "UOM");
                                String quantityPerApplication = reader.getElementText();
                                System.out.println(quantityUOMid);
                                System.out.println("QuantityPerApplication: " + quantityPerApplication);
                                break;
                            case "ItemEffectiveDate":
                                String itemEffectiveDate = reader.getElementText();
                                System.out.println("ItemEffectiveDate: " + itemEffectiveDate);
                                break;
                            case "AvailableDate":
                                String availableDate = reader.getElementText();
                                System.out.println("AvailableDate: " + availableDate);
                                break;
                            case "MinimumOrderQuantity":
                                String minimumOrderQuantity = reader.getElementText();
                                System.out.println("MinimumOrderQuantity: " + minimumOrderQuantity);
                                break;
                            case "AAIAProductCategoryCode":
                                String aaiapProductCategoryCode = reader.getElementText();
                                System.out.println("AAIAProductCategoryCode: " + aaiapProductCategoryCode);
                                break;
                            case "PartTerminologyID":
                                String partTerminologyID = reader.getElementText();
                                System.out.println("PartTerminologyID: " + partTerminologyID);
                                break;
                            case "ExtendedProductInformation":
                                String expiCode = reader.getAttributeValue(null, "EXPICode");
                                String ExtendedProductLanguageCode = reader.getAttributeValue(null, "LanguageCode");
                                String maintenanceTypeExtendedProduct = reader.getAttributeValue(null, "MaintenanceType");
                                String extendedProductInformationValue = reader.getElementText();


                                System.out.println(expiCode);
                                System.out.println(ExtendedProductLanguageCode);
                                System.out.println(maintenanceTypeExtendedProduct);
                                System.out.println("ExtendedProductInformationValue : " + extendedProductInformationValue);
                                break;
                            case "ProductAttribute":
                                String AttributeID = reader.getAttributeValue(null, "AttributeID");
                                String maintenanceTypeProductAttributes = reader.getAttributeValue(null, "MaintenanceType");
                                String padbAttribute = reader.getAttributeValue(null, "PADBAttribute");
                                String recordNumber = reader.getAttributeValue(null, "RecordNumber");
                                String productAttributeValue = reader.getElementText();


                                System.out.println("AttributeID: "+AttributeID);
                                System.out.println("MaintenanceType: "+maintenanceTypeProductAttributes);
                                System.out.println("PADBAttribute: "+padbAttribute);
                                System.out.println("RecordNumber: "+recordNumber);
                                System.out.println("ProductAttributeValue : " + productAttributeValue);
                                break;
                            case "Package":
                                String maintenanceTypePackage = reader.getAttributeValue(null, "MaintenanceType");
                                System.out.println("MaintenanceType : " + maintenanceTypePackage);
                                break;
                            case "PackageUOM":
                                String packageUOM = reader.getElementText();
                                System.out.println("PackageUOM : " + packageUOM);
                                break;
                            case "PackageLevelGTIN":
                                String packageLevelGTIN = reader.getElementText();
                                System.out.println("PackageLevelGTIN : " + packageLevelGTIN);
                                break;
                            case "PackageBarCodeCharacters":
                                String packageBarCodeCharacters = reader.getElementText();
                                System.out.println(packageBarCodeCharacters);
                                System.out.println("PackageBarCodeCharacters : " + packageBarCodeCharacters);
                                break;
                            case "QuantityofEaches":
                                String quantityofEaches = reader.getElementText();
                                System.out.println("insuredValue : " + quantityofEaches);
                                break;
                            case "Dimensions":
                                String dimensionsUOM = reader.getAttributeValue(null, "UOM");
                                System.out.println(dimensionsUOM);
                                break;
                            case "Height":
                                String height = reader.getElementText();
                                System.out.println(height);
                                break;
                            case "Width":
                                String width = reader.getElementText();
                                System.out.println(width);
                                break;
                            case "Length":
                                String length = reader.getElementText();
                                System.out.println(length);
                                break;
                            case "Weights":
                                String weightsUOM = reader.getAttributeValue(null, "UOM");
                                System.out.println(weightsUOM);
                                break;
                            case "Weight":
                                String weight = reader.getElementText();
                                System.out.println(weight);
                                break;
                            case "TypeCode":
                                String typeCode = reader.getElementText();
                                System.out.println(typeCode);
                                break;
                            case "PartInterchange":
                                String maintenanceTypePartInterchange = reader.getAttributeValue(null, "MaintenanceType");
                                System.out.println("MaintenanceTypePartInterchange : " + maintenanceTypePartInterchange);
                                break;
                            case "BrandAAIAID":
                                String brandAAIAID = reader.getElementText();
                                System.out.println(brandAAIAID);
                                break;
                            case "PartNumber":
                                String partNumber = reader.getElementText();
                                System.out.println(partNumber);
                                break;
                            case "BrandLabel":
                                String brandLabel = reader.getElementText();
                                System.out.println(brandLabel);
                                break;
                            case "DigitalFileInformation":


                                String maintenanceTypeDigital = reader.getAttributeValue(null, "MaintenanceType");
                                String languageCodeDigital = reader.getAttributeValue(null, "LanguageCode");
                                System.out.println(maintenanceTypeDigital);
                                System.out.println(languageCodeDigital);
                                break;
                            case "AssetType":
                                String assetType = reader.getElementText();
                                System.out.println(assetType);
                                break;
                            case "FileName":
                                String fileName = reader.getElementText();
                                System.out.println(fileName);
                                break;
                            case "FileType":
                                String fileType = reader.getElementText();
                                System.out.println(fileType);
                                break;
                            case "Representation":
                                String representation = reader.getElementText();
                                System.out.println(representation);
                                break;
                            case "Background":
                                String background = reader.getElementText();
                                System.out.println(background);
                                break;
                            case "AssetHeight":
                                String assetHeight = reader.getElementText();
                                System.out.println(assetHeight);
                                break;
                            case "AssetWidth":
                                String assetWidth = reader.getElementText();
                                System.out.println(assetWidth);
                                break;
                            case "FilePath":
                                String filePath = reader.getElementText();
                                System.out.println(filePath);
                                break;
                            case "URI":
                                String uri = reader.getElementText();
                                System.out.println(uri);
                                break;
                            case "AssetDimensions":
                                String assetDimensionsUOM = reader.getAttributeValue(null, "UOM");
                                System.out.println(assetDimensionsUOM);
                                break;
                            case "AssetID":
                                String assetID = reader.getElementText();
                                System.out.println(assetID);
                                break;
                            case "Resolution":
                                String resolution = reader.getElementText();
                                System.out.println(resolution);
                                break;
                            case "FileSize":
                                String fileSize = reader.getElementText();
                                System.out.println(fileSize);
                                break;
                        }
                        break;
                }
            }
        }
    }
}


