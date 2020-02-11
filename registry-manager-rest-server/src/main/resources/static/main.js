const initGlobals = () => {
    window.FILE_UPLOAD_FIELD = document.querySelector("#import-field");
    window.FILE_MERGE_FIELD = document.querySelector("#merge-field");
    window.HIDDEN_ROW = document.querySelector("tbody>tr.hidden");
    window.WARNING = document.querySelector(".alert.warning");
    window.FAILURE = document.querySelector(".alert.failure");
    window.SUCCESS = document.querySelector(".alert.success");
    window.COUNTER = 0;
    window.BASE = "/v1/aspsps";
    window.BASE_URL = "";
    window.HIT_BUTTON = "NONE";
}

setTimeout(() => {
    initGlobals();
    document.querySelector("#upload>button").addEventListener("click", () => FILE_UPLOAD_FIELD.click());
    FILE_UPLOAD_FIELD.addEventListener("change", validateUpload);
    document.querySelector("#merge>button").addEventListener("click", () => FILE_MERGE_FIELD.click());
    FILE_MERGE_FIELD.addEventListener("change", validateMerge);
    document.querySelectorAll(".mdl-textfield__input").forEach(field => field.addEventListener('keypress', event => onEnterPress(event)));
}, 0);

const bankName = (element) => {
    let target = element.textContent;

    if (target === "") {
        element.classList.add("invalid");
        warning("Bank name should not be empty");
    } else {
        element.classList.remove("invalid");
    }
}

const bic = (element) => {
    toUpper(element);
    let target = element.textContent;
    let regex = /^[A-Z]{6}([A-Z0-9]{2})?([A-Z0-9]{5})?$/;

    if (!regex.test(target)) {
        element.classList.add("invalid");
        if (!element.parentElement.cells[5].classList.contains("invalid")) {
            validate(bankCode, element.parentElement.cells[5]);
            return;
        }
        warning("BIC should be 6, 8 or 11 characters long and consist of word characters and numbers only and not empty");
    } else {
        element.parentElement.cells[5].classList.remove("invalid");
        element.classList.remove("invalid");
    }
}

const url = (element) => {
    let target = element.textContent;
    let regex = /(https|http):\/\/[\w\-]+\.[^\n\r]+$/;

    if (!regex.test(target) && !(target === "" && element.classList.contains("idp-url"))) {
        element.classList.add("invalid");
        warning("URL format is wrong, e.g. right format is https://example.test, or field is empty");
    } else {
        element.classList.remove("invalid");
    }
}

const adapterId = (element) => {
    let target = element.textContent;
    let regex = /^[\w\-]+-adapter$/;

    if (!regex.test(target)) {
        element.classList.add("invalid");
        warning("Adapter Id should consist of a-z, A-Z, 0-9, a hyphen(-) only and ends with '...-adapter', e.g. '12345-adapter', and should not be emoty");
    } else {
        element.classList.remove("invalid");
    }
}

const bankCode = (element) => {
    let target = element.textContent;
    let regex = /^\d{8}$/;

    if (!regex.test(target)) {
        element.classList.add("invalid");
        if (!element.parentElement.cells[2].classList.contains("invalid")) {
            validate(bic, element.parentElement.cells[2]);
            return;
        }
        warning("Bank Code should be 8 digits long and consist of numbers only and not empty");
    } else {
        element.parentElement.cells[2].classList.remove("invalid");
        element.classList.remove("invalid");
    }
}

const validate = (rule, target) => {
    rule(target);
}

const toUpper = (element) => {
    element.innerText = element.innerText.toUpperCase();
}

const forceValidation = () => {
    let rows = document.querySelectorAll("tr");
    const makeValid = (element) => {
        if (element.classList.contains("invalid")) {
            element.classList.remove("invalid");
        }
    }

    rows.forEach((row) => {
        if (!row.classList.contains("hidden")) {
            for (let cell of row.cells) {
                makeValid(cell);
            }
        }
    })

}

// Manipulating cells
const uneditableCells = (e) => {
    let rowCells = e.parentElement.parentElement.cells;
    let approach = e.parentElement.parentElement.querySelectorAll('input');

    for (let i = 1, till = (rowCells.length - 1); i < till; i++) {
        rowCells[i].removeAttribute("contenteditable");
    }

    approach.forEach(element => {
        element.disabled = true;
    })
}

