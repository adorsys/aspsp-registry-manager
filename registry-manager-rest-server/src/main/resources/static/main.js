setTimeout(function () {
    initGlobals();
    document.querySelector("#upload>button").addEventListener("click", () => FILE_UPLOAD_FIELD.click());
    FILE_UPLOAD_FIELD.addEventListener("change", validate);
    document.querySelector("#merge>button").addEventListener("click", () => FILE_MERGE_FIELD.click());
    FILE_MERGE_FIELD.addEventListener("change", merge);
    document.querySelectorAll(".mdl-textfield__input").forEach(field => field.addEventListener('keypress', event => onEnterPress(event)));
}, 0);

function initGlobals() {
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

function validateBankName(element) {
    let target = element.textContent;
    let regex = /^[\w\s\WäöüÄÖÜß]+$/;

    if (!regex.test(target)) {
        element.classList.add("invalid");
        warning("Bank name should be a plain text, e.g. there shouldn't be symbols like #, @, *, %, etc.");
    } else {
        element.classList.remove("invalid");
    }
}

function validateBic(element) {
    toUpper(element);
    let target = element.textContent;
    let regex = /^[A-Z]{6}([A-Z0-9]{2})?([A-Z0-9]{5})?$/;

    if (!regex.test(target)) {
        element.classList.add("invalid");
        if (!element.parentElement.cells[5].classList.contains("invalid")) {
            validateBankCode(element.parentElement.cells[5]);
            return;
        }
        warning("BIC should be 6, 8 or 11 characters long and consist of word characters and numbers only");
    } else {
        element.parentElement.cells[5].classList.remove("invalid");
        element.classList.remove("invalid");
    }
}

function validateUrl(element) {
    let target = element.textContent;
    let regex = /(https|http):\/\/[\w\-]+\.[^\n\r]+$/;

    if (!regex.test(target) && !(target === "" && element.classList.contains("idp-url"))) {
        element.classList.add("invalid");
        warning("URL format is wrong, e.g. right format is https://example.test");
    } else {
        element.classList.remove("invalid");
    }
}

function validateAdapterId(element) {
    let target = element.textContent;
    let regex = /^\w+-adapter$/;

    if (!regex.test(target)) {
        element.classList.add("invalid");
        warning("Adapter Id should consist of aA-zZ, 0-9 and a hyphen(-) only, e.g. 'Adapter-12345'");
    } else {
        element.classList.remove("invalid");
    }
}

function validateBankCode(element) {
    let target = element.textContent;
    let regex = /^\d{8}$/;

    if (!regex.test(target)) {
        element.classList.add("invalid");
        if (!element.parentElement.cells[2].classList.contains("invalid")) {
            validateBic(element.parentElement.cells[2]);
            return;
        }
        warning("Bank Code should be 8 digits long and consist of numbers only");
    } else {
        element.parentElement.cells[2].classList.remove("invalid");
        element.classList.remove("invalid");
    }
}

function toUpper(element) {
    element.innerText = element.innerText.toUpperCase();
}

function forceValidation() {
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
function uneditableCells(e) {
    let rowCells = e.parentElement.parentElement.cells;
    let approach = e.parentElement.parentElement.querySelectorAll('input');

    for (let i = 1, till = (rowCells.length - 1); i < till; i++) {
        rowCells[i].removeAttribute("contenteditable");
    }

    approach.forEach(element => {
        element.disabled = true;
    })
}

function editableCells(e) {
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
function showTable() {
    let table = HIDDEN_ROW.parentElement.parentElement.parentElement;
    let message = document.querySelector(".welcome-message");

    table.hidden = false;
    message.hidden = true;
}

function clearTable() {
    let body = document.querySelectorAll("tbody>tr");

    if (body.length > 1) {
        body.forEach(e => { if (!e.className) { e.remove(); } })
    }
}

function checkMorePart() {
    let showMore = document.querySelector(".show-more");

    if (!showMore.hidden) {
        showMore.hidden = true;
    }
}

function clearContent() {
    clearTable();
    checkMorePart();

    document.querySelectorAll(".mdl-textfield__input").forEach(element => { element.value = ""; element.parentElement.classList.remove("is-dirty") });
}
// End of table part

function onEnterPress(event) {
    if (event.keyCode === 13) {
        searchButton();
    }
}

function addTooltips(e) {
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
function purgeRow(e) {
    let tableRow = e.parentElement.parentElement;

    tableRow.remove();
}

function assembleRowData(e) {
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

    function approachParser(data) {
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

function buildRow(data) {
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
    const spinner = document.querySelector(".spinner");
    const verdict = document.querySelector(".verdict");
    const amount = document.querySelector(".not-valid-amount");
    const example = document.querySelector(".example");
    const display = document.querySelector(".display");

    verdict.classList.add("hidden");
    amount.classList.add("hidden");
    example.classList.add("hidden");
    display.classList.add("hidden");

    spinner.classList.remove("hidden");

    modal.classList.toggle("hidden");
}

const createReport = (data) => {
    const temp = new Blob([JSON.stringify(data)]);
    const virtualLink = document.createElement("a");
    const virtualUrl = URL.createObjectURL(temp);
    virtualLink.href = virtualUrl;
    virtualLink.download = "report.json";
    virtualLink.click();
}
function fail(message) {
    let messageBlock = FAILURE.querySelector(".message");
    messageBlock.textContent = message;
    
    setTimeout(() => { FAILURE.style.opacity = 1 }, 500);

    setTimeout(() => { FAILURE.style.opacity = 0 }, 8000);
}

function success() {
    setTimeout(() => { SUCCESS.style.opacity = 1 }, 500);

    setTimeout(() => { SUCCESS.style.opacity = 0 }, 8000);
}

function warning(message) {
    let messageBlock = WARNING.querySelector(".message");

    if (message) {
        messageBlock.textContent = message;
    }

    setTimeout(() => { WARNING.style.opacity = 1 }, 500);

    setTimeout(() => { WARNING.style.opacity = 0 }, 8000);
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

// Requests part
function saveButton(e) {
    let row = e.parentElement.parentElement;

    for (let cell of row.cells) {
        if (cell.classList.contains("invalid")) {
            warning();
            return;
        }
    }

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
        let output = JSON.parse(response);
        row.cells[0].textContent = output.id;
        COUNTUP.update(COUNTUP.endVal + 1);
    }).catch(() => {
        fail("Saving process has failed");
    });
}

function updateButton(e) {
    let row = e.parentElement.parentElement;

    for (let cell of row.cells) {
        if (cell.classList.contains("invalid")) {
            warning();
            return;
        }
    }

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

function deleteButton(e) {
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

function upload() {
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
    })
}

async function searchButton() {
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

    try {
        response = await search(BASE_URL);

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

function merge() {
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
    })
}

async function search(URI) {
    let output = {};

    let response = await fetch(URI);
    output.headers = await response.headers.get("X-Total-Elements");
    output.data = JSON.parse(await response.text());

    return output;
}

const validate = () => {
    let file = FILE_UPLOAD_FIELD.files[0];
    let data = new FormData();

    data.append("file", file);

    toggleModal();

    fetch(BASE + "/csv/validate", {
        method: 'POST',
        body: data
    }).then(response => {
        if (response.status === 403) {
            warning("It looks like you don't have enough permissions to perform this action");
            return;
        } else if (!response.ok && response.status !== 400) {
            throw Error(response.statusText);
        }
        success();
        return response.text()
    }).then(response => {
        validationResponseHandler(JSON.parse(response));
    }).catch(() => {
        fail("Validation process failed, please check if you provided an appropriately formatted CSV file");
    })
}
// End of requests part

async function showMore() {

    let pagination = "&page=" + PAGINATOR.page + "&size=" + PAGINATOR.size;

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
        if (window.confirm("Are you sure you want to save the new entry?")) {
            saveButton(e);
        }
    } else {
        if (window.confirm("Are you sure you want to update the aspsp?")) {
            updateButton(e);
        } else {
            toggleButtons(e);
        }
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

function showButton() {
    let drawer = document.querySelector(".mdl-layout__drawer");
    let icon = document.querySelector(".expand>button>i");

    drawer.classList.toggle("is-hidden");
    icon.classList.toggle("rotate");
}

const proceedButton = () => {
    if (HIT_BUTTON === "UPLOAD") {
        upload();
    } else if (HIT_BUTTON === "MERGE") {
        merge();
    }

    toggleModal();
}

const reportButton = () => {
    createReport(VALIDATOR.data);
} 

const rejectButton = () => {
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

    let isValid = data.validationResult === "VALID";

    const verdict = document.querySelector("#verdict");
    const amount = document.querySelector("#records-amount");
    const example = document.querySelector(".display");
    const spinner = document.querySelector(".spinner");

    if (!data) {
        fail("Oops... something went wrong. Please try again to validate");
        toggleModal();
        return;
    }
    
    verdict.textContent = data.validationResult;
    spinner.classList.add("hidden");

    if (!isValid) {
        verdict.classList.add("not-valid");
        verdict.parentElement.classList.remove("hidden");
        amount.parentElement.classList.remove("hidden");
        example.classList.remove("hidden");
        document.querySelector(".example").classList.remove("hidden");
        amount.textContent = data.totalNotValidRecords;

        example.textContent = buildString(data.aspspValidationErrorReports);
    } else {
        verdict.classList.add("valid");
        verdict.parentElement.classList.remove("hidden");
        amount.parentElement.classList.add("hidden");
        example.classList.add("hidden");
        document.querySelector(".example").classList.add("hidden");
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