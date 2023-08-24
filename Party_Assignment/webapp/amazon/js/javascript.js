 // this function redirect the user to the add new person page
 function addNewPerson() {
      const urlParams = new URLSearchParams(window.location.search);
       const partyId = urlParams.get('partyId');

       // Redirect to the target URL with the partyId as a query parameter
       window.location.href = "/amazon/control/addNewPerson?partyId=" + partyId;
  }

function deleteRelationship(event)
{
      event.preventDefault();
      var formData = $("#deleteRelationship").serialize();
      console.log(formData)

            $.ajax({
              url: "/amazon/control/deletePartyRelationship",
              type: "post",
              data: formData,
              success: function (response) {
                console.log("Data updated successfully");
                 const urlParams = new URLSearchParams(window.location.search);
                 const partyId = urlParams.get('partyId');

                 // Redirect to the target URL with the partyId as a query parameter
                 window.location.href = "/amazon/control/personDetails?partyId=" + partyId;
              },
              error: function (xhr, status, error) {
                // Handle error response
                console.log("Error updating data: " + error);
                // You can add code here to show an error message or perform any other actions
              }
            });
}

  //  handleSubmit function handle the add new person form or create a new person
  function handleSubmit(event) {
    // Prevent the form from submitting and refreshing the page
    event.preventDefault();

    // Retrieve values of input fields using their IDs
    var statusId = document.getElementById("statusIdF").value;
    var firstName = document.getElementById("firstNameF").value;
    var middleName = document.getElementById("middleNameF").value;
    var roleTypeId = document.getElementById("roleTypeIdF").value;
    var lastName = document.getElementById("lastNameF").value;
    var email = document.getElementById("emailF").value;

    var toName = document.getElementById("toNameF").value;
    var city = document.getElementById("cityF").value;
    var postalCode = document.getElementById("postalCodeF").value;
    var address1 = document.getElementById("address1F").value;
    var houseNumber = document.getElementById("houseNumberF").value;
    var contactMechPurposeTypeIdPostal = document.getElementById("contactMechPurposeTypeIdPostal").value;

    var countryCode = document.getElementById("countryCodeF").value;
    var contactNumber = document.getElementById("contactNumberF").value;
    var contactMechPurposeTypeIdPhone = document.getElementById("contactMechPurposeTypeIdPhone").value;

    var PartyRelationshipType = document.getElementById("PartyRelationshipTypeF").value;

    const urlParams = new URLSearchParams(window.location.search);
    const Organization_partyId = urlParams.get('partyId');
    console.log("statusId:", statusId);
    console.log("firstName:", firstName);
    console.log("middleName:", middleName);
    console.log("roleTypeId:", roleTypeId);
    console.log("lastName:", lastName);
    console.log("email:", email);
    console.log("toName:", toName);
    console.log("city:", city);
    console.log("postalCode:", postalCode);
    console.log("address1:", address1);
    console.log("houseNumber:", houseNumber);
    console.log("contactMechPurposeTypeIdPostal:", contactMechPurposeTypeIdPostal);

    console.log("countryCode:", countryCode);
    console.log("contactNumber:", contactNumber);
    console.log("contactMechPurposeTypeIdPhone:", contactMechPurposeTypeIdPhone);

    console.log("PartyRelationshipType:", PartyRelationshipType);

    // checking if the user selected correct options
    if (statusId.toLowerCase() === "select status id") {
      alert("Please Select correct Status Id")
    } else if (contactMechPurposeTypeIdPostal === "Select Contact Purpose") {
      alert("Please Select Contact purpose")
    } else if (contactMechPurposeTypeIdPhone === "Select Contact Purpose") {
      alert("Please Select Contact purpose");
    } else if (PartyRelationshipType === "Select Relationship") {
      alert("Please select the relationship")
    } else if (roleTypeId === "Select Role Type Id") {
      alert("Please select Role")
    }
    else {   // now we are going to insert the form data to respective tables
      const timestamp = generateDateTime(); // generating timestamp
      console.log(timestamp);
      const partyId = "100" + generatePrimaryKey() + "_P"; // creating party primary key
      console.log(partyId);
      const contactId = "cont_" + generatePrimaryKey(); // creating contact id
      console.log(contactId);
      $.ajax({
        url: "/amazon/control/createPerson",
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify({
          partyId: partyId,
          firstName: firstName,
          middleName: middleName,
          lastName: lastName,
          statusId: statusId
        }),
        success: function (data) {
          console.log("successfully Created Person");
          $.ajax({
            url: "/amazon/control/createRole",
            type: "POST",
            contentType: 'application/json',
            data: JSON.stringify({
              partyId: partyId,
              roleTypeId: roleTypeId
            }),
            success: function (data) {
              console.log("Created Party Role");
              $.ajax({
                url: "/amazon/control/createPostal",
                type: "POST",
                contentType: 'application/json',
                data: JSON.stringify({
                  contactMechId: contactId,
                  toName: toName,
                  address1: address1,
                  houseNumber: houseNumber,
                  city: city,
                  postalCode: postalCode
                }),
                success: function (data) {
                  console.log("Postal address created");
                  const contactIdTelecom = "cont_" + generatePrimaryKey();
                  $.ajax({
                    url: "/amazon/control/createTelecom",
                    type: "POST",
                    contentType: 'application/json',
                    data: JSON.stringify({
                      contactMechId: contactIdTelecom,
                      countryCode: countryCode,
                      contactNumber: contactNumber
                    }),
                    success: function (data) {
                      console.log("Telecom Number Created.");
                      $.ajax({
                        url: "/amazon/control/createPurpose",
                        type: "POST",
                        contentType: 'application/json',
                        data: JSON.stringify({
                          partyId: partyId,
                          contactMechId: contactId,
                          contactMechPurposeTypeId: contactMechPurposeTypeIdPostal,
                          fromDate: timestamp
                        }),
                        success: function (data) {
                          console.log("Created Postal Purpose.");
                          $.ajax({
                            url: "/amazon/control/createPurpose",
                            type: "POST",
                            contentType: 'application/json',
                            data: JSON.stringify({
                              partyId: partyId,
                              contactMechId: contactIdTelecom,
                              contactMechPurposeTypeId: contactMechPurposeTypeIdPhone,
                              fromDate: timestamp
                            }),
                            success: function (data) {
                              console.log("Created Telecom Purpose.");
                              $.ajax({
                                url: "/amazon/control/createRelationship",
                                type: "POST",
                                contentType: 'application/json',
                                data: JSON.stringify({
                                  partyIdFrom: Organization_partyId,
                                  partyIdTo: partyId,
                                  roleTypeIdFrom: "ORGANIZATION_ROLE",
                                  roleTypeIdTo: roleTypeId,
                                  partyRelationshipTypeId: PartyRelationshipType,
                                  fromDate: timestamp
                                }),
                                success: function (data) {
                                  console.log("Created Relationship.");
                                  location.href = "/amazon/control/personDetails?partyId=" +Organization_partyId;

                                },
                                error: function (xhr, status, error) {
                                  // Request failed, handle error here if needed
                                  console.error("Failed create relationship.");
                                }
                              });
                            },
                            error: function (xhr, status, error) {
                              // Request failed, handle error here if needed
                              console.error("Failed to create Telecom Purpose");
                            }
                          });

                        },
                        error: function (xhr, status, error) {
                          // Request failed, handle error here if needed
                          console.error("Failed create Postal Address Purpose.");
                        }
                      });
                    },
                    error: function (xhr, status, error) {
                      // Request failed, handle error here if needed
                      console.error("Failed to create Telecom Number.");
                    }
                  });
                },
                error: function (xhr, status, error) {
                  // Request failed, handle error here if needed
                  console.error("Failed to create postal address.");
                }
              });
            },
            error: function (xhr, status, error) {
              // Request failed, handle error here if needed
              console.error("Failed to create role.");
            }
          });
        },
        error: function (xhr, status, error) {
          // Request failed, handle error here if needed
          console.error("Failed to create person.");
        }
      });
      event.target.reset();
    }
  }