const editableCells = (e) => {
    let rowCells = e.parentElement.parentElement.cells;
    let approach = e.parentElement.parentElement.querySelectorAll('input');

    for (let i = 1, till = (rowCells.length - 2); i < till; i++) {
        rowCells[i].setAttribute("contenteditable", true);
    }

    approach.forEach(element => {
        element.disabled = false;
    })
}
// End of cells part

// Manipulating tables
const showTable = () => {
    let table = HIDDEN_ROW.parentElement.parentElement.parentElement;
    let message = document.querySelector(".welcome-message");

    table.hidden = false;
    message.hidden = true;
}

const clearTable = () => {
    let body = document.querySelectorAll("tbody>tr");

    if (body.length > 1) {
        body.forEach(e => { if (!e.className) { e.remove(); } })
    }
}

const checkMorePart = () => {
    let showMore = document.querySelector(".show-more");

    if (!showMore.hidden) {
        showMore.hidden = true;
    }
}

const clearContent = () => {
    clearTable();
    checkMorePart();

    document.querySelectorAll(".mdl-textfield__input").forEach(element => { element.value = ""; element.parentElement.classList.remove("is-dirty") });
}
// End of table part

const onEnterPress = (event) => {
    if (event.keyCode === 13) {
        searchButton();
    }
}

const addTooltips = (e) => {
    let editId = "edit-";
    let updateId = "update-";
    let deleteId = "delete-";
    
    if (e.className.indexOf("edit") > -1) {
        let helper = e.parentNode.childNodes[7];
        
        e.addEventListener("click", () => { editButton(e) });
        e.setAttribute("id", editId + COUNTER);
        
        helper.setAttribute("data-mdl-for", editId + COUNTER);
        helper.setAttribute("class", "mdl-tooltip mdl-tooltip--top");
    }
    
    if (e.className.indexOf("update") > -1) {
        let helper = e.parentNode.childNodes[9];
        
        e.addEventListener("click", () => { greenButton(e) });
        e.setAttribute("id", updateId + COUNTER);
        
        helper.setAttribute("data-mdl-for", updateId + COUNTER);
        helper.setAttribute("class", "mdl-tooltip mdl-tooltip--top");
    }
    
    if (e.className.indexOf("delete") > -1) {
        let helper = e.parentNode.childNodes[11];
        
        e.addEventListener("click", () => { redButton(e) });
        e.setAttribute("id", deleteId + COUNTER);
        
        helper.setAttribute("data-mdl-for", deleteId + COUNTER);
        helper.setAttribute("class", "mdl-tooltip mdl-tooltip--top");
    }
}

// Manipulating rows
const purgeRow = (e) => {
    let tableRow = e.parentElement.parentElement;

    tableRow.remove();
}

const assembleRowData = (e) => {
    let row = e.parentNode.parentNode;

    let object = {};
    object.id = row.cells[0].textContent;
    object.name = row.cells[1].textContent;
    object.bic = row.cells[2].textContent;
    object.url = row.cells[3].textContent;
    object.adapterId = row.cells[4].textContent;
    object.bankCode = row.cells[5].textContent;
    object.idpUrl = row.cells[6].textContent;
    object.scaApproaches = approachParser(row.cells[7]);

    return JSON.stringify(object);

    function approachParser (data) {
        let inputs = data.querySelectorAll("input");
        let resultString = [];

        inputs.forEach((element) => {
            if (element.checked) {
                resultString.push(element.name);
            }
        });

        return resultString;
    }
}

const buildRow = (data) => {
    let clone = HIDDEN_ROW.cloneNode(true);
    clone.removeAttribute("class");

    clone.cells[0].textContent = data.id;
    clone.cells[1].textContent = data.name;
    clone.cells[2].textContent = data.bic;
    clone.cells[3].textContent = data.url;
    clone.cells[4].textContent = data.adapterId;
    clone.cells[5].textContent = data.bankCode;
    clone.cells[6].textContent = data.idpUrl;
    approachParser(data.scaApproaches, clone.cells[7]);

    clone.lastElementChild.childNodes.forEach(e => {
        if (e.className) {
            addTooltips(e)
        }
    });
    document.querySelector("table>tbody").appendChild(clone);

    COUNTER++;

    // updating MDL library for making Tooltip working
    componentHandler.upgradeAllRegistered();

    function approachParser(data, cell) {
        if (!data) {
            return;
        }

        let inputs = cell.querySelectorAll("input");

        data.forEach(element => {
            switch (element) {
                case "EMBEDDED":
                    inputs[0].checked = true;
                    break;
                case "REDIRECT":
                    inputs[1].checked = true;
                    break;
                case "DECOUPLED":
                    inputs[2].checked = true;
                    break;
                case "OAUTH":
                    inputs[3].checked = true;
                    break;
            }
        })
    }
}
// End of row part

