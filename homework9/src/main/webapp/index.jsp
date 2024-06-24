<!DOCTYPE html>
<html>

<head>
    <title>Calculator</title>
</head>
<body>
<table id="calc">
    <tr>
        <td><input id="add_operand1" type="number" value="0"></td>
        <td><p>+</p></td>
        <td><input id="add_operand2" type="number" value="0"></td>
        <td><p>=</p></td>
        <td><input id="add_result" type="text" value="0" readonly></td>
        <td><input type="button" value="Calculate" onclick="onAdd()"></td>
    </tr>
    <tr>
        <td><input id="subtract_operand1" type="number" value="0"></td>
        <td><p>-</p></td>
        <td><input id="subtract_operand2" type="number" value="0"></td>
        <td><p>=</p></td>
        <td><input id="subtract_result" type="text" value="0" readonly></td>
        <td><input type="button" value="Calculate" onclick="onSubtract()"></td>
    </tr>
    <tr>
        <td><input id="multiply_operand1" type="number" value="0"></td>
        <td><p>*</p></td>
        <td><input id="multiply_operand2" type="number" value="0"></td>
        <td><p>=</p></td>
        <td><input id="multiply_result" type="text" value="0" readonly></td>
        <td><input type="button" value="Calculate" onclick="onMultiply()"></td>
    </tr>
    <tr>
        <td><input id="div_operand1" type="number" value="0"></td>
        <td><p>/<p></p></td>
        <td><input id="div_operand2" type="number" value="0"></td>
        <td><p>=</p></td>
        <td><input id="div_result" type="text" value="0" readonly></td>
        <td><input type="button" value="Calculate" onclick="onDiv()"></td>
    </tr>
</table>
</body>

</html>

<script>
    function onAdd() {
        console.log("onAdd")
        const xmlHttp = new XMLHttpRequest();
        console.log(document.getElementById("add_operand1").value)
        xmlHttp.open( "GET", document.URL.replace('index.jsp', '') + "add?operand1=" + document.getElementById("add_operand1").value + "&operand2=" + document.getElementById("add_operand2").value, false);
        xmlHttp.send( null );
        console.log(xmlHttp.responseText);
        if (xmlHttp.status === 200) {
            document.getElementById("add_result").value = xmlHttp.responseText
        }
    }

    function onSubtract() {
        console.log("onSubtract")
        const xmlHttp = new XMLHttpRequest();
        console.log(document.getElementById("subtract_operand1").value)
        xmlHttp.open( "GET", document.URL.replace('index.jsp', '') + "subtract?operand1=" + document.getElementById("subtract_operand1").value + "&operand2=" + document.getElementById("subtract_operand2").value, false);
        xmlHttp.send( null );
        console.log(xmlHttp.responseText);
        if (xmlHttp.status === 200) {
            document.getElementById("subtract_result").value = xmlHttp.responseText
        }
    }

    function onMultiply() {
        console.log("onMultiply")
        const xmlHttp = new XMLHttpRequest();
        console.log(document.getElementById("multiply_operand1").value)
        xmlHttp.open( "GET", document.URL.replace('index.jsp', '') + "multiply?operand1=" + document.getElementById("multiply_operand1").value + "&operand2=" + document.getElementById("multiply_operand2").value, false);
        xmlHttp.send( null );
        console.log(xmlHttp.responseText);
        if (xmlHttp.status === 200) {
            document.getElementById("multiply_result").value = xmlHttp.responseText
        }
    }

    function onDiv() {
        console.log("onDiv")
        const xmlHttp = new XMLHttpRequest();
        console.log(document.getElementById("div_operand1").value)
        xmlHttp.open( "GET", document.URL.replace('index.jsp', '') + "div?operand1=" + document.getElementById("div_operand1").value + "&operand2=" + document.getElementById("div_operand2").value, false);
        xmlHttp.send( null );
        console.log(xmlHttp.responseText);
        if (xmlHttp.status === 200) {
            document.getElementById("div_result").value = xmlHttp.responseText
        }
    }
</script>