// function to generate timestamp
  function generateDateTime() {
    const currentDate = new Date();
    const sqlFormattedDateTime = currentDate.toISOString().slice(0, 19).replace('T', ' ');
    return sqlFormattedDateTime;
  }
// function to generate primary key
  function generatePrimaryKey() {
    const date = new Date();

    // Extracting individual components of the date
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');
    const seconds = String(date.getSeconds()).padStart(2, '0');
    const milliseconds = String(date.getMilliseconds()).padStart(3, '0');

    // Generating a random component (6-digit random number)
    const randomComponent = String(Math.floor(Math.random() * 1000000)).padStart(6, '0');

    // Combining the components to form the primary key
    const primaryKey = `${year}${month}${day}${hours}${minutes}${seconds}${milliseconds}${randomComponent}`;

    return primaryKey.slice(0, 15);;
  }


// function to update the value of organization fields
  function orgSave() {

    var id = document.getElementById("orgId").value;
    console.log(id)

    var companyName = document.getElementById("companyName").value;
    console.log(companyName)

    var annualRevenue = document.getElementById("annualRevenue").value;
    console.log(annualRevenue);

    var buttonElement = document.getElementById("orgCloseButton");
    $.ajax({
      url: "/amazon/control/updateGroup",
      type: "POST",
      contentType: 'application/json',
      data: JSON.stringify({
        partyId: id,
        groupName: companyName,
        annualRevenue: annualRevenue
      }),
      success: function (data) {
        // Request was successful, handle response here if needed
        console.log("Item updated successfully.");

        document.getElementById("tdGroupName").textContent = companyName;
        document.getElementById("tdannualRevenue").textContent = annualRevenue;

        buttonElement.click();
      },
      error: function (xhr, status, error) {
        // Request failed, handle error here if needed
        console.error("Failed to delete item.");
      }
    });

  }