const toggleModal = () => {
    const modal = document.querySelector(".validation-layout");
    
    modal.classList.toggle("hidden");

    showSpinner();
}

const showSpinner = () => {
    const spinner = document.querySelector(".spinner");
    const verdict = document.querySelector(".verdict");
    const verdictReport = document.querySelector(".validation-report");
    const merge = document.querySelector(".merge-request");
    const upload = document.querySelector(".upload-request");

    verdict.classList.add("hidden");
    verdictReport.classList.add("hidden");
    merge.classList.add("hidden");
    upload.classList.add("hidden");

    spinner.classList.remove("hidden");
}

const createFile = (data, fileName, fileFormat) => {

    let virtualUrl = null;
    let temp = null;

    if (fileFormat === "json") {
        temp = new Blob([JSON.stringify(data)]);
        virtualUrl = URL.createObjectURL(temp); 
    } else {
        temp = new Blob([data], {type: 'text/csv;charset=utf-8;'});
        virtualUrl = URL.createObjectURL(temp);
    }

    const virtualLink = document.createElement("a");
     
    virtualLink.href = virtualUrl;
    virtualLink.download = fileName + "." + fileFormat;
    virtualLink.click();
}

const resolveResponseJson = (json) => {
    if (!json) {
        return "It's save to proceed";
    } 

    let output = "Duplicate found: \n";

    for (let key in json) {
        output += key + ": " + json[key] + "\n";
    }

    return output += "\nAre you sure you want to proceed?";
}
function fail(message) {
    showMessage(FAILURE, 8000, message);
}

function success(message) {
    showMessage(SUCCESS, 8000, message);
}

function warning(message) {
    showMessage(WARNING, 8000, message);
}

const showMessage = (messageType, duration, message) => {
    let messageBlock = messageType.querySelector(".message");

    messageBlock.textContent = message;

    setTimeout(() => { messageType.style.opacity = 1 }, 500);

    setTimeout(() => { messageType.style.opacity = 0 }, duration);
}

function addRow() {
    let clone = HIDDEN_ROW.cloneNode(true);
    clone.removeAttribute("class");
    clone.setAttribute("class", "new");
    clone.lastElementChild.childNodes.forEach(e => {
        if (e.className) {
            addTooltips(e);
            editButton(e);
        }
    });
    document.querySelector("table>tbody").appendChild(clone);

    COUNTER++;

    if (HIDDEN_ROW.parentElement.parentElement.parentElement.hidden) {
        showTable();
    }

    warning("Bank Name, URL, Adapter Id, Bank Code or BIC must not be empty");
    // updating MDL library for making Tooltip working
    componentHandler.upgradeAllRegistered();
}

async function showMore() {

    let pagination = "page=" + PAGINATOR.page + "&size=" + PAGINATOR.size;

    let nextPageUrl = BASE_URL + pagination;

    let output = await search(nextPageUrl);

    PAGINATOR.addRow(output.data);

    forceValidation();
}

function editButton(e) {
    let editButton = e.parentNode.children[0];
    let updateOrSaveButton = e.parentNode.children[1];
    let deleteButton = e.parentNode.children[2];

    if (editButton.style.display !== "none") {
        editButton.style.display = "none";
        updateOrSaveButton.style.display = "inherit";
        deleteButton.style.display = "inherit";
        editableCells(e);
    }
}

function toggleButtons(e) {
    let editButton = e.parentNode.children[0];
    let updateOrSaveButton = e.parentNode.children[1];
    let deleteButton = e.parentNode.children[2];

    if (editButton.style.display === "none") {
        editButton.style.display = "inherit";
        updateOrSaveButton.style.display = "none";
        deleteButton.style.display = "none";
        uneditableCells(e);
    } else {
        editButton.style.display = "none";
        updateOrSaveButton.style.display = "inherit";
        deleteButton.style.display = "inherit";
        editableCells(e);
    }
}

