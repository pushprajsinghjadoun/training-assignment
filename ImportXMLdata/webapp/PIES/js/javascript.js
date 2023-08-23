    // function to get file name 

        document.addEventListener('DOMContentLoaded', function() {
            const inputElement = document.getElementById('formFile');
            const filenameInput = document.getElementById('fileNameValue');
            console.log(inputElement);
            console.log(filenameInput);

            inputElement.addEventListener('change', function() {
                          const selectedFile = inputElement.files[0];
                           if (selectedFile) {
                               console.log(selectedFile.name);
                               filenameInput.value = selectedFile.name;
                           } else {
                               fileNameDisplay.textContent = 'No file selected';
                           }
            });
        });