// this function show model to edit organization value
  function orgEdit(button) {
  const row = button.parentNode.parentNode; // Get the <tr> element that contains the button

  // Get the values from the row
  const partyId = row.cells[0].innerText;
  const groupName = row.cells[1].innerText;
  const annualRevenue = row.cells[2].innerText;

  // Do whatever you want with the values (e.g., display them in a popup, log them, etc.)
  console.log("Party ID:", partyId);
  console.log("Group Name:", groupName);
  console.log("Annual Revenue:", annualRevenue);

  location.href = "/amazon/control/personDetails"

  window
}
















  function createUpdateFirstColumnFunction() {
    // Declare a variable to store the firstColumnTd reference
    var firstColumnTd;

    // Return a function (closure) that can access and update the firstColumnTd variable
    return function (td) {
      if (td) {
        // If the function is called with an argument (td), update the firstColumnTd variable
        firstColumnTd = td;
      } else {
        // If the function is called without an argument, return the current value of firstColumnTd
        return firstColumnTd;
      }
    };
  }

  // Call the createUpdateFirstColumnFunction to get the closure function
  var updateFirstColumn = createUpdateFirstColumnFunction();



  function logRowValues(button) {
    var row = button.parentNode.parentNode; // Get the parent row of the clicked button
    var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row


    var myModal = new bootstrap.Modal(document.getElementById('myModal'));
    myModal.show();
  }

  function postalbutton(button) {
    var row = button.parentNode.parentNode; // Get the parent row of the clicked button
    var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row


    var myModal = new bootstrap.Modal(document.getElementById('myModalForPostal'));
    myModal.show();
  }

  function teleButton(button) {
    var row = button.parentNode.parentNode; // Get the parent row of the clicked button
    var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row


    var myModal = new bootstrap.Modal(document.getElementById('myModalForTele'));
    myModal.show();
  }



  function relValues(button) {
    var row = button.closest("tr"); // Get the parent row of the clicked button
    var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row
    var fromDate = cells[3].textContent
    console.log(fromDate)
    var partyIdTo = cells[4].textContent
    var partyIdFrom = cells[5].textContent

    var roleTypeIdFrom = cells[0].textContent
    var roleTypeIdTo = cells[1].textContent
    console.log(partyIdFrom + " " + partyIdTo + " " + roleTypeIdFrom + " " + roleTypeIdTo + " " + fromDate)

    $.ajax({
      url: "/amazon/control/deletePartyRelationship",
      type: "POST",
      contentType: 'application/json',
      data: JSON.stringify({
        fromDate: fromDate,
        partyIdTo: partyIdTo,
        partyIdFrom: partyIdFrom,
        roleTypeIdFrom: roleTypeIdFrom,
        roleTypeIdTo: roleTypeIdTo
      }),
      success: function (data) {
        // Request was successful, handle response here if needed
        console.log("Item deleted successfully.");
        console.log(row)
        row.remove();
      },
      error: function (xhr, status, error) {
        // Request failed, handle error here if needed
        console.error("Failed to delete item.");
      }
    });

  }


  <!---deleteTele--->

  function deleteTele() {
    var contactIdF = document.getElementById("contactIdF").innerText;
    console.log(contactIdF);
    var countryCodeF = document.getElementById("countryCodeF");
    var contactNumberF = document.getElementById("contactNumberF");


    $.ajax({
      url: "/amazon/control/deleteTelecomNumber",
      type: "post",
      data: {
        contactMechId: contactIdF
      },
      success: function (response) {
        console.log("success")
        countryCodeF.remove();
        contactNumberF.remove();
        document.getElementById("deleteTeleButton").remove();
        document.getElementById("divTeleEdit").remove();
      },
      error: function (xhr, status, error) {
        // Handle error response
        console.log("Error updating data: " + error);
        // You can add code here to show an error message or perform any other actions
      }
    });
  }
  function deletePostal() {
    var contactMechIdF = document.getElementById("contactMechIdF").innerText;
    console.log(contactMechIdF);
    var toNameF = document.getElementById("toNameF");
    console.log(toNameF);
    var address1F = document.getElementById("address1F");
    console.log(address1F);
    var houseNumberF = document.getElementById("houseNumberF");
    console.log(houseNumberF);
    var cityF = document.getElementById("cityF");
    console.log(cityF);
    var postalCodeF = document.getElementById("postalCodeF");
    console.log(postalCodeF);

    $.ajax({
      url: "/amazon/control/deletePostalAddress",
      type: "post",
      data: { contactMechId: contactMechIdF },
      success: function (response) {
        console.log("Data updated successfully");
        toNameF.remove();
        address1F.remove();
        houseNumberF.remove();
        cityF.remove();
        postalCodeF.remove();
        document.getElementById("divPostalDelete").remove();
        document.getElementById("divPostalEdit").remove();
      },
      error: function (xhr, status, error) {
        // Handle error response
        console.log("Error updating data: " + error);
        // You can add code here to show an error message or perform any other actions
      }
    });
  }

  function deletePurpose(button) {
    var row = button.closest("tr"); // Get the parent row of the clicked button
    var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row

    var partyId = document.getElementById("PersonPartyId").value;
    var contactMechId = cells[2].textContent;
    console.log(partyId)
    console.log(contactMechId)
    var fromDate = cells[3].textContent;
    console.log(fromDate)
    var contactMechTypeId = cells[0].textContent;
    console.log(contactMechTypeId)
    var contactMechPurposeTypeId = cells[1].textContent;
    console.log(contactMechPurposeTypeId)

    // for postal
    var toNameF = document.getElementById("toNameF");
    console.log(toNameF);
    var address1F = document.getElementById("address1F");
    console.log(address1F);
    var houseNumberF = document.getElementById("houseNumberF");
    console.log(houseNumberF);
    var cityF = document.getElementById("cityF");
    console.log(cityF);
    var postalCodeF = document.getElementById("postalCodeF");
    console.log(postalCodeF);

    // for telecomNumber
    var countryCodeF = document.getElementById("countryCodeF");
    var contactNumberF = document.getElementById("contactNumberF");

    $.ajax({
      url: "/amazon/control/deleteContactMechPurpose",
      type: "post",
      data: {
        fromDate: fromDate,
        partyId: partyId,
        contactMechId: contactMechId,
        contactMechPurposeTypeId: contactMechPurposeTypeId
      },
      success: function (response) {
        console.log("Data updated successfully");
        row.remove();
        if (contactMechTypeId == "POSTAL_ADDRESS") {
          toNameF.remove();
          address1F.remove();
          houseNumberF.remove();
          cityF.remove();
          postalCodeF.remove();
          document.getElementById("divPostalDelete").remove();
          document.getElementById("divPostalEdit").remove();
        }
        if (contactMechTypeId == "TELECOM_NUMBER") {
          countryCodeF.remove();
          contactNumberF.remove();
          document.getElementById("deleteTeleButton").remove();
          document.getElementById("divTeleEdit").remove();
        }
      },
      error: function (xhr, status, error) {
        // Handle error response
        console.log("Error updating data: " + error);
        // You can add code here to show an error message or perform any other actions
      }
    });
  }


  function deletePerson() {

    var partyId = document.getElementById("PersonPartyId").value;
    var firstNameF = document.getElementById("firstNameF")
    var middleNameF = document.getElementById("middleNameF")
    var lastNameF = document.getElementById("lastNameF")
    var roleTypeIdF = document.getElementById("roleTypeIdF")
    var partyTypeF = document.getElementById("partyTypeF")
    console.log(partyId)
    var firstName = firstNameF.innerText;
    console.log(firstName);

    var middleName = middleNameF.innerText;
    console.log(middleName);

    var lastName = lastNameF.innerText;
    console.log(lastName);

    var roleTypeId = roleTypeIdF.innerText;
    console.log(roleTypeId);

    var partyType = partyTypeF.innerText;
    console.log(partyType);

    var statusDate = document.getElementById("partyStatusDate").value;
    var partyStatus = document.getElementById("partyStatus").value;
    console.log(statusDate)
    console.log(partyStatus)
    var tbody = document.getElementById('tbodyPurpose');
    var tbodyRel = document.getElementById('tbodyRel');
    if (tbody.children.length === 0 && tbodyRel.children.length === 0) {

      $.ajax({
        url: "/amazon/control/deletePartyRole",
        type: "post",
        data: {
          partyId: partyId,
          roleTypeId: roleTypeId
        },
        success: function (response) {
          console.log("Role deleted ");
          $.ajax({
            url: "/amazon/control/deletePartyRole",
            type: "post",
            data: {
              partyId: partyId,
              roleTypeId: "_NA_"
            },
            success: function (response) {
              console.log("Role deleted 2");
              $.ajax({
                url: "/amazon/control/deletePerson",
                type: "POST",
                contentType: 'application/json',
                data: JSON.stringify({
                  partyId: partyId,
                  statusDate: statusDate,
                  statusId: partyStatus

                }),
                success: function (data) {
                  // Request was successful, handle response here if needed
                  console.log("Person deleted successfully.");
                  location.href = "/amazon/control/Amazon";
                },
                error: function (xhr, status, error) {
                  // Request failed, handle error here if needed
                  console.error("Failed to delete item.");
                }
              });

            },
            error: function (xhr, status, error) {
              // Handle error response
              console.log("Error updating data: " + error);
              // You can add code here to show an error message or perform any other actions
            }
          });

        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });



    } else {
      alert("First delete the Contact Type & Purpose and Relationship records");
    }


  }

  $(document).ready(function () {
    // Test jQuery by hiding the test paragraph on page load
    $("#saveButton").click(function (e) {
      e.preventDefault(); // Prevent normal form submission
      var formData = $("#updateForm").serialize();
      console.log(formData)
      $.ajax({
        url: "/amazon/control/update",
        type: "post",
        data: formData,
        success: function (response) {
          console.log("Data updated successfully");
          document.getElementById("firstNameF").innerHTML = document.getElementById("firstName").value;
          document.getElementById("middleNameF").innerHTML = document.getElementById("middleName").value;
          document.getElementById("lastNameF").innerHTML = document.getElementById("lastName").value;
          document.getElementById("partyTypeF").innerHTML = document.getElementById("partyType").value;
          $("#basicDetailsClose").click();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });
    });
    $("#savePostalButton").click(function (e) {
      e.preventDefault(); // Prevent normal form submission
      var formData = $("#PostalForm").serialize();
      console.log(formData)
      $.ajax({
        url: "/amazon/control/update_PostalAddress",
        type: "post",
        data: formData,
        success: function (response) {
          console.log("Data updated successfully");
          document.getElementById("toNameF").innerHTML = document.getElementById("toName").value;
          document.getElementById("address1F").innerHTML = document.getElementById("address1").value;
          document.getElementById("houseNumberF").innerHTML = document.getElementById("houseNumber").value;
          document.getElementById("cityF").innerHTML = document.getElementById("city").value;
          document.getElementById("postalCodeF").innerHTML = document.getElementById("postalCode").value;
          $("#basicDetailsClosePostal").click();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });

    });
    $("#teleButton").click(function (e) {
      e.preventDefault(); // Prevent normal form submission
      var formData = $("#TeleForm").serialize();
      console.log(formData)
      $.ajax({
        url: "/amazon/control/update_TelePhone",
        type: "post",
        data: formData,
        success: function (response) {
          console.log("Data updated successfully");

          document.getElementById("countryCodeF").innerHTML = document.getElementById("countryCode").value;
          document.getElementById("contactNumberF").innerHTML = document.getElementById("contactNumber").value;
          $("#teleCloseButton").click();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });


    });

    $("#purposeButton").click(function (e) {
      e.preventDefault(); // Prevent normal form submission
      var formData = $("#purposeForm").serialize();
      console.log(formData)
      $.ajax({
        url: "/amazon/control/update_Purpose",
        type: "post",
        data: formData,
        success: function (response) {
          console.log("Data updated successfully");
          var firstColumnTd = updateFirstColumn();
          if (firstColumnTd) {

            firstColumnTd.textContent = document.getElementById("contactMechTypeId").value;
          }
          $("#purposeClose").click();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });


    });


  });


   function createUpdateFirstColumnFunction() {
      // Declare a variable to store the firstColumnTd reference
      var firstColumnTd;

      // Return a function (closure) that can access and update the firstColumnTd variable
      return function (td) {
        if (td) {
          // If the function is called with an argument (td), update the firstColumnTd variable
          firstColumnTd = td;
        } else {
          // If the function is called without an argument, return the current value of firstColumnTd
          return firstColumnTd;
        }
      };
    }

    // Call the createUpdateFirstColumnFunction to get the closure function
    var updateFirstColumn = createUpdateFirstColumnFunction();



    function logRowValues(button) {
      var row = button.parentNode.parentNode; // Get the parent row of the clicked button
      var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row


      var myModal = new bootstrap.Modal(document.getElementById('myModal'));
      myModal.show();
    }

    function postalbutton(button) {
      var row = button.parentNode.parentNode; // Get the parent row of the clicked button
      var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row


      var myModal = new bootstrap.Modal(document.getElementById('myModalForPostal'));
      myModal.show();
    }

    function teleButton(button) {
      var row = button.parentNode.parentNode;
      var cells = row.getElementsByTagName("td");


      var myModal = new bootstrap.Modal(document.getElementById('myModalForTele'));
      myModal.show();
    }

    function roleValues(button) {
      var row = button.parentNode.parentNode; // Get the parent row of the clicked button
      var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row
      var firstColumnTd = row.querySelector("td:first-child");
      updateFirstColumn(firstColumnTd);
      console.log(firstColumnTd)

      var myModal = new bootstrap.Modal(document.getElementById('myModalForRole'));
      myModal.show();
      var contactMechTypeId = document.getElementById("contactMechTypeId");
      console.log(contactMechTypeId)
      contactMechTypeId.value = cells[0].textContent;

      var contactMechPurposeTypeId = document.getElementById("contactMechPurposeTypeId");
      contactMechPurposeTypeId.value = cells[1].textContent;

      var contactIdf = cells[2].textContent;
      console.log(contactIdf)
      document.getElementById("contact").value = contactIdf;

    }


    function relValues(button) {
      var row = button.closest("tr");
      var cells = row.getElementsByTagName("td");
      var fromDate = cells[3].textContent
      console.log(fromDate)
      var partyIdTo = cells[4].textContent
      var partyIdFrom = cells[5].textContent

      var roleTypeIdFrom = cells[0].textContent
      var roleTypeIdTo = cells[1].textContent
      console.log(partyIdFrom + " " + partyIdTo + " " + roleTypeIdFrom + " " + roleTypeIdTo + " " + fromDate)

      $.ajax({
        url: "/amazon/control/deletePartyRelationship",
        type: "POST",
        contentType: 'application/json',
        data: JSON.stringify({
          fromDate: fromDate,
          partyIdTo: partyIdTo,
          partyIdFrom: partyIdFrom,
          roleTypeIdFrom: roleTypeIdFrom,
          roleTypeIdTo: roleTypeIdTo
        }),
        success: function (data) {
          // Request was successful, handle response here if needed
          console.log("Item deleted successfully.");
          console.log(row)
          row.remove();
        },
        error: function (xhr, status, error) {
          // Request failed, handle error here if needed
          console.error("Failed to delete item.");
        }
      });

    }


    <!---deleteTele--->

    function deleteTele() {
      var contactIdF = document.getElementById("contactIdF").innerText;
      console.log(contactIdF);
      var countryCodeF = document.getElementById("countryCodeF");
      var contactNumberF = document.getElementById("contactNumberF");


      $.ajax({
        url: "/amazon/control/deleteTelecomNumber",
        type: "post",
        data: {
          contactMechId: contactIdF
        },
        success: function (response) {
          console.log("success")
          countryCodeF.remove();
          contactNumberF.remove();
          document.getElementById("deleteTeleButton").remove();
          document.getElementById("divTeleEdit").remove();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });
    }
    function deletePostal() {
      var contactMechIdF = document.getElementById("contactMechIdF").innerText;
      console.log(contactMechIdF);
      var toNameF = document.getElementById("toNameF");
      console.log(toNameF);
      var address1F = document.getElementById("address1F");
      console.log(address1F);
      var houseNumberF = document.getElementById("houseNumberF");
      console.log(houseNumberF);
      var cityF = document.getElementById("cityF");
      console.log(cityF);
      var postalCodeF = document.getElementById("postalCodeF");
      console.log(postalCodeF);

      $.ajax({
        url: "/amazon/control/deletePostalAddress",
        type: "post",
        data: { contactMechId: contactMechIdF },
        success: function (response) {
          console.log("Data updated successfully");
          toNameF.remove();
          address1F.remove();
          houseNumberF.remove();
          cityF.remove();
          postalCodeF.remove();
          document.getElementById("divPostalDelete").remove();
          document.getElementById("divPostalEdit").remove();
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });
    }

    function deletePurpose(button) {
      var row = button.closest("tr"); // Get the parent row of the clicked button
      var cells = row.getElementsByTagName("td"); // Get all cells (columns) in that row

      var partyId = document.getElementById("PersonPartyId").value;
      var contactMechId = cells[2].textContent;
      console.log(partyId)
      console.log(contactMechId)
      var fromDate = cells[3].textContent;
      console.log(fromDate)
      var contactMechTypeId = cells[0].textContent;
      console.log(contactMechTypeId)
      var contactMechPurposeTypeId = cells[1].textContent;
      console.log(contactMechPurposeTypeId)

      // for postal
      var toNameF = document.getElementById("toNameF");
      console.log(toNameF);
      var address1F = document.getElementById("address1F");
      console.log(address1F);
      var houseNumberF = document.getElementById("houseNumberF");
      console.log(houseNumberF);
      var cityF = document.getElementById("cityF");
      console.log(cityF);
      var postalCodeF = document.getElementById("postalCodeF");
      console.log(postalCodeF);

      // for telecomNumber
      var countryCodeF = document.getElementById("countryCodeF");
      var contactNumberF = document.getElementById("contactNumberF");

      $.ajax({
        url: "/amazon/control/deleteContactMechPurpose",
        type: "post",
        data: {
          fromDate: fromDate,
          partyId: partyId,
          contactMechId: contactMechId,
          contactMechPurposeTypeId: contactMechPurposeTypeId
        },
        success: function (response) {
          console.log("Data updated successfully");
          row.remove();
          if (contactMechTypeId == "POSTAL_ADDRESS") {
            toNameF.remove();
            address1F.remove();
            houseNumberF.remove();
            cityF.remove();
            postalCodeF.remove();
            document.getElementById("divPostalDelete").remove();
            document.getElementById("divPostalEdit").remove();
          }
          if (contactMechTypeId == "TELECOM_NUMBER") {
            countryCodeF.remove();
            contactNumberF.remove();
            document.getElementById("deleteTeleButton").remove();
            document.getElementById("divTeleEdit").remove();
          }
        },
        error: function (xhr, status, error) {
          // Handle error response
          console.log("Error updating data: " + error);
          // You can add code here to show an error message or perform any other actions
        }
      });
    }


    function deletePerson() {

      var partyId = document.getElementById("PersonPartyId").value;
      var firstNameF = document.getElementById("firstNameF")
      var middleNameF = document.getElementById("middleNameF")
      var lastNameF = document.getElementById("lastNameF")
      var roleTypeIdF = document.getElementById("roleTypeIdF")
      var partyTypeF = document.getElementById("partyTypeF")
      console.log(partyId)
      var firstName = firstNameF.innerText;
      console.log(firstName);

      var middleName = middleNameF.innerText;
      console.log(middleName);

      var lastName = lastNameF.innerText;
      console.log(lastName);

      var roleTypeId = roleTypeIdF.innerText;
      console.log(roleTypeId);

      var partyType = partyTypeF.innerText;
      console.log(partyType);

      var statusDate = document.getElementById("partyStatusDate").value;
      var partyStatus = document.getElementById("partyStatus").value;
      console.log(statusDate)
      console.log(partyStatus)
      var tbody = document.getElementById('tbodyPurpose');
      var tbodyRel = document.getElementById('tbodyRel');
      if (tbody.children.length === 0 && tbodyRel.children.length === 0) {

        $.ajax({
          url: "/amazon/control/deletePartyRole",
          type: "post",
          data: {
            partyId: partyId,
            roleTypeId: roleTypeId
          },
          success: function (response) {
            console.log("Role deleted ");
            $.ajax({
              url: "/amazon/control/deletePartyRole",
              type: "post",
              data: {
                partyId: partyId,
                roleTypeId: "_NA_"
              },
              success: function (response) {
                console.log("Role deleted 2");
                $.ajax({
                  url: "/amazon/control/deletePerson",
                  type: "POST",
                  contentType: 'application/json',
                  data: JSON.stringify({
                    partyId: partyId,
                    statusDate: statusDate,
                    statusId: partyStatus

                  }),
                  success: function (data) {
                    // Request was successful, handle response here if needed
                    console.log("Person deleted successfully.");
                    location.href = "/amazon/control/Amazon";
                  },
                  error: function (xhr, status, error) {
                    // Request failed, handle error here if needed
                    console.error("Failed to delete item.");
                  }
                });

              },
              error: function (xhr, status, error) {
                // Handle error response
                console.log("Error updating data: " + error);
                // You can add code here to show an error message or perform any other actions
              }
            });

          },
          error: function (xhr, status, error) {
            // Handle error response
            console.log("Error updating data: " + error);
            // You can add code here to show an error message or perform any other actions
          }
        });



      } else {
        alert("First delete the Contact Type & Purpose and Relationship records");
      }


    }