function greenButton(e) {
    let tableRow = e.parentElement.parentElement;

    if (tableRow.className) {
        let isDuplicate;
        checkForDuplicates(e)
            .then(response => isDuplicate = response)
            .finally(() => {
                if (window.confirm(resolveResponseJson(isDuplicate))) {
                    saveButton(e);
                }
            });
        return;
    }


    if (window.confirm(`Are you sure you want to update the aspsp?`)) {
        updateButton(e);
    } else {
        toggleButtons(e);
    }
}

function redButton(e) {
    let tableRow = e.parentElement.parentElement;

    if (tableRow.className) {
        purgeRow(e);
    } else {
        if (window.confirm("You you sure you want to delete this aspsp record?")) {
            deleteButton(e);
        } else {
            toggleButtons(e);
        }
    }
}

const searchButton = async () => {
    clearTable();

    let response;

    BASE_URL = BASE + "/?";

    let data = document.querySelector(".search-form");

    if (data[0].value !== "")
        BASE_URL += "name=" + data[0].value.toLowerCase() + "&";

    if (data[1].value !== "")
        BASE_URL += "bic=" + data[1].value + "&";

    if (data[2].value !== "")
        BASE_URL += "bankCode=" + data[2].value + "&";

    if (data[3].value !== "")
        BASE_URL += "adapterId=" + data[3].value + "&";

    try {
        response = await search(BASE_URL.slice(0, -1));

        if (response.data.length === 0) {
            warning("Failed to find any records. Please double check the search conditions");
            return;
        }

        PAGINATOR.create(response.data, response.headers);
    } catch (error) {
        fail("Oops... Something went wrong");
        return;
    }

    if (HIDDEN_ROW.parentElement.parentElement.parentElement.hidden) {
        showTable();
    }

    forceValidation();
}

function showButton() {
    let drawer = document.querySelector(".mdl-layout__drawer");
    let icon = document.querySelector(".expand>button>i");

    drawer.classList.toggle("is-hidden");
    icon.classList.toggle("rotate");
}

const proceedButton = () => {

    upload();

    showSpinner();
}

const confirmButton = () => {

    merge();

    showSpinner();
}

const reportButton = () => {
    createFile(VALIDATOR.data, "report", "json");
}

const downloadButton = () => {
    download();
}

const rejectCancelButton = () => {
    toggleModal();
}
let PAGINATOR = {
    data: null,
    page: 0,
    showMore: null,
    button: null,
    total: null,
    left: 0,
    size: 0
};

    // size is calculated with the consideration to have a user clicking on SHOW NEXT button
    // at most 10 times. E.g. if the total element quantity in the result set is 300, the size for
    // the next page request will be 30 and there can be only 10 requests ( 30 * 10 = 300 )
    PAGINATOR.setSize = (dataLength) => {
        PAGINATOR.size = (dataLength - 10) / 10 <= 10 ? Math.min(10, dataLength) : Math.floor((dataLength - 10) / 10) + 1;
    };

    PAGINATOR.create = (data, dataLength) => {

        PAGINATOR.page = 0;
        PAGINATOR.data = data;
        PAGINATOR.setSize(dataLength);
        PAGINATOR.left = dataLength;
        PAGINATOR.showMore = document.querySelector(".show-more");
        PAGINATOR.button = document.querySelector(".show-more>.more");
        PAGINATOR.total = document.querySelector(".total");

        PAGINATOR.total.innerHTML = dataLength;

        PAGINATOR.addRow(PAGINATOR.data);
    };

    PAGINATOR.addRow = (input) => {
        for (let iterator = 0, limit = input.length; iterator < limit; iterator++) {
            buildRow(input[iterator]);
            PAGINATOR.left--;
        }

        PAGINATOR.button.innerHTML = "show next " + Math.min(PAGINATOR.size, PAGINATOR.left);
        PAGINATOR.page++;
        PAGINATOR.showMore.hidden = PAGINATOR.left === 0;
    };

window.onload = async () => {
    window.COUNTUP = new CountUp("total", await getTotal());
    
    COUNTUP.start();
}

let getTotal = async () => {

    let result = await fetch(BASE + "/count");
    result = await result.text();
    return result;
}
// this counter is made by inorgaik https://inorganik.github.io/countUp.js/

var __assign=this&&this.__assign||function(){return(__assign=Object.assign||function(t){for(var i,a=1,s=arguments.length;a<s;a++)for(var n in i=arguments[a])Object.prototype.hasOwnProperty.call(i,n)&&(t[n]=i[n]);return t}).apply(this,arguments)},CountUp=function(){function t(t,i,a){var s=this;this.target=t,this.endVal=i,this.options=a,this.version="2.0.4",this.defaults={startVal:0,decimalPlaces:0,duration:2,useEasing:!0,useGrouping:!0,smartEasingThreshold:999,smartEasingAmount:333,separator:",",decimal:".",prefix:"",suffix:""},this.finalEndVal=null,this.useEasing=!0,this.countDown=!1,this.error="",this.startVal=0,this.paused=!0,this.count=function(t){s.startTime||(s.startTime=t);var i=t-s.startTime;s.remaining=s.duration-i,s.useEasing?s.countDown?s.frameVal=s.startVal-s.easingFn(i,0,s.startVal-s.endVal,s.duration):s.frameVal=s.easingFn(i,s.startVal,s.endVal-s.startVal,s.duration):s.countDown?s.frameVal=s.startVal-(s.startVal-s.endVal)*(i/s.duration):s.frameVal=s.startVal+(s.endVal-s.startVal)*(i/s.duration),s.countDown?s.frameVal=s.frameVal<s.endVal?s.endVal:s.frameVal:s.frameVal=s.frameVal>s.endVal?s.endVal:s.frameVal,s.frameVal=Math.round(s.frameVal*s.decimalMult)/s.decimalMult,s.printValue(s.frameVal),i<s.duration?s.rAF=requestAnimationFrame(s.count):null!==s.finalEndVal?s.update(s.finalEndVal):s.callback&&s.callback()},this.formatNumber=function(t){var i,a,n,e,r,o=t<0?"-":"";if(i=Math.abs(t).toFixed(s.options.decimalPlaces),n=(a=(i+="").split("."))[0],e=a.length>1?s.options.decimal+a[1]:"",s.options.useGrouping){r="";for(var l=0,h=n.length;l<h;++l)0!==l&&l%3==0&&(r=s.options.separator+r),r=n[h-l-1]+r;n=r}return s.options.numerals&&s.options.numerals.length&&(n=n.replace(/[0-9]/g,function(t){return s.options.numerals[+t]}),e=e.replace(/[0-9]/g,function(t){return s.options.numerals[+t]})),o+s.options.prefix+n+e+s.options.suffix},this.easeOutExpo=function(t,i,a,s){return a*(1-Math.pow(2,-10*t/s))*1024/1023+i},this.options=__assign({},this.defaults,a),this.formattingFn=this.options.formattingFn?this.options.formattingFn:this.formatNumber,this.easingFn=this.options.easingFn?this.options.easingFn:this.easeOutExpo,this.startVal=this.validateValue(this.options.startVal),this.frameVal=this.startVal,this.endVal=this.validateValue(i),this.options.decimalPlaces=Math.max(this.options.decimalPlaces),this.decimalMult=Math.pow(10,this.options.decimalPlaces),this.resetDuration(),this.options.separator=String(this.options.separator),this.useEasing=this.options.useEasing,""===this.options.separator&&(this.options.useGrouping=!1),this.el="string"==typeof t?document.getElementById(t):t,this.el?this.printValue(this.startVal):this.error="[CountUp] target is null or undefined"}return t.prototype.determineDirectionAndSmartEasing=function(){var t=this.finalEndVal?this.finalEndVal:this.endVal;this.countDown=this.startVal>t;var i=t-this.startVal;if(Math.abs(i)>this.options.smartEasingThreshold){this.finalEndVal=t;var a=this.countDown?1:-1;this.endVal=t+a*this.options.smartEasingAmount,this.duration=this.duration/2}else this.endVal=t,this.finalEndVal=null;this.finalEndVal?this.useEasing=!1:this.useEasing=this.options.useEasing},t.prototype.start=function(t){this.error||(this.callback=t,this.duration>0?(this.determineDirectionAndSmartEasing(),this.paused=!1,this.rAF=requestAnimationFrame(this.count)):this.printValue(this.endVal))},t.prototype.pauseResume=function(){this.paused?(this.startTime=null,this.duration=this.remaining,this.startVal=this.frameVal,this.determineDirectionAndSmartEasing(),this.rAF=requestAnimationFrame(this.count)):cancelAnimationFrame(this.rAF),this.paused=!this.paused},t.prototype.reset=function(){cancelAnimationFrame(this.rAF),this.paused=!0,this.resetDuration(),this.startVal=this.validateValue(this.options.startVal),this.frameVal=this.startVal,this.printValue(this.startVal)},t.prototype.update=function(t){cancelAnimationFrame(this.rAF),this.startTime=null,this.endVal=this.validateValue(t),this.endVal!==this.frameVal&&(this.startVal=this.frameVal,this.finalEndVal||this.resetDuration(),this.determineDirectionAndSmartEasing(),this.rAF=requestAnimationFrame(this.count))},t.prototype.printValue=function(t){var i=this.formattingFn(t);"INPUT"===this.el.tagName?this.el.value=i:"text"===this.el.tagName||"tspan"===this.el.tagName?this.el.textContent=i:this.el.innerHTML=i},t.prototype.ensureNumber=function(t){return"number"==typeof t&&!isNaN(t)},t.prototype.validateValue=function(t){var i=Number(t);return this.ensureNumber(i)?i:(this.error="[CountUp] invalid start or end value: "+t,null)},t.prototype.resetDuration=function(){this.startTime=null,this.duration=1e3*Number(this.options.duration),this.remaining=this.duration},t}();

const VALIDATOR = {
    data: null
}

const validationResponseHandler = (data) => {
    VALIDATOR.data = data;
    
    if (!data) {
        fail("Oops... something went wrong. Please try again to validate");
        toggleModal();
        return;
    }

    let isValid = data.fileValidationReport.validationResult === "VALID";

    const spinner = document.querySelector(".spinner");
    const verdict = document.querySelector("#verdict");
    const report = document.querySelector(".validation-report");
    const amountNotValid = document.querySelector("#records-amount");
    const example = document.querySelector(".display");
    
    verdict.textContent = data.fileValidationReport.validationResult;
    spinner.classList.add("hidden");

    if (!isValid) {
        verdict.classList.add("valid", "not-valid");
        verdict.parentElement.classList.remove("hidden");
        report.classList.remove("hidden");

        amountNotValid.textContent = data.fileValidationReport.totalNotValidRecords;
        example.textContent = buildString(data.fileValidationReport.aspspValidationErrorReports);

        mergeOrUpload(data);
    } else {
        verdict.classList.replace("not-valid", "valid");
        verdict.parentElement.classList.remove("hidden");

        mergeOrUpload(data);
    }

}

const buildString = (input) => {
    let result = "";

    for (let i = 0, limit = Math.min(3, input.length); i < limit; i++) {
        result += "line number: " + input[i].lineNumberInCsv + "\n"
            + "validation errors: \n";

        input[i].validationErrors.forEach(element => {
            result += "\t" + element + "\n";
        });

        result += "\n";
    }

    if (input[3]) {
        result += "\n... ";
    }

    return result;
}

const mergeOrUpload = (input) => {
    const merge = document.querySelector(".merge-request");
    const newRecords = document.querySelector("#new-records");
    const altered = document.querySelector("#altered");

    const upload = document.querySelector(".upload-request");
    const csvRecords = document.querySelector("#csv-quantity");
    const bdSize = document.querySelector("#db-size");

    if (HIT_BUTTON === "MERGE") {
        merge.classList.remove("hidden");

        newRecords.textContent = input.numberOfNewRecords;
        altered.textContent = `${input.difference.length} (${(input.difference.length / COUNTUP.endVal) * 100}%)`;
    } else if (HIT_BUTTON === "UPLOAD") {
        upload.classList.remove("hidden");

        csvRecords.textContent = input.csvFileRecordsNumber;
        bdSize.textContent = input.dbRecordsNumber;
    }
}

const checkForDuplicates = (e) => {
    let row = e.parentElement.parentElement;

    for (let cell of row.cells) {
        if (cell.classList.contains("invalid")) {
            warning("Some data is invalid");
            return;
        }
    }

    return fetch(BASE + "/validate", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: assembleRowData(e)
    }).then(r => r.json());
}

const saveButton = (e) => {
    fetch(BASE, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: assembleRowData(e)
    }).then((response) => {
        if (response.status === 403) {
            warning("It looks like you don't have enough permissions to perform this action");
            return;
        } else if (!response.ok) {
            throw Error(response.statusText);
        }
        row.removeAttribute("class");
        toggleButtons(e);
        return response.text();
    }).then(response => {
        if (!response) {
            return;
        }
        let output = JSON.parse(response);
        row.cells[0].textContent = output.id;
        COUNTUP.update(COUNTUP.endVal + 1);
    }).catch(() => {
        fail("Saving process has failed");
    });
}

const updateButton = (e) => {
    fetch(BASE, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: assembleRowData(e)
    }).then((response) => {
        if (response.status === 403) {
            warning("It looks like you don't have enough permissions to perform this action");
            toggleButtons(e);
            return;
        } else if (!response.ok) {
            throw Error(response.statusText);
        }
        toggleButtons(e);
        return response;
    }).catch(() => {
        fail("Update process has failed");
    });
}

const deleteButton = (e) => {
    let uuidCell = e.parentElement.parentElement.cells[0].innerText;
    let url = BASE + "/" + uuidCell;

    fetch(url, {
        method: 'DELETE',
        headers: {
            'Content-Type': 'application/json'
        }
    }).then((response) => {
        if (response.status === 403) {
            warning("It looks like you don't have enough permissions to perform this action");
            toggleButtons(e);
            return;
        } else if (!response.ok) {
            throw Error(response.statusText);
        }
        purgeRow(e);
        COUNTUP.update(COUNTUP.endVal - 1);
    }).catch(() => {
        fail("Deleting process has failed");
    });
}

const upload = () => {
    let file = FILE_UPLOAD_FIELD.files[0];
    let data = new FormData();

    data.append("file", file);

    fetch(BASE + "/csv/upload", {
        method: 'POST',
        body: data
    }).then(response => {
        if (response.status === 403) {
            warning("It looks like you don't have enough permissions to perform this action");
            return;
        } else if (!response.ok) {
            throw Error(response.statusText);
        }
        success();
        (async () => { COUNTUP.update(await getTotal()); })()
    }).catch(() => {
        fail("Failed to upload the file. It looks like the file has an inappropriate format.");
    }).finally (() => {
        toggleModal();
        FILE_UPLOAD_FIELD.value = "";
    })
}

const merge = () => {
    let file = FILE_MERGE_FIELD.files[0];
    let data = new FormData();

    data.append("file", file);

    fetch(BASE + "/csv/merge", {
        method: 'POST',
        body: data
    }).then(response => {
        if (response.status === 403) {
            warning("It looks like you don't have enough permissions to perform this action");
            return;
        } else if (!response.ok) {
            throw Error(response.statusText);
        }
        success();
        (async () => { COUNTUP.update(await getTotal()); })()
    }).catch(() => {
        fail("Failed to upload and merge the file.");
    }).finally (() => {
        toggleModal();
        FILE_MERGE_FIELD.value = "";
    })
}

const search = async (URI) => {
    let output = {};

    let response = await fetch(URI);
    output.headers = await response.headers.get("X-Total-Elements");
    output.data = JSON.parse(await response.text());

    return output;
}

const validateUpload = () => {
    let file = FILE_UPLOAD_FIELD.files[0];
    let data = new FormData();

    data.append("file", file);

    toggleModal();

    fetch(BASE + "/csv/validate/upload", {
        method: 'POST',
        body: data
    }).then(response => {
        if (response.status === 403) {
            warning("It looks like you don't have enough permissions to perform this action");
            return;
        } else if (!response.ok && response.status !== 400) {
            throw Error(response.statusText);
        }
        return response.text()
    }).then(response => {
        validationResponseHandler(JSON.parse(response));
    }).catch(() => {
        fail("Validation process failed, please check if you provided an appropriately formatted CSV file");
    })
}

const validateMerge = () => {
    let file = FILE_MERGE_FIELD.files[0];
    let data = new FormData();

    data.append("file", file);

    toggleModal();

    fetch(BASE + "/csv/validate/merge", {
        method: 'POST',
        body: data
    }).then(response => {
        if (response.status === 403) {
            warning("It looks like you don't have enough permissions to perform this action");
            return;
        } else if (!response.ok && response.status !== 400) {
            throw Error(response.statusText);
        }
        return response.text()
    }).then(response => {
        validationResponseHandler(JSON.parse(response));
    }).catch(() => {
        fail("Validation process failed, please check if you provided an appropriately formatted CSV file");
    })
}

const download = () => {
    toggleModal();

    fetch("/v1/aspsps/csv/download").then(response => {
        if (!response) {
            throw Error(response.statusText());
        }
        return response.text();
    }).then(response => {
        createFile(response, "aspsps", "csv")
    }).catch(error => {
        fail("Failed to upload the file");
    }).finally(() => {
        toggleModal();
    })
}